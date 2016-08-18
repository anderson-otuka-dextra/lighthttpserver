package org.otuka.lighthttpserver.internal;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Anderson Otuka (anderson.otuka@dextra-sw.com)
 */
public class SimplestErrorFilter implements Filter {

    @Override public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            System.err.println("handler error: " + e);
            handleError((HttpServletResponse) servletResponse, e);
        }
    }

    private void handleError(HttpServletResponse resp, Exception exp) {
        if (resp.isCommitted()) {
            System.err.println("Error cannot be handled to client because response is commited");
            return;
        }
        resp.reset();
        try {
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(404);
            resp.getWriter().write(exp.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void destroy() {

    }
}
