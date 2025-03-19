package com.tfkfan.orbital.scheduler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.ArrayList;
import java.util.List;

public class RoomScheduler implements Scheduler {
    private final List<Long> futures = new ArrayList<>();
    private final Vertx vertx;

    public RoomScheduler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void schedule(Long delayMillis, Handler<Long> task) {
        if (delayMillis <= 1) {
            task.handle(0L);
            return;
        }

        futures.add(vertx.setTimer(delayMillis, (t) -> {
            task.handle(t);
            futures.remove(t);
        }));
    }

    @Override
    public void schedulePeriodically(Long initDelay, Long loopRate, Handler<Long> task) {
        futures.add(vertx.setPeriodic(initDelay, loopRate, task));
    }

    public void eraseTasks() {
        futures.forEach(vertx::cancelTimer);
        futures.clear();
    }
}
