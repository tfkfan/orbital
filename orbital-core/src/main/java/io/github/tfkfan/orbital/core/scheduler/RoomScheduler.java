package io.github.tfkfan.orbital.core.scheduler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class RoomScheduler implements Scheduler {
    private final List<Long> futures = new ArrayList<>();
    private final Vertx vertx;
    private final ReentrantLock lock = new ReentrantLock();

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
    // This method should be thread safe. Task called sequentially with locking - game loop as example
    public void schedulePeriodically(Long initDelay, Long loopRate, Callable<Long> task) {
        final Callable<Long> scheduledTask = wrap(task);
        futures.add(vertx.setPeriodic(initDelay, loopRate, (l) -> vertx.executeBlocking(scheduledTask, true)));
    }

    private Callable<Long> wrap(Callable<Long> task) {
        return () -> {
            if (lock.tryLock()) {
                try {
                    return task.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
            return 0L;
        };
    }

    public void eraseTasks() {
        futures.forEach(vertx::cancelTimer);
        futures.clear();
    }
}
