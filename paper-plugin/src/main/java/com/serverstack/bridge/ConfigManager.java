package com.serverstack.bridge;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Loads and exposes the values from config.yml.
 */
public class ConfigManager {

    private final BridgePlugin plugin;
    private String botUrl;
    private int httpPort;
    private String authToken;

    public ConfigManager(BridgePlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.reloadConfig();
        FileConfiguration cfg = plugin.getConfig();
        this.botUrl = cfg.getString("bot-url", "http://localhost:8080/event");
        this.httpPort = cfg.getInt("http-port", 8787);
        this.authToken = cfg.getString("auth-token", "changeme");
    }

    public String getBotUrl() {
        return botUrl;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public String getAuthToken() {
        return authToken;
    }
}
