package io.github.tfkfan.orbital.core.event;

public class KeyDownPlayerEvent extends AbstractEvent{
   private String key;
   private boolean state;

   public KeyDownPlayerEvent() {
   }
   public KeyDownPlayerEvent(String key, boolean state) {
      this.key = key;
      this.state = state;
   }

   public String getKey() {
      return key;
   }

   public KeyDownPlayerEvent setKey(String key) {
      this.key = key;
      return this;
   }

   public boolean isState() {
      return state;
   }

   public KeyDownPlayerEvent setState(boolean state) {
      this.state = state;
      return this;
   }
}