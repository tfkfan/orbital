package com.tfkfan.orbital.game.room;

import com.tfkfan.orbital.configuration.Fields;
import com.tfkfan.orbital.configuration.MessageTypes;
import com.tfkfan.orbital.event.InitPlayerEvent;
import com.tfkfan.orbital.event.KeyDownPlayerEvent;
import com.tfkfan.orbital.game.map.GameMap;
import com.tfkfan.orbital.game.model.players.DefaultPlayer;
import com.tfkfan.orbital.game.model.players.Direction;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.network.message.Message;
import com.tfkfan.orbital.network.message.MessageType;
import com.tfkfan.orbital.network.pack.init.GameInitPack;
import com.tfkfan.orbital.network.pack.init.IInitPackProvider;
import com.tfkfan.orbital.network.pack.shared.GameMessagePack;
import com.tfkfan.orbital.network.pack.shared.GameRoomPack;
import com.tfkfan.orbital.network.pack.shared.GameSettingsPack;
import com.tfkfan.orbital.properties.RoomProperties;
import com.tfkfan.orbital.session.UserSession;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
public class DefaultGameRoom extends AbstractGameRoom {
    private final RoomProperties roomProperties;
    private boolean started = false;

    public DefaultGameRoom(String verticleId, UUID gameRoomId,
                           GameMap map,
                           GameManager<?, ?> gameManager,
                           RoomProperties roomProperties) {
        super(map, verticleId, gameRoomId, gameManager);
        this.roomProperties = roomProperties;

        addEventListener(this::onPlayerInitRequest, InitPlayerEvent.class);
    }

    @Override
    public void onRoomCreated(List<UserSession> userSessions) {
        super.onRoomCreated(userSessions);
        broadcast(_ -> new JsonObject()
                .put(Fields.type, MessageTypes.GAME_ROOM_JOIN_SUCCESS)
                .put(Fields.data, JsonObject.mapFrom(new GameSettingsPack(roomProperties.getLoopRate())))
        );
    }

    @Override
    public void onRoomStarted() {
        started = false;
        schedulePeriodically(roomProperties.getInitDelay(), roomProperties.getLoopRate(), this::update);
        schedule(roomProperties.getEndDelay() + roomProperties.getStartDelay(), (_) -> gameManager.onBattleEnd(this));
        schedule(roomProperties.getStartDelay(), this::onBattleStarted);
        broadcast(MessageTypes.GAME_ROOM_START, new GameRoomPack(
                OffsetDateTime.now().plus(roomProperties.getStartDelay(), ChronoUnit.MILLIS).toInstant().toEpochMilli()
        ));
        log.trace("Room {} has been started", key());
    }

    @Override
    public void onBattleStarted(long timerId) {
        log.trace("Room {}. Battle has been started", key());
        started = true;

        schedule((long) (5 * 1000), (_) -> respawn());
        broadcast(MessageTypes.GAME_ROOM_BATTLE_START, new GameRoomPack(
                OffsetDateTime.now()
                        .plus(roomProperties.getEndDelay(), ChronoUnit.MILLIS)
                        .toInstant()
                        .toEpochMilli()
        ));
    }

    //room's game loop
    @Override
    public void update(long timerID) {
        if (!started) return;
        super.update(timerID);
    }

    private void respawn() {
        broadcast(MessageTypes.MESSAGE, new GameMessagePack(MessageType.ROOM.getType(),
                "New loot has been placed on battlefield")
        );

        schedule((long) (5 * 1000), (t) -> respawn());
    }

    @Override
    protected void onPlayerKeyDown(UserSession userSession, KeyDownPlayerEvent event) {
        if (!started) return;
        DefaultPlayer player = (DefaultPlayer) userSession.getPlayer();
        if (!player.isAlive()) return;
        var direction = Direction.valueOf(event.getKey());

        player.updateState(direction, event.isState());
    }

    private void onPlayerInitRequest(UserSession userSession, InitPlayerEvent event) {
        userSession.send(MessageTypes.INIT,
                new GameInitPack(
                        userSession.getPlayer().getInitPack(),
                        roomProperties.getLoopRate(),
                        map.alivePlayers(),
                        map.getPlayers().stream().map(IInitPackProvider::getInitPack).toList())
        );
    }

    @Override
    public void onRejoin(UserSession userSession, UUID reconnectKey) {
        super.onRejoin(userSession, reconnectKey);

        userSession.send(MessageTypes.GAME_ROOM_JOIN_SUCCESS,
                new GameSettingsPack(roomProperties.getLoopRate())
        );
    }

    @Override
    public void onClose(UserSession userSession) {
        userSession.send(new Message(MessageTypes.GAME_ROOM_CLOSE));
        super.onClose(userSession);
    }
}
