package com.example.nettytransport.serialzable;

import javax.sql.rowset.serial.SerialException;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-21 17:10
 **/
public interface Serializer {
	
	byte[] serialize(Object obj) throws SerialException;
	
	<T> T deserialize(byte[] bytes, Class<T> clazz) throws SerialException;
}
