package com.tfkfan.orbital.room;

import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.event.KeyDownPlayerEvent;
import com.tfkfan.orbital.event.MouseDownPlayerEvent;
import com.tfkfan.orbital.manager.GameManager;
import com.tfkfan.orbital.math.Vector2D;
import com.tfkfan.orbital.model.DefaultGameUpdatePack;
import com.tfkfan.orbital.model.DefaultPlayer;
import com.tfkfan.orbital.model.Shot;
import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.session.PlayerSession;
import com.tfkfan.orbital.state.GameState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class DefaultGameRoom extends AbstractGameRoom {
    private List<Shot> shots = new ArrayList<>();

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

    @Override
    protected void onPlayerMouseClick(PlayerSession userSession, MouseDownPlayerEvent event) {
        DefaultPlayer player = (DefaultPlayer) userSession.getPlayer();
        if (!player.isAlive()) return;

        Vector2D target = new Vector2D(event.getTarget());
        Vector2D velocity = event.getTarget().diff(player.getPosition()).normalize().mult(50, 50);
        Shot shot = new Shot((long) (shots.size() + 1), this, target, new Vector2D(player.getPosition()));
        shot.setPosition(new Vector2D(player.getPosition()));
        shot.setVelocity(velocity);
        shots.add(shot);
    }

    @Override
    public void update(long dt) {
        final List<UpdatePack> shotsUpdatePack = updateShots(dt);
        final List<UpdatePack> playerUpdatePackList = updatePlayers(dt);
        sendUpdate(dt, currentPlayer -> new DefaultGameUpdatePack(
                currentPlayer.getPrivateUpdatePack(),
                playerUpdatePackList,
                shotsUpdatePack
        ));
    }

    List<UpdatePack> updateShots(long dt) {
        List<UpdatePack> packs = new ArrayList<>();
        Collection<Shot> removed = new ArrayList<>();
        for (Shot shot : shots) {
            shot.update(dt);
            if (Vector2D.diff(shot.getInitial(), shot.getPosition()).length() > 500) {
                removed.add(shot);
                continue;
            }
            packs.add(shot.getUpdatePack());
        }
        shots.removeAll(removed);
        return packs;
    }

}
