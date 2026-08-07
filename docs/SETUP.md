# Setup Guide

## Part A — Paper plugin

1. Requirements: JDK 21, Maven.
2. From paper-plugin/, build the plugin:

       mvn clean package

   The compiled jar is in paper-plugin/target/paper-discord-bridge-1.0.0.jar.
3. Copy the jar into your server's plugins/ folder.
4. Start the server once to generate plugins/PaperDiscordBridge/config.yml,
   then stop it.
5. Edit config.yml:
       bot-url:     where the bot listens (e.g. http://127.0.0.1:8080/event)
       http-port:   port the plugin API listens on (e.g. 8787)
       auth-token:  a long random secret (SAME value as the bot's AUTH_TOKEN)
6. Restart the server. Check the console for "PaperDiscordBridge enabled".

## Part B — Discord bot

1. Go to https://discord.com/developers/applications and create an application.
2. Add a Bot and copy its token -> DISCORD_TOKEN.
3. Copy the Application ID -> CLIENT_ID.
4. In discord-bot/, copy .env.example to .env and fill it in:

       DISCORD_TOKEN=...
       CLIENT_ID=...
       BOT_HTTP_PORT=8080
       PLUGIN_API_URL=http://<server-host>:8787
       AUTH_TOKEN=<same secret as the plugin>
       EVENT_CHANNEL_ID=<channel id for join/leave messages>
       PLAYER_COUNT_CHANNEL_ID=<voice channel id, or leave blank>

5. Install dependencies:

       npm install

6. Register the slash command:

       npm run register

7. Start the bot:

       npm start

8. Invite the bot to your server with the bot and applications.commands scopes.

## Part C — Networking
- The plugin must be able to reach BOT_HTTP_PORT on the bot host.
- The bot must be able to reach the plugin's http-port on the server host.
- If they run on the same machine, 127.0.0.1 works.
- For different hosts, open the ports or use a reverse proxy / VPN. Prefer TLS.

## Verification
- Join the Minecraft server -> a message appears in EVENT_CHANNEL_ID.
- Run /players in Discord -> lists online players.
- If PLAYER_COUNT_CHANNEL_ID is set, the voice channel name updates to "Players: N".
