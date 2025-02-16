package com.tfkfan.vertx;

import com.tfkfan.vertx.configuration.Constants;
import com.tfkfan.vertx.manager.StopListener;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public abstract class BaseVerticle extends AbstractVerticle {
    protected String verticleId;
    private StopListener stopListener;

    protected BaseVerticle stopListener(StopListener stopListener) {
        this.stopListener = stopListener;
        return this;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        verticleId = config().getString(Constants.ROOM_VERTICAL_ID);
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        super.stop(stopPromise);
        if (stopListener != null)
            stopListener.stop(stopPromise);
    }
}
