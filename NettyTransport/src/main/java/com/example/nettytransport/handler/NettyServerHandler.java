package com.example.nettytransport.handler;

import com.example.nettytransport.transEntity.RpcRequest;
import com.example.nettytransport.transEntity.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-22 15:32
 **/
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	private static final AtomicInteger atomicInteger = new AtomicInteger(1);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		try {
			RpcRequest rpcRequest = (RpcRequest) msg;
			log.info("server receive msg: [{}], times:[{}]", rpcRequest, atomicInteger.getAndIncrement());
			RpcResponse messageFromServer = RpcResponse.builder().message("message from server").build();
			ChannelFuture f = ctx.writeAndFlush(messageFromServer);
			f.addListener(ChannelFutureListener.CLOSE);
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("server catch exception", cause);
		ctx.close();
	}
}
