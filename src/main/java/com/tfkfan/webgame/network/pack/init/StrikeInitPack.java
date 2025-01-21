package com.tfkfan.webgame.network.pack.init;

import com.tfkfan.webgame.network.pack.InitPack;
import com.tfkfan.webgame.math.Vector;
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
