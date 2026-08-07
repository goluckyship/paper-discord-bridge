# Configuration Reference

## Paper plugin — config.yml
- bot-url: Full URL of the bot's event endpoint. Default: http://localhost:8080/event
- http-port: Port the plugin's HTTP API listens on. Default: 8787
- auth-token: Shared secret. Must match the bot's AUTH_TOKEN. Default: changeme (CHANGE THIS)

## Discord bot — .env
- DISCORD_TOKEN: Bot token from the Discord developer portal. Required.
- CLIENT_ID: Application ID, used to register slash commands. Required.
- GUILD_ID: Server ID to register commands instantly (optional; blank = global, slower).
- BOT_HTTP_PORT: Port the bot's HTTP server listens on. Default: 8080
- PLUGIN_API_URL: Base URL of the plugin's HTTP API. Default: http://localhost:8787
- AUTH_TOKEN: Shared secret. Must match the plugin's auth-token. Default: changeme (CHANGE THIS)
- EVENT_CHANNEL_ID: Channel where join/leave messages are sent. Required.
- PLAYER_COUNT_CHANNEL_ID: Voice channel to rename with the live count. Optional.

## Security checklist
- Use a long random auth-token (32+ characters).
- Never commit .env or a real config.yml with secrets.
- Restrict the ports to trusted networks.
