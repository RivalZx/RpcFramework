package com.example.nettytransport.handler;

import com.example.nettytransport.transEntity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: RPCFramework
 * @description:自定义ChannelHandler处理服务端消息
 * @author: he-zx
 * @create: 2022-04-22 14:54
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

	public static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		try {
			RpcResponse rpcResponse = (RpcResponse) msg;
			logger.info("client receive msg: [{}]", rpcResponse.toString());
			
			//申明一个AttributeKey对象
			AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
			
			//将服务端的返回结果保存到AttributeMap上，AttributeMap可以看作是一个Channel的共享数据源
			//AttributeMap的key是AttributeKey，value是Attribute
			ctx.channel().attr(key).set(rpcResponse);
			ctx.channel().close();
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("client caught exception", cause);
		ctx.close();
	}
}
