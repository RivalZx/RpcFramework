package com.example.nettytransport.client;

import com.example.nettytransport.coder.NettyKryoDecoder;
import com.example.nettytransport.coder.NettyKryoEncoder;
import com.example.nettytransport.handler.NettyClientHandler;
import com.example.nettytransport.serialzable.kryo.KryoSerializer;
import com.example.nettytransport.transEntity.RpcRequest;
import com.example.nettytransport.transEntity.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-21 23:14
 **/
public class NettyClient {
	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
	private final String host;
	private final int port;
	//创建Bootstrap并初始化
	private static final Bootstrap b;
	
	public NettyClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	static {
		EventLoopGroup eventExecutors = new NioEventLoopGroup();
		b = new Bootstrap();
		KryoSerializer kryoSerializer = new KryoSerializer();

		b.group(eventExecutors)
				.channel(NioSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				//连接超时事件，超过这个时间还是建立不上的话则代表连接失败
				//如果15秒之内没有发送数据给服务端的话，就发送一次心跳请求
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
						channel.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
						channel.pipeline().addLast(new NettyClientHandler());
					}
				});
	}
	
	/**
	 *
	 * @param rpcRequest
	 * @return
	 */
	public RpcResponse sendMessage(RpcRequest rpcRequest) {
		try {
			//通过bootstrap对象连接服务端
			ChannelFuture f = b.connect(host, port).sync();
			logger.info("client connect {}", host + ":" + port);
			Channel channel = f.channel();
			logger.info("send message!");
			if (channel != null) {
				//通过Channel向服务端发送消息RpcRequest
				channel.writeAndFlush(rpcRequest).addListener(future -> {
					if (future.isSuccess()) {
						logger.info("client send message : [{}]", rpcRequest.toString());
					} else {
						logger.error("Send message failed!", future.cause());
					}
				});
				//阻塞等待，直到Channel关闭
				channel.closeFuture().sync();
				logger.info("channel close success!");
				//将服务端返回的数据也就是RpcResponse对象取出
				AttributeKey<RpcResponse> rpcResponse = AttributeKey.valueOf("rpcResponse");
				return channel.attr(rpcResponse).get();
			}
		} catch (InterruptedException e) {
			logger.error("occur exception when connect server");
		}
		return null;
	}
	
	public static void main(String[] args) {
		RpcRequest rpcRequest = RpcRequest.builder()
				.interfaceName("interface")
				.methodName("hello").build();
		
		NettyClient nettyClient = new NettyClient("127.0.0.1", 8889);
		for (int i = 0; i < 3; i++) {
			nettyClient.sendMessage(rpcRequest);
		}
		RpcResponse rpcResponse = nettyClient.sendMessage(rpcRequest);
		System.out.println("rpcResponse : " + rpcResponse.toString());
	}
}
