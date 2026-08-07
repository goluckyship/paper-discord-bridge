# Privacy Policy

**Last updated:** 2026-08-07

This Privacy Policy describes how the Paper Discord Bridge software (the
"Service") handles data. The Service is self-hosted by you; this policy describes
the default behavior of the code.

## 1. Data We Process
- Minecraft usernames of players currently online (used for join/leave messages
  and the /players command).
- Discord server IDs and channel IDs you configure (for posting messages and
  renaming a voice channel).
- Slash command invocations of /players.

## 2. Data We Do Not Collect
- We do not collect or store IP addresses.
- We do not read, store, or transmit Minecraft chat messages.
- We do not store Discord message content.
- We do not collect passwords or authentication credentials beyond the bot token
  you configure yourself.

## 3. How Data Is Used
- To display who joined or left the server in your configured Discord channel.
- To answer the /players command with the current online player list.
- To update a voice channel name with the live player count, if enabled.

## 4. Data Retention
Online player data is transient: it is read live from the server and is not
persisted by the Service. We do not maintain a database of players.

## 5. Third Parties
- Discord: messages and channel updates are sent through Discord. Discord's own
  policies apply to data on its platform.
- GitHub: the source code is hosted on GitHub. No user data is sent to GitHub.

## 6. Security
Requests between the plugin and bot are authenticated with a shared Bearer
token. You are responsible for securing your deployment, including using TLS or
a trusted network for traffic between the plugin and bot.

## 7. Your Rights
As the operator of your own deployment, you control all data. To remove data,
simply stop the bot or uninstall the plugin.

## 8. Changes
We may update this policy from time to time. Changes are posted in this file on
the GitHub repository.

## 9. Contact
For privacy questions, open an issue on the GitHub repository.
