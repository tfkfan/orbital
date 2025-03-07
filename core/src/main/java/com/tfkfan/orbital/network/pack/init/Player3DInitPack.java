package com.tfkfan.orbital.network.pack.init;

import com.tfkfan.orbital.math.Vector3D;
import com.tfkfan.orbital.network.pack.InitPack;
import lombok.Data;

@Data
public class Player3DInitPack implements InitPack {
    private final Long id;
    private final Vector3D position;
}
