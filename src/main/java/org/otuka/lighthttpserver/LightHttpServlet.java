package org.otuka.lighthttpserver;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.otuka.util.IOUtil;
import org.otuka.util.MediaTypeResolver;

/**
 * @author Anderson Otuka (anderson.otuka@dextra-sw.com)
 */
public class LightHttpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String appPath = new File(System.getProperty("user.dir")).getAbsolutePath();
        String uri = getURI(req);
        File file = new File(appPath + "/" + uri);

        final ServletOutputStream outputStream;
        try {
            outputStream = resp.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!file.exists()) {
            resp.setStatus(404);
            resp.setContentType("text/html");
            try {
                outputStream.write(("<h1>" + file.getAbsolutePath() + "</h1><ul>").getBytes());
                outputStream.write("<p>File not found</p>".getBytes());
                outputStream.write("<p><a href=\"javascript:history.back()\">Back</a></p>".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (file.isDirectory()) {
            resp.setContentType("text/html");
            try {
                outputStream.write(("<h1>" + file.getAbsolutePath() + "</h1><ul>").getBytes());
                if (!"/".equals(uri)) {
                    outputStream.write("<li><a href=\"../\">[parent]</a></li>".getBytes());
                }
                final File[] files = file.listFiles();
                for (File file1 : files) {
                    final String name = file1.getName();
                    final String s = file1.isDirectory() ? "/" : "";
                    outputStream.write(("<li><a href=\"" + name + s + "\">" + name + s + "</a></li>").getBytes());
                }
                outputStream.write("</ul>".getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        final MediaTypeResolver.MediaType mediaType = MediaTypeResolver.me().mediaType(file.getPath());
        if (mediaType == null) {
            resp.setContentType("text/plain");
        } else {
            if (mediaType.isRequiredCharset()) {
                resp.setCharacterEncoding("UTF-8");
            }
            resp.setContentType(mediaType.getMediaType());
        }
        try {
            byte[] data = IOUtil.readAll(file.toURL());
            resp.getOutputStream().write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getURI(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        if (contextPath != null && !contextPath.isEmpty()) {
            uri = uri.replace(contextPath, "");
        }
        return uri;
    }
}
