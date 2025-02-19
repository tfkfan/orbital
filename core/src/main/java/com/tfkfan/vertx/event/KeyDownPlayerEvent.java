package com.tfkfan.vertx.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyDownPlayerEvent extends AbstractEvent{
   private String key;
   private boolean state;
}