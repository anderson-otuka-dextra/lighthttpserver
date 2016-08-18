package org.otuka.lighthttpserver;

/**
 * @author Anderson Otuka (anderson.otuka@dextra-sw.com)
 */
public class Main {

    public static void main(String[] args) {
        String host;
        int port;
        if (args.length == 0) {
            System.out.println("Starting App! You can configure IP and Port by running with args 0.0.0.0:1234");
            host = "0.0.0.0";
            port = 5432;
        } else if (args.length == 1) {
            if (args[0].contains(":")) {
                final String[] split = args[0].split(":");
                host = split[0];
                port = Integer.valueOf(split[1]);
            } else {
                host = args[0];
                port = 5432;
            }
            System.out.println("Starting App! Host=" + host + "; Port=" + port);
        } else {
            System.out.println("Expected 0 or 1 arguments only.");
            return;
        }
        try (LightHttpServer server = new LightHttpServer(host, port)) {
            System.out.println("Point your browser to http://localhost:" + server.bind() + "/");
            server.listen();
        }
    }
}
