import 'dotenv/config';

export const config = {
  discordToken: process.env.DISCORD_TOKEN,
  clientId: process.env.CLIENT_ID,
  guildId: process.env.GUILD_ID || null,
  botHttpPort: parseInt(process.env.BOT_HTTP_PORT || '8080', 10),
  pluginApiUrl: process.env.PLUGIN_API_URL || 'http://localhost:8787',
  authToken: process.env.AUTH_TOKEN || 'changeme',
  eventChannelId: process.env.EVENT_CHANNEL_ID || null,
  playerCountChannelId: process.env.PLAYER_COUNT_CHANNEL_ID || null,
};
