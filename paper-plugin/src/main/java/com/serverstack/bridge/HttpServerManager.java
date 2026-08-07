package com.serverstack.bridge;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Embedded HTTP server (JDK HttpServer) that exposes the server's player data
 * to the Discord bot. All player state reads are scheduled on the main thread.
 */
public class HttpServerManager {

    private final BridgePlugin plugin;
    private final ConfigManager config;
    private final Gson gson = new Gson();
    private HttpServer server;

    public HttpServerManager(BridgePlugin plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(config.getHttpPort()), 0);
            server.createContext("/players", new PlayersHandler());
            server.createContext("/count", new CountHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to start HTTP server: " + e.getMessage());
        }
    }

    public void stop() {
        if (server != null) {
            server.stop((int) TimeUnit.SECONDS.toMillis(1) / 1000);
        }
    }

    private boolean isAuthorized(HttpExchange exchange) {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        return ("Bearer " + config.getAuthToken()).equals(auth);
    }

    private void sendJson(HttpExchange exchange, int status, Object body) throws IOException {
        String json = gson.toJson(body);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private List<String> getOnlinePlayerNames() throws Exception {
        return plugin.getServer().getScheduler().callSyncMethod(plugin, () ->
                plugin.getServer().getOnlinePlayers().stream()
                        .map(p -> p.getName())
                        .collect(Collectors.toList())
        ).get();
    }

    class PlayersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!isAuthorized(exchange)) {
                sendJson(exchange, 401, new ErrorResponse("unauthorized"));
                return;
            }
            try {
                List<String> names = getOnlinePlayerNames();
                sendJson(exchange, 200, new PlayersResponse(names.size(), names));
            } catch (Exception e) {
                sendJson(exchange, 500, new ErrorResponse(e.getMessage()));
            }
        }
    }

    class CountHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!isAuthorized(exchange)) {
                sendJson(exchange, 401, new ErrorResponse("unauthorized"));
                return;
            }
            try {
                List<String> names = getOnlinePlayerNames();
                sendJson(exchange, 200, new CountResponse(names.size()));
            } catch (Exception e) {
                sendJson(exchange, 500, new ErrorResponse(e.getMessage()));
            }
        }
    }

    static class ErrorResponse {
        final String error;
        ErrorResponse(String error) {
            this.error = error;
        }
    }

    static class PlayersResponse {
        final int count;
        final List<String> players;
        PlayersResponse(int count, List<String> players) {
            this.count = count;
            this.players = players;
        }
    }

    static class CountResponse {
        final int count;
        CountResponse(int count) {
            this.count = count;
        }
    }
}
