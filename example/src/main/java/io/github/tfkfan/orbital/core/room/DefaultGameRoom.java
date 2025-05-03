package io.github.tfkfan.orbital.core.room;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.event.KeyDownPlayerEvent;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.model.DefaultPlayer;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.UUID;

public class DefaultGameRoom extends AbstractGameRoom<GameState> {

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
