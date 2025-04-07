package io.github.tfkfan.orbital.core.network.pack.update;

import io.github.tfkfan.orbital.core.math.Vector3D;
import io.github.tfkfan.orbital.core.network.pack.UpdatePack;
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
