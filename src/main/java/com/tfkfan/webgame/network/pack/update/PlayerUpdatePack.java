package com.tfkfan.webgame.network.pack.update;

import com.tfkfan.webgame.network.pack.UpdatePack;
import com.tfkfan.webgame.math.Vector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerUpdatePack implements UpdatePack{
    private long id;
    private Vector position;
}
