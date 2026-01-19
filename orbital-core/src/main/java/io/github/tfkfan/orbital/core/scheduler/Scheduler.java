package io.github.tfkfan.orbital.core.scheduler;

import io.vertx.core.Handler;

import java.util.concurrent.Callable;

public interface Scheduler {
    void schedule(Long delayMillis, Handler<Long> task);

    void schedulePeriodically(Long initDelay, Long loopRate, Callable<Long> task);
}
