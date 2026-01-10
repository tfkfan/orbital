package io.github.tfkfan.orbital;

import io.gatling.http.action.ws.WsInboundMessage;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.message.MessageBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.*;

@Slf4j
public class MainScenarioTest extends Simulation {
    final HttpProtocolBuilder httpProtocol =
            http.wsBaseUrl("ws://localhost:8085")
                    .wsUnmatchedInboundMessageBufferSize(100);

    final ScenarioBuilder scenario = scenario("Game")
            .exec(
                    ws("Connect")
                            .connect("/game"))
            .pace(5)
            .exec(
                    ws("Join")
                            .sendText(MessageBuilder.joinMessage(RoomType.TRAINING))
                            .await(10)
                            .on(ws.checkTextMessage("Join:check").check(bodyString().is(MessageBuilder.joinWaitMessage())))
            );

    {
        setUp(scenario.injectOpen(rampUsers(10).during(60))).protocols(httpProtocol);
    }
}
