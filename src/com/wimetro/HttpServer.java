package com.wimetro;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * http服务类
 * 
 * @author x'd'q
 *
 */
public class HttpServer {

	public int port = 9999;

	public void start() {
		ServerSocket serverSocket = null;
		ExecutorService pool = Executors.newCachedThreadPool();
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = serverSocket.accept();
				pool.submit(new HttpProcesser(socket));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
