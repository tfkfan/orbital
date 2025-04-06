package com.tfkfan.orbital.network.pack.shared;

import com.tfkfan.orbital.network.pack.Pack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionPack implements Pack {
    private String message;
}
