package com.tfkfan.vertx.network.pack.update;

import com.tfkfan.vertx.network.pack.UpdatePack;
import com.tfkfan.vertx.math.Vector;
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
