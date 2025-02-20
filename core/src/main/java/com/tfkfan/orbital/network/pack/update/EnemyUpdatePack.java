package com.tfkfan.orbital.network.pack.update;

import com.tfkfan.orbital.math.Vector2D;
import com.tfkfan.orbital.network.pack.UpdatePack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnemyUpdatePack implements UpdatePack {
    private int id;
    private Vector2D position;
    private boolean isMoving;
    private boolean isAlive;
}
