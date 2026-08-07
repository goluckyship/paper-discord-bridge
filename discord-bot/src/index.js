import { config } from './config.js';
import { createHttpServer } from './httpServer.js';
import { startBot } from './bot.js';

async function main() {
  if (!config.discordToken) {
    throw new Error('DISCORD_TOKEN is not set. Copy .env.example to .env and fill it in.');
  }

  const { client, handleEvent } = startBot();
  const app = createHttpServer(handleEvent);

  app.listen(config.botHttpPort, () => {
    console.log('HTTP server listening on port ' + config.botHttpPort);
  });

  await client.login(config.discordToken);
}

main().catch((err) => {
  console.error('Failed to start:', err);
  process.exit(1);
});
