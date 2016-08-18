package org.otuka.http;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import org.otuka.util.IOUtil;

public class HTTPResponse {

	private int code;

	private String mediaType;

	private String charset;

	private byte[] body;

	private Map<String, String> headers = new HashMap<String, String>();

	public Map<String, String> getHeaders() {
		return headers;
	}

	public HTTPResponse setHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public int getCode() {
		return code;
	}

	public HTTPResponse setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMediaType() {
		return mediaType;
	}

	public HTTPResponse setMediaType(String mediaType) {
		this.mediaType = mediaType;
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public HTTPResponse setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public byte[] getBody() {
		return body;
	}

	public HTTPResponse setBody(byte[] body) {
		this.body = body;
		return this;
	}

	public String bodyText() {
		return IOUtil.toString(body, charset == null ? "UTF-8" : charset);
	}

	public HTTPResponse header(String name, String value) {
		headers.put(name, value);
		return this;
	}

	public String getContentType() {
		if (charset == null) {
			return mediaType;
		}
		return format("%s; charset=%s", mediaType, charset);
	}

}
