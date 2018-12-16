package com.wimetro;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * request 类
 * 
 * @author x'd'q
 *
 */
public class HttpRequest {

	private InputStream inputStream;
	private byte[] buffer = new byte[1024];
	private String requestLine;
	private Map<String, String> headersMap = new HashMap<>();
	private int contentLength;
	private boolean headerEnd;
	private StringBuilder header = new StringBuilder();
	private StringBuilder entity;

	public HttpRequest(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * 解析请求头 以及请求体
	 * 
	 * @throws IOException
	 */
	public void parseRequest() throws IOException {
		int bufferSize = inputStream.read(buffer);
		for (int index = 0; index < bufferSize; index++) {
			char c = (char) buffer[index];
			if (c == HttpConstant.LF) {
				int length = header.length();
				if (header.charAt(length - 1) == HttpConstant.CR) {
					if (requestLine == null) {
						requestLine = header.toString();
					} else {
						int crlf = header.lastIndexOf(HttpConstant.CRLF);
						String headerLine = header.substring(crlf + 2, length - 1);
						if (!"".equals(headerLine)) {
							putHeader(headerLine);
						} else {
							headerEnd = true;
							if (headersMap.get(HttpConstant.CONTENT_LENGTH) != null) {
								contentLength = Integer.parseInt(headersMap.get(HttpConstant.CONTENT_LENGTH));
							}
						}
					}
				}
			}
			if (contentLength != 0) {
				if (entity == null) {
					header.append(c);
					entity = new StringBuilder(contentLength);
				} else {
					entity.append(c);
				}
			} else {
				header.append(c);
			}
		}
		if (headerEnd) {
			while (contentLength > 0 && contentLength > entity.length()) {
				bufferSize = inputStream.read(buffer);
				entity.append(new String(buffer, 0, bufferSize));
			}
		} else {
			parseRequest();
		}
	}

	private void putHeader(String header) {
		int index = header.indexOf(":");
		String key = header.substring(0, index);
		String value = header.charAt(index + 1) == ' ' ? header.substring(index + 2) : header.substring(index + 1);
		headersMap.put(key, value);
	}

	public String getHeader() {
		return header.toString();
	}

	public String getEntity() {
		return entity == null ? null : entity.toString();
	}

	public String getUri() {
		return requestLine.split(" ")[1];
	}
}
