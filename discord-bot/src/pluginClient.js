import { config } from './config.js';

// Queries the Paper plugin's HTTP API for the online player list.
export async function fetchPlayers() {
  const res = await fetch(config.pluginApiUrl + '/players', {
    headers: { Authorization: 'Bearer ' + config.authToken },
  });
  if (!res.ok) throw new Error('Plugin API error: ' + res.status);
  return res.json();
}

// Queries the Paper plugin's HTTP API for just the player count.
export async function fetchPlayerCount() {
  const res = await fetch(config.pluginApiUrl + '/count', {
    headers: { Authorization: 'Bearer ' + config.authToken },
  });
  if (!res.ok) throw new Error('Plugin API error: ' + res.status);
  return res.json();
}
