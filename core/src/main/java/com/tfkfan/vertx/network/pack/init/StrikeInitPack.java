package com.tfkfan.vertx.network.pack.init;

import com.tfkfan.vertx.network.pack.InitPack;
import com.tfkfan.vertx.math.Vector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrikeInitPack implements InitPack{
    private UUID id;
    private Vector position;
}
