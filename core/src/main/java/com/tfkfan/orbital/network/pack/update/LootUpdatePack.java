package com.tfkfan.orbital.network.pack.update;

import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.math.Vector2D;
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
    private Vector2D position;
}
