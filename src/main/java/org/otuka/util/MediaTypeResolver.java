package org.otuka.util;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class MediaTypeResolver {

	public static class MediaType {
		private String mediaType;
		private boolean requiredCharset = false;

		public MediaType() {

		}

		public MediaType(String mediaType, boolean requiredCharset) {
			this.mediaType = mediaType;
			this.requiredCharset = requiredCharset;
		}

		public String getMediaType() {
			return mediaType;
		}

		public MediaType setMediaType(String mediaType) {
			this.mediaType = mediaType;
			return this;
		}

		public boolean isRequiredCharset() {
			return requiredCharset;
		}

		public MediaType setRequiredCharset(boolean requireCharset) {
			this.requiredCharset = requireCharset;
			return this;
		}

		@Override
		public String toString() {
			return "[MediaType mediaType=" + mediaType + ", requireCharset=" + requiredCharset + "]";
		}

	}

	private static final Object MUTEX = new Object();

	private static MediaTypeResolver me;

	private Map<String, MediaType> mediaTypes = new HashMap<String, MediaType>();

	public static MediaTypeResolver me() {
		if (me == null) {
			synchronized (MUTEX) {
				if (me == null) {
					MediaTypeResolver config = new MediaTypeResolver();
					config.init();
					me = config;
				}
			}
		}
		return me;
	}

	private void init() {
		mediaTypes.put("js", new MediaType("application/javascript", true));
		mediaTypes.put("css", new MediaType("text/css", true));
	}

	public MediaType mediaType(String path) {
		String ext = IOUtil.extension(path);
		MediaType ret = mediaTypes.get(ext);
		if (ret != null) {
			return ret;
		}
		FileNameMap map = URLConnection.getFileNameMap();
		String str = map.getContentTypeFor(path);
		if (str == null) {
			return null;
		}
		ret = new MediaType().setMediaType(str);
		if (str.startsWith("text/")) {
			ret.setRequiredCharset(true);
		}
		return ret;
	}

}
