package com.tfkfan.vertx.game.model;

import com.tfkfan.vertx.game.Initializable;
import com.tfkfan.vertx.game.room.GameRoom;
import com.tfkfan.vertx.math.Vector;
import com.tfkfan.vertx.network.pack.InitPack;
import com.tfkfan.vertx.network.pack.UpdatePack;
import com.tfkfan.vertx.network.pack.init.IInitPackProvider;
import com.tfkfan.vertx.network.pack.update.IUpdatePackProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class BaseGameEntity<ID, GR extends GameRoom, IP extends InitPack, UP extends UpdatePack>
        implements Entity<ID>, Initializable<IP>, IUpdatePackProvider<UP>, IInitPackProvider<IP> {
    protected final GR gameRoom;
    protected boolean isMoving = false;
    protected boolean isAlive = true;
    protected Vector position = new Vector();
    protected Vector velocity = new Vector();
    protected Vector acceleration = new Vector();

}
