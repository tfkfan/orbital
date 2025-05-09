package io.github.tfkfan.orbital.core.scheduler;

import io.vertx.core.Handler;

public interface Scheduler {
    void schedule(Long delayMillis, Handler<Long> task);

    void schedulePeriodically(Long initDelay, Long loopRate, Handler<Long> task);
}
