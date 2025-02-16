package com.tfkfan.vertx.network.pack.update;

import com.tfkfan.vertx.network.pack.UpdatePack;
import com.tfkfan.vertx.math.Vector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortPlayerUpdatePack implements UpdatePack {
    private long id;
    private Vector position;
    private Vector mouseTarget;
    private int health;
    private int maxHealth;
    private String type;
    private String name;
}
