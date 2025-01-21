package com.tfkfan.webgame.network.pack.init;

import com.tfkfan.webgame.network.pack.InitPack;
import com.tfkfan.webgame.math.Vector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LootInitPack implements InitPack {
    private Long id;
    private Integer type;
    private String name;
    private Vector position;
}
