package com.tfkfan.vertx.verticle;

import com.tfkfan.vertx.WebsocketGatewayVerticle;
import com.tfkfan.vertx.RoomVerticle;
import com.tfkfan.vertx.manager.DefaultMatchmakerManager;
import com.tfkfan.vertx.manager.MatchmakerManager;
import com.tfkfan.vertx.properties.ApplicationProperties;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class DefaultGatewayVerticle extends WebsocketGatewayVerticle {
    @Override
    protected RoomVerticle createRoomVerticle() {
        return new DefaultRoomVerticle();
    }

    @Override
    protected void initRouter(Router router) {
        router.route().handler(StaticHandler.create("static"));
    }

    @Override
    protected MatchmakerManager createMatchmakerManager(ApplicationProperties applicationProperties) {
        return new DefaultMatchmakerManager(applicationProperties.getRoom());
    }
}
