package com.serverstack.bridge;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Forwards join/quit events to the Discord bot via the webhook.
 */
public class PlayerEventListener implements Listener {

    private final BridgePlugin plugin;
    private final DiscordWebhook webhook;

    public PlayerEventListener(BridgePlugin plugin, DiscordWebhook webhook) {
        this.plugin = plugin;
        this.webhook = webhook;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        webhook.sendEvent("join", event.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        webhook.sendEvent("quit", event.getPlayer().getName());
    }
}
