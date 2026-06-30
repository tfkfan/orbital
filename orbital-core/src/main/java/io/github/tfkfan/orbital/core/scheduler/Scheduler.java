package io.github.tfkfan.orbital.core.scheduler;

import io.vertx.core.Handler;

public interface Scheduler {
    void runOnContext(Handler<Void> task);

    void schedule(Long delayMillis, Handler<Long> task);

    void schedulePeriodically(Long delayMillis, Long loopRate, Handler<Long> task);
}
