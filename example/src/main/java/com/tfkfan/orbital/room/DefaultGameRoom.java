package com.tfkfan.orbital.room;

import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.event.KeyDownPlayerEvent;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.model.DefaultPlayer;
import com.tfkfan.orbital.session.PlayerSession;
import com.tfkfan.orbital.state.GameState;

import java.util.UUID;

public class DefaultGameRoom extends AbstractGameRoom {

    public DefaultGameRoom(String verticleId, UUID gameRoomId,
                           GameState state,
                           GameManager gameManager,
                           RoomConfig roomConfig) {
        super(state, verticleId, gameRoomId, gameManager, roomConfig);
    }

    @Override
    protected void onPlayerKeyDown(PlayerSession userSession, KeyDownPlayerEvent event) {
        DefaultPlayer player = (DefaultPlayer) userSession.getPlayer();
        if (!player.isAlive()) return;
        var direction = DefaultPlayer.Direction.valueOf(event.getKey());

        player.updateState(direction, event.isState());
    }
}
