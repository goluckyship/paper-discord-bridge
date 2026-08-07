# Architecture

## Components

Paper plugin (paper-plugin/)
- Listens for player join/quit events on the server.
- Runs an embedded HTTP server (JDK HttpServer) exposing:
    - GET /players  -> JSON { count, players: [names] }
    - GET /count    -> JSON { count }
- On join/quit, POSTs an event to the bot's HTTP endpoint.

Discord bot (discord-bot/)
- Runs an Express HTTP server exposing:
    - POST /event   -> receives { type, player, timestamp } from the plugin
    - GET /health   -> liveness check
- Connects to the Discord Gateway (discord.js).
- Slash command: /players queries the plugin's GET /players.
- Optional: every 60s queries GET /count and renames a voice channel.

## Data flow

Player joins the server
    1. Paper fires PlayerJoinEvent.
    2. Plugin POSTs { type: "join", player, timestamp } to the bot POST /event.
    3. Bot sends a message to the configured Discord channel.

Someone runs /players in Discord
    1. Bot calls the plugin GET /players (with Bearer token).
    2. Plugin returns the online player list (fetched on the main thread).
    3. Bot replies with an embed listing the players.

Live player count (optional)
    1. Every 60s the bot calls the plugin GET /count.
    2. Bot renames the configured voice channel to "Players: N".

## Security
- Both directions use a shared Bearer token (auth-token / AUTH_TOKEN).
- The token must match on both sides; mismatched tokens are rejected with 401.
- Put the bot and plugin behind a trusted network or a reverse proxy with TLS.
- Never commit your real .env or config.yml with secrets — use the example files.

## Why direct HTTP?
Direct plugin-to-bot HTTP avoids alts, macros, and AFK bots entirely. The plugin
knows the server state authoritatively and exposes it via a tiny API; the bot
queries it. No Minecraft client is involved.
