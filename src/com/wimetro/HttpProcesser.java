package com.wimetro;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 请求处理类
 * 
 * @author x'd'q
 *
 */
public class HttpProcesser implements Runnable {

	private Socket socket;

	public HttpProcesser(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			HttpRequest request = new HttpRequest(inputStream);
			request.parseRequest();
			System.out.print(request.getHeader());
			if (request.getEntity() != null) {
				System.out.println(request.getEntity());
			}
			OutputStream outputStream = socket.getOutputStream();
			HttpResponse response = new HttpResponse(outputStream);
			response.setRequest(request);
			response.sendResponse("this is a msg from xdq server!!!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
