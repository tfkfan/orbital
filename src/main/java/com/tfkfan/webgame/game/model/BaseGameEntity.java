package com.tfkfan.webgame.game.model;

import com.tfkfan.webgame.game.Initializable;
import com.tfkfan.webgame.game.room.GameRoom;
import com.tfkfan.webgame.math.Vector;
import com.tfkfan.webgame.network.pack.InitPack;
import com.tfkfan.webgame.network.pack.UpdatePack;
import com.tfkfan.webgame.network.pack.init.IInitPackProvider;
import com.tfkfan.webgame.network.pack.update.IUpdatePackProvider;
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
