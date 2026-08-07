# ServerStack Paper <-> Discord Bridge

A lightweight, direct bridge between a Paper Minecraft server and a Discord bot.
The plugin and the bot communicate over plain HTTP — no alt accounts, no macros,
no AFK bots.

## Features
- Sends a Discord message when a player joins or leaves the server.
- /players slash command lists everyone currently online.
- (Optional) Live player count in a voice channel name, refreshed every minute.

## How it works
The Paper plugin runs a tiny HTTP API that returns the online player list and
count. The Discord bot runs its own HTTP endpoint that the plugin posts
join/leave events to. Both sides authenticate requests with a shared token.

See docs/ARCHITECTURE.md for the full data flow.

## Quick start
1. Build and install the plugin — see docs/SETUP.md.
2. Create the Discord bot and run it — see docs/SETUP.md.
3. Make sure the shared auth-token / AUTH_TOKEN matches on both sides.

## Project layout
- paper-plugin/   the Paper plugin (Java)
- discord-bot/    the Discord bot (Node.js)
- docs/           setup, architecture, and config reference

## Requirements
- Paper 1.21+ (Java 21)
- Node.js 18+
- A Discord application with a bot token
- Network reachability between the server host and the bot host
