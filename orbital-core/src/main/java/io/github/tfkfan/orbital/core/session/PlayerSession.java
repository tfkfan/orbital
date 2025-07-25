package io.github.tfkfan.orbital.core.session;

import io.github.tfkfan.orbital.core.model.players.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
}
