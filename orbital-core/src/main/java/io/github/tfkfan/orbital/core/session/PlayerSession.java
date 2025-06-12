package io.github.tfkfan.orbital.core.session;

import io.github.tfkfan.orbital.core.model.players.Player;

public final class PlayerSession extends Session {
    private Player player;

    public PlayerSession(boolean admin) {
        super(admin);
    }

    public PlayerSession(String sessionId, boolean admin) {
        super(sessionId, admin);
    }

    public PlayerSession(String sessionId, boolean admin, boolean npc) {
        super(sessionId, admin, npc);
    }

    public PlayerSession() {
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerSession setPlayer(Player player) {
        this.player = player;
        return this;
    }
}
