package com.tfkfan.vertx.network.pack.update;

import com.tfkfan.vertx.network.pack.PrivateUpdatePack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivatePlayerUpdatePack implements PrivateUpdatePack {
    private long id;
}
