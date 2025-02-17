package com.tfkfan.vertx.network.pack.update;

import com.tfkfan.vertx.network.pack.UpdatePack;
import com.tfkfan.vertx.math.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrikeUpdatePack implements UpdatePack {
    private UUID id;
    private Vector2D position;
    private Vector2D velocity;
    private boolean isCollided;
}
