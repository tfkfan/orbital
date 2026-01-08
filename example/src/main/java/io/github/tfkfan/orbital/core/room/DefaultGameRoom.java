package io.github.tfkfan.orbital.core.room;

import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.event.KeyDownPlayerEvent;
import io.github.tfkfan.orbital.core.manager.GameManager;
import io.github.tfkfan.orbital.core.math.random.Random;
import io.github.tfkfan.orbital.core.model.Direction;
import io.github.tfkfan.orbital.core.model.players.Player2D;
import io.github.tfkfan.orbital.core.network.message.MessageType;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.github.tfkfan.orbital.core.state.GameState;

import java.util.UUID;

public class DefaultGameRoom extends AbstractGameRoom<GameState> {

    public DefaultGameRoom(String verticleId, UUID gameRoomId, RoomType roomType,
                           GameState state,
                           GameManager gameManager,
                           RoomConfig roomConfig) {
        super(state, verticleId, gameRoomId, roomType, gameManager, roomConfig);
    }

    @Override
    public void onBattleStart() {
        schedule(5000L, l->broadcast(MessageType.ROOM,"Room event emit"));
    }

    @Override
    public void onJoin(PlayerSession playerSession) {
        final Player2D player = (Player2D) playerSession.getPlayer();
        if (player.isNpc())
            player.setPosition(Random.getRandomVector2D(0, 1000));
    }

    @Override
    protected void onPlayerKeyDown(PlayerSession userSession, KeyDownPlayerEvent event) {
        Player2D player = (Player2D) userSession.getPlayer();
        if (!player.isAlive()) return;
        var direction = Direction.valueOf(event.getKey());

        player.updateState(direction, event.isState());
    }
}
