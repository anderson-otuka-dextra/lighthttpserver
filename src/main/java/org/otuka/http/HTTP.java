package org.otuka.http;

public class HTTP {

	private HTTPRequest request = new HTTPRequest();
	private HTTPResponse response;

	public static HTTP create() {
		return new HTTP();
	}

	public HTTPRequest request() {
		return request;
	}

	public HTTP execute() {
		response = new HTTPService().execute(request);
		return this;
	}

//	public HTTP validate(int... codes) {
//		if (codes == null) {
//			return this;
//		}
//		int code = response.getCode();
//		if (Colls.indexOf(code, codes) < 0) {
//			throw new RuntimeException("error invoking: " + handler.getUrl() +
//				"\nexpected codes: " + Arrays.toString(codes) + ", but was: " + code + "\nbody:" + response.bodyDebug());
//		}
//		return this;
//	}

	public HTTPResponse response() {
		return response;
	}

}
