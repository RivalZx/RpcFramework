package com.example.nettytransport.serialzable.kryo;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.example.nettytransport.serialzable.Serializer;

import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @program: RPCFramework
 * @description: 使用kryo做序列化
 * @author: he-zx
 * @create: 2022-04-21 16:57
 **/
public class KryoSerializer implements Serializer {
	
	private final ThreadLocal<Kryo> threadLocal = ThreadLocal.withInitial(() -> {
		Kryo kryo = new Kryo();
		return kryo;
	});
	
	
	@Override
	public byte[] serialize(Object obj) throws SerialException {
		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 Output output = new Output(byteArrayOutputStream)) {
			//通过threadLocal获取线程安全的kryo
			Kryo kryo = threadLocal.get();
			//将obj序列化为二进制数组
			kryo.writeObject(output, obj);
			//删除threadLocal,保证内存不泄漏
			threadLocal.remove();
			return output.toBytes();
		} catch (Exception e) {
			throw new SerialException("Serialization failed!");
		}
	}
	
	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerialException  {
		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			 Input input = new Input(byteArrayInputStream)) {
			Kryo kryo = threadLocal.get();
			Object o = kryo.readObject(input, clazz);
			threadLocal.remove();
			return clazz.cast(o);
		} catch (Exception e) {
			throw new SerialException("Deserialization failed!");
		}
	}
}
