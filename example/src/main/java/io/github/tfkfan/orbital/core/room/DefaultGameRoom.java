package io.github.tfkfan.orbital.core.room;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.event.KeyDownPlayerEvent;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.math.random.Random;
import io.github.tfkfan.orbital.core.math.random.RandomBiInitializer;
import io.github.tfkfan.orbital.core.math.random.RandomInitializer;
import io.github.tfkfan.orbital.core.model.DefaultPlayer;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DefaultGameRoom extends AbstractGameRoom<GameState> {

    public DefaultGameRoom(String verticleId, UUID gameRoomId, RoomType roomType,
                           GameState state,
                           GameManager gameManager,
                           RoomConfig roomConfig) {
        super(state, verticleId, gameRoomId, roomType, gameManager, roomConfig);
    }

    @Override
    public void onJoin(PlayerSession playerSession) {
        super.onJoin(playerSession);
        final DefaultPlayer player = (DefaultPlayer) playerSession.getPlayer();
        if (player.isNpc())
            player.setPosition(Random.getRandomVector2D(0, 1000));
    }

    @Override
    protected void onPlayerKeyDown(PlayerSession userSession, KeyDownPlayerEvent event) {
        DefaultPlayer player = (DefaultPlayer) userSession.getPlayer();
        if (!player.isAlive()) return;
        var direction = DefaultPlayer.Direction.valueOf(event.getKey());

        player.updateState(direction, event.isState());
    }
}
