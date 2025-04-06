package com.tfkfan.orbital.network.pack.update;

import com.tfkfan.orbital.math.Vector3D;
import com.tfkfan.orbital.network.pack.UpdatePack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player3DUpdatePack implements UpdatePack{
    private long id;
    private Vector3D position;
}
