package io.github.tfkfan.orbital.core.network.pack.update;

import io.github.tfkfan.orbital.core.network.pack.UpdatePack;
import io.github.tfkfan.orbital.core.math.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player2DUpdatePack implements UpdatePack{
    private long id;
    private Vector2D position;
}
