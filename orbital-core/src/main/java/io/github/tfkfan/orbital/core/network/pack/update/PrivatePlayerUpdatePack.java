package io.github.tfkfan.orbital.core.network.pack.update;

import io.github.tfkfan.orbital.core.network.pack.PrivateUpdatePack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivatePlayerUpdatePack implements PrivateUpdatePack {
    private long id;
}
