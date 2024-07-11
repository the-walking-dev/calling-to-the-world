import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

public class Main {
    private static final int HTTP_PORT = 8090;

    private static final Logger logger = Logger.getLogger("multitenant-poc-logger");

    public static void main(String...args) throws IOException {
        logger.info("Multiple TDLib instances PoC");
        var clients = Map.of(
            "tenant1", new MultitenantClient("tenant1"),
            "tenant2", new MultitenantClient("tenant2")
        );

        var server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
        server.createContext("/").setHandler(exchange -> {
            var response  = exchange.getRequestURI().toString().substring(1).split("=");
            var tenant    = response[0];
            var authCode  = response[1];

            new Thread(() -> clients.get(tenant).sendAuthCode(authCode)).start(); // send the auth code in other thread
            exchange.sendResponseHeaders(200, 0);
            try (var os = exchange.getResponseBody()) {
                os.write("".getBytes());
            }
        });
        server.start();
        logger.info("server started successfully. Waiting for tenant auth codes....");
        logger.info(String.format("  listening on port %d", HTTP_PORT));
    }
}
