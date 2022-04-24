package com.example.server;

import com.example.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: RPCFramework
 * @description:
 * @author: he-zx
 * @create: 2022-04-21 21:33
 **/
public class HelloServer {
	public static final Logger logger = LoggerFactory.getLogger(HelloServer.class);
	
	public void start(int port) {
		//1. 创建ServerSocket对象并且绑定一个端口
		try (ServerSocket server = new ServerSocket(port)) {
			Socket socket;
			while ((socket = server.accept()) != null) {
				logger.info("client connection!");
				try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
					 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())){
					Message message = (Message) objectInputStream.readObject();
					
					logger.info("server receive message:" + message.getContent());
					message.setContent("new content");
					objectOutputStream.writeObject(message);
					objectOutputStream.flush();
				} catch (ClassNotFoundException | IOException e) {
					logger.error("occur exception:", e);
				}
			}
		} catch (IOException e) {
			logger.error("occur IOException:", e);
		}
	}
	
	public static void main(String[] args) {
//		HelloServer server = new HelloServer();
//		server.start(6666);
	}
	
	
	
}
