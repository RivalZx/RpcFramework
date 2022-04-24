package com.example.client;

import com.example.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-21 21:47
 **/
public class HelloClient {
	public static final Logger logger = LoggerFactory.getLogger(HelloClient.class);
	
	public Object send(Message message, String host, int port) {
		try (Socket socket = new Socket(host, port)) {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(message);
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			return objectInputStream.readObject();
		} catch (Exception e) {
			logger.error("occur exception:", e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		HelloClient helloClient = new HelloClient();
		Message message = (Message) helloClient.send(new Message("content from client"), "127.0.0.1", 6666);
		System.out.println("client receive message:" + message.getContent());
	}
}
