package io.github.tfkfan.orbital.core.network.pack.shared;

import io.github.tfkfan.orbital.core.network.pack.Pack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionPack implements Pack {
    private String message;
}
