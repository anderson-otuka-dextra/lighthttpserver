package org.otuka.lighthttpserver;

import java.io.Closeable;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.otuka.lighthttpserver.internal.SimplestErrorFilter;

public class LightHttpServer implements Closeable {

	private final String host;
	private final int port;
	private Server server;

	public LightHttpServer(String host, int port) {
		System.out.println("Creating Server...");
		this.host = host;
		this.port = port;
		this.server = new Server();
		try {
			configureJetty();
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void configureJetty() {
		ServletHandler handler = new ServletHandler();
		handler.addFilterWithMapping(SimplestErrorFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		handler.addServletWithMapping(LightHttpServlet.class, "/*");
		server.setHandler(handler);
	}

	public void close() {
		if (server != null) {
			try {
				server.stop();
			} catch (Exception e) {
				System.out.println("Error shutting down: " + e);
			}
		}
	}

	public boolean run() {
		System.out.println("Starting server.");
		bind();
		return listen();
	}

	public int bind() {
		System.out.println("Binding host " + host + " and port " + port);
		try {
			ServerConnector connector = new ServerConnector(server);
			connector.setHost(host);
			connector.setPort(port);
			server.addConnector(connector);
			connector.start();
			return connector.getLocalPort();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean listen() {
		System.out.println("Server listening.");
		try {
			server.join();
		} catch (InterruptedException e) {
			System.out.println("Server interrupted.");
			return false;
		}
		System.out.println("Server shutting down.");
		return true;
	}
}
