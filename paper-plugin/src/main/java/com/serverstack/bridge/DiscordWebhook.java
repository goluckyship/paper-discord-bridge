package com.serverstack.bridge;

import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Sends join/leave events to the Discord bot's HTTP endpoint asynchronously,
 * so the main server thread is never blocked.
 */
public class DiscordWebhook {

    private final BridgePlugin plugin;
    private final ConfigManager config;
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public DiscordWebhook(BridgePlugin plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void sendEvent(String type, String playerName) {
        try {
            JsonObject payload = new JsonObject();
            payload.addProperty("type", type);
            payload.addProperty("player", playerName);
            payload.addProperty("timestamp", System.currentTimeMillis());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getBotUrl()))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + config.getAuthToken())
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .timeout(Duration.ofSeconds(5))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() >= 400) {
                            plugin.getLogger().warning("Bot returned status "
                                    + response.statusCode() + " for " + type + " event.");
                        }
                    })
                    .exceptionally(e -> {
                        plugin.getLogger().warning("Failed to forward " + type
                                + " event to bot: " + e.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to build " + type + " event: " + e.getMessage());
        }
    }
}
