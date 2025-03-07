package com.tfkfan.orbital.room;

import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.event.KeyDownPlayerEvent;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.players.DefaultPlayer;
import com.tfkfan.orbital.session.UserSession;
import com.tfkfan.orbital.state.GameState;
import com.tfkfan.orbital.state.impl.BaseGameState;

import java.util.UUID;

public class DefaultGameRoom extends AbstractGameRoom {

    public DefaultGameRoom(String verticleId, UUID gameRoomId,
                           GameState state,
                           GameManager gameManager,
                           RoomConfig roomConfig) {
        super(state, verticleId, gameRoomId, gameManager, roomConfig);
    }

    @Override
    protected void onPlayerKeyDown(UserSession userSession, KeyDownPlayerEvent event) {
        if (!started) return;
        DefaultPlayer player = (DefaultPlayer) userSession.getPlayer();
        if (!player.isAlive()) return;
        var direction = DefaultPlayer.Direction.valueOf(event.getKey());

        player.updateState(direction, event.isState());
    }
}
