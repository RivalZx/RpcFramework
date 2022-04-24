package com.example.nettytransport.client;

import com.example.nettytransport.coder.NettyKryoDecoder;
import com.example.nettytransport.coder.NettyKryoEncoder;
import com.example.nettytransport.handler.NettyServerHandler;
import com.example.nettytransport.serialzable.kryo.KryoSerializer;
import com.example.nettytransport.transEntity.RpcRequest;
import com.example.nettytransport.transEntity.RpcResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: RPCFramework
 * @description:主要作用是开启了一服务端用于接受客户端的请求并处理
 * @author: he-zx
 * @create: 2022-04-22 15:25
 **/

public class NettyServer {
	private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

	private final int port;
	
	private NettyServer(int port){
		this.port = port;
	}

	private void run(){
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		KryoSerializer kryoSerializer = new KryoSerializer();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					//tcp默认开启了Nagle算法，该算法的作用是尽可能的发送大数据块，减少网络传输
					//TCP_NODELAY参数的作用是控制是否开启Nagle算法
					.childOption(ChannelOption.TCP_NODELAY, true)
					//是否开启TCP底层的心跳机制
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					//表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当的调大这个参数
					.option(ChannelOption.SO_BACKLOG, 128)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcRequest.class));
							ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcResponse.class));
							ch.pipeline().addLast(new NettyServerHandler());
						}
					});
			//绑定端口，同步等待绑定成功
			ChannelFuture f = b.bind(port).sync();
			
			//等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("occur exception when start server:", e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		new NettyServer(8889).run();
	}
}
