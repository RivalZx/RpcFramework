package com.example.nettytransport.coder;

import com.example.nettytransport.serialzable.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-22 15:14
 **/
@AllArgsConstructor
@Slf4j
public class NettyKryoDecoder extends ByteToMessageDecoder {
	private final Serializer serializer;
	private final Class<?> genericClass;
	
	/**
	 * Netty传输的消息长度，也就是对象序列化后对应的字节数组的大小，存储在ByteBuf头部
	 */
	private static final int BODY_LENGTH = 4;
	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		//1. byteBuf中写入的消息长度所占的字节数已经是4了，所以byteBuf的可读字节必须大于4
		if (in.readableBytes() >= BODY_LENGTH) {
			in.markReaderIndex();
			int dataLength = in.readInt();
			
			if (dataLength < 0 || in.readableBytes() < 0) {
				log.error("data length or byteBuf readableBytes is not valid");
				return;
			}
			
			if (in.readableBytes() < dataLength) {
				in.resetReaderIndex();
				return;
			}
			
			byte[] body = new byte[dataLength];
			in.readBytes(body);
			
			Object obj = serializer.deserialize(body, genericClass);
			out.add(obj);
			log.info("successful decode ByteBuf to Object");
		}
	}
}
