package com.tfkfan.webgame.network.pack.update;

import com.tfkfan.webgame.network.pack.UpdatePack;
import com.tfkfan.webgame.math.Vector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LootUpdatePack implements UpdatePack {
    private long id;
    private String type;
    private String name;
    private Vector position;
}
