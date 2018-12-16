package com.wimetro;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * response类
 * 
 * @author x'd'q
 *
 */
public class HttpResponse {

	HttpRequest request;

	private OutputStream outputStream;

	public HttpResponse(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	public void sendResponse(String content) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
		StringBuffer sb = new StringBuffer();
		sb.append("HTTP/1.1").append(" ").append("200").append(" ").append("OK").append(HttpConstant.CRLF);
		sb.append("Content-Type:text/html;charset=UTF-8").append(HttpConstant.CRLF);
		sb.append("Content-Length:").append(content.toString().getBytes("UTF-8").length).append(HttpConstant.CRLF);
		sb.append(HttpConstant.CRLF);
		sb.append(content);
		writer.write(sb.toString());
		writer.flush();
		if (writer != null) {
			writer.close();
		}
	}
}
