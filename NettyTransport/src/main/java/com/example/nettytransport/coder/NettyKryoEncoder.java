package com.example.nettytransport.coder;

import com.example.nettytransport.serialzable.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-21 23:33
 **/
@AllArgsConstructor
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {
	private final Serializer serializer;
	private final Class<?> genericClass;
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
		if (genericClass.isInstance(o)) {
			//1. 将对象转换为byte
			byte[] body = serializer.serialize(o);
			//2. 读取对象长度
			int length = body.length;
			//3. 写入消息对应的字节数组长度
			byteBuf.writeInt(length);
			//4. 将字节数组写入 ByteBuf 对象中
			byteBuf.writeBytes(body);
			
		}
	}
}
