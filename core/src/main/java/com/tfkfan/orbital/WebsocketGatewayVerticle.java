package com.tfkfan.orbital;


import com.tfkfan.orbital.manager.MatchmakerManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebsocketGatewayVerticle extends GatewayVerticle {
    public WebsocketGatewayVerticle( int port, MatchmakerManager matchmakerManager) {
        super(port, matchmakerManager);
    }
}
