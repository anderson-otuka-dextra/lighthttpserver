package org.otuka.lighthttpserver.handler;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import org.otuka.util.IOUtil;
import org.otuka.util.MediaTypeResolver;

/**
 * @author Anderson Otuka (anderson.otuka@dextra-sw.com)
 */
public class ResourceFileHandler {

    private final String uri;

    public ResourceFileHandler(String uri) {
        if ("/".equals(uri)) {
            this.uri = "index.html";
        } else {
            this.uri = uri.substring(1);
        }
    }

    public boolean parse(HttpServletResponse resp) {
        URL fileURL = Object.class.getResource(uri);
        if (fileURL == null) {
            throw new RuntimeException(format("URI not found: %s", uri));
        }
        MediaTypeResolver.MediaType mediaType = MediaTypeResolver.me().mediaType(fileURL.getPath());
        if (mediaType == null) {
            mediaType = new MediaTypeResolver.MediaType().setMediaType("application/octet-stream");
        }
        resp.setContentType(mediaType.getMediaType());
        if (mediaType.isRequiredCharset()) {
            resp.setCharacterEncoding("UTF-8");
        }

        byte[] data = IOUtil.readAll(fileURL);
        try {
            resp.getOutputStream().write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
