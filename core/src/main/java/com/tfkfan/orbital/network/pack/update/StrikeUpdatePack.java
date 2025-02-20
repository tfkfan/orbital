package com.tfkfan.orbital.network.pack.update;

import com.tfkfan.orbital.network.pack.UpdatePack;
import com.tfkfan.orbital.math.Vector2D;
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
