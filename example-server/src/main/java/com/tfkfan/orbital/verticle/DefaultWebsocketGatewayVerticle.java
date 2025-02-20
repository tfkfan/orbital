package com.tfkfan.orbital.verticle;

import com.tfkfan.orbital.WebsocketGatewayVerticle;
import com.tfkfan.orbital.RoomVerticle;
import com.tfkfan.orbital.manager.DefaultMatchmakerManager;
import com.tfkfan.orbital.manager.MatchmakerManager;
import com.tfkfan.orbital.properties.ApplicationProperties;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class DefaultWebsocketGatewayVerticle extends WebsocketGatewayVerticle {
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
