package com.tfkfan.vertx.network.pack.update;

import com.tfkfan.vertx.network.pack.UpdatePack;
import com.tfkfan.vertx.math.Vector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrikeUpdatePack implements UpdatePack {
    private UUID id;
    private Vector position;
    private Vector velocity;
    private boolean isCollided;
}
