package com.tfkfan.orbital.network.pack.update;

import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.math.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortPlayerUpdatePack implements UpdatePack {
    private long id;
    private Vector2D position;
    private Vector2D mouseTarget;
    private int health;
    private int maxHealth;
    private String type;
    private String name;
}
