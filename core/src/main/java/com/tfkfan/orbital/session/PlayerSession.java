package com.tfkfan.orbital.session;

import com.tfkfan.orbital.model.players.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class PlayerSession extends Session {
    private Player player;

    public PlayerSession(boolean admin) {
        super(admin);
    }

    public PlayerSession(String sessionId, boolean admin) {
        super(sessionId, admin);
    }

    public PlayerSession(String sessionId, boolean admin, boolean test) {
        super(sessionId, admin, test);
    }
}
