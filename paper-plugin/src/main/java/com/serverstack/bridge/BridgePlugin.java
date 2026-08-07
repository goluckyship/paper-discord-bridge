package com.serverstack.bridge;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin entry point. Starts the HTTP API server and registers the
 * player join/quit listener that forwards events to the Discord bot.
 */
public class BridgePlugin extends JavaPlugin {

    private ConfigManager configManager;
    private HttpServerManager httpServerManager;
    private DiscordWebhook discordWebhook;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        configManager = new ConfigManager(this);
        configManager.load();

        discordWebhook = new DiscordWebhook(this, configManager);

        httpServerManager = new HttpServerManager(this, configManager);
        httpServerManager.start();

        getServer().getPluginManager().registerEvents(
                new PlayerEventListener(this, discordWebhook), this);

        getLogger().info("PaperDiscordBridge enabled. HTTP API on port "
                + configManager.getHttpPort());
    }

    @Override
    public void onDisable() {
        if (httpServerManager != null) {
            httpServerManager.stop();
        }
        getLogger().info("PaperDiscordBridge disabled.");
    }
}
