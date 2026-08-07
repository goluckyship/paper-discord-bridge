import { REST, Routes } from 'discord.js';
import { config } from './config.js';

// Slash commands the bot registers with Discord.
const commands = [
  {
    name: 'players',
    description: 'Show the players currently online on the Minecraft server.',
  },
];

const rest = new REST({ version: '10' }).setToken(config.discordToken);

try {
  console.log('Registering slash commands...');
  if (config.guildId) {
    await rest.put(
      Routes.applicationGuildCommands(config.clientId, config.guildId),
      { body: commands }
    );
  } else {
    await rest.put(
      Routes.applicationCommands(config.clientId),
      { body: commands }
    );
  }
  console.log('Slash commands registered.');
} catch (err) {
  console.error(err);
  process.exit(1);
}
