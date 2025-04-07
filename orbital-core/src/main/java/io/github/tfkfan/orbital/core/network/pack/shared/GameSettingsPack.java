package io.github.tfkfan.orbital.core.network.pack.shared;

import io.github.tfkfan.orbital.core.network.pack.InitPack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSettingsPack implements InitPack{
    private long loopRate;
}
