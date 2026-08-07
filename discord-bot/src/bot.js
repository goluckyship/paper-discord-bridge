import { Client, GatewayIntentBits, Events, EmbedBuilder, ChannelType } from 'discord.js';
import { config } from './config.js';
import { fetchPlayers, fetchPlayerCount } from './pluginClient.js';

// Creates the Discord client and wires up slash commands + event handling.
// Returns { client, handleEvent } so index.js can feed plugin events in.
export function startBot() {
  const client = new Client({ intents: [GatewayIntentBits.Guilds] });

  client.once(Events.ClientReady, (c) => {
    console.log('Discord bot logged in as ' + c.user.tag);
    startPlayerCountSync(client);
  });

  client.on(Events.InteractionCreate, async (interaction) => {
    if (!interaction.isChatInputCommand()) return;
    if (interaction.commandName === 'players') {
      await interaction.deferReply();
      try {
        const data = await fetchPlayers();
        const embed = buildPlayersEmbed(data);
        await interaction.editReply({ embeds: [embed] });
      } catch (err) {
        await interaction.editReply('Could not reach the server: ' + err.message);
      }
    }
  });

  const handleEvent = (event) => handlePluginEvent(client, event);

  return { client, handleEvent };
}

function buildPlayersEmbed(data) {
  const list = data.players && data.players.length > 0
    ? data.players.map((p) => '- ' + p).join('\n')
    : '*No players online*';
  return new EmbedBuilder()
    .setTitle('Online Players')
    .setColor(0x57F287)
    .setDescription(list)
    .setFooter({ text: data.count + ' player(s) online' })
    .setTimestamp();
}

async function handlePluginEvent(client, event) {
  if (!client.isReady()) return;
  if (!config.eventChannelId) return;
  const channel = client.channels.cache.get(config.eventChannelId);
  if (!channel) return;
  const emoji = event.type === 'join' ? '🟢' : '🔴';
  const verb = event.type === 'join' ? 'joined' : 'left';
  try {
    await channel.send(emoji + ' **' + event.player + '** ' + verb + ' the server.');
  } catch (err) {
    console.error('Failed to send event message: ' + err.message);
  }
}

function startPlayerCountSync(client) {
  if (!config.playerCountChannelId) return;
  const update = async () => {
    try {
      const data = await fetchPlayerCount();
      const channel = client.channels.cache.get(config.playerCountChannelId);
      if (channel && channel.type === ChannelType.GuildVoice) {
        await channel.setName('Players: ' + data.count);
      }
    } catch (err) {
      console.error('Player count sync failed: ' + err.message);
    }
  };
  update();
  setInterval(update, 60000);
}
