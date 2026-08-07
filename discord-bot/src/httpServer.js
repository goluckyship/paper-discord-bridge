import express from 'express';
import { config } from './config.js';

// Creates the Express app that receives events from the Paper plugin.
// onEvent is called with { type, player, timestamp } for each join/leave.
export function createHttpServer(onEvent) {
  const app = express();
  app.use(express.json());

  app.get('/health', (req, res) => res.json({ ok: true }));

  app.post('/event', (req, res) => {
    if (req.headers.authorization !== 'Bearer ' + config.authToken) {
      return res.status(401).json({ error: 'unauthorized' });
    }
    const body = req.body || {};
    const type = body.type;
    const player = body.player;
    const timestamp = body.timestamp;
    if (!type || !player) {
      return res.status(400).json({ error: 'missing type or player' });
    }
    onEvent({ type, player, timestamp: timestamp || Date.now() });
    return res.json({ ok: true });
  });

  return app;
}
