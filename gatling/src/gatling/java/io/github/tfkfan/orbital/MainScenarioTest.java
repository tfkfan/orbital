package io.github.tfkfan.orbital;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.message.MessageBuilder;
import lombok.extern.slf4j.Slf4j;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.*;

@Slf4j
public class MainScenarioTest extends Simulation {
    static String MAIN_SCENARIO = "MainGameScenario";
    final HttpProtocolBuilder httpProtocol =
            http.wsBaseUrl("ws://localhost:8085")
                    .wsUnmatchedInboundMessageBufferSize(100);

    final ScenarioBuilder scenario = scenario(MAIN_SCENARIO).exec(
            ws("Connect")
                    .connect("/game")
                    .onConnected(exec(
                            ws("Join")
                                    .sendText(MessageBuilder.joinMessage(RoomType.TRAINING).toString())
                                    .await(2)
                                    .on(ws.checkTextMessage("Join:check")
                                                    .check(jsonPath("$.type").is(Integer.toString(MessageTypes.GAME_ROOM_JOIN_SUCCESS))),
                                            ws.checkTextMessage("Start:check")
                                                    .check(jsonPath("$.type").is(Integer.toString(MessageTypes.GAME_ROOM_START))))
                                    .await(6)
                                    .on(ws.checkTextMessage("BattleStart:check")
                                            .check(jsonPath("$.type").is(Integer.toString(MessageTypes.GAME_ROOM_BATTLE_START)))
                                    )
                    ))
    );

    {
        setUp(scenario.injectOpen(constantUsersPerSec(2).during(20)))
                .protocols(httpProtocol);
    }
}
