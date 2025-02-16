package com.tfkfan.vertx.verticle;

import com.tfkfan.vertx.BaseGatewayVerticle;
import com.tfkfan.vertx.BaseRoomVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class GatewayVerticle extends BaseGatewayVerticle {
    @Override
    protected BaseRoomVerticle createRoomVerticle() {
        return new RoomVerticle();
    }

    @Override
    protected void initRouter(Router router) {
        router.route().handler(StaticHandler.create("static"));
    }
}
