package io.github.tfkfan.orbital;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.github.tfkfan.orbital.data.MessageWrapper;
import io.vertx.core.*;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketClient;
import io.vertx.core.http.WebSocketClientOptions;
import io.vertx.core.http.WebSocketConnectOptions;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.extern.slf4j.Slf4j;
import org.icepear.echarts.Bar;
import org.icepear.echarts.Line;
import org.icepear.echarts.render.Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class LoadTestVerticle extends AbstractVerticle {
    int clients = 1;
    long maxTimeMs = 20000;

    int connections = clients;
    int failedConnections = 0;

    WebSocketClient webSocketClient;

    TestMetrics testMetrics = new TestMetrics();

    Map<String, ClientMetrics> clientMetrics = new HashMap<>();
    List<MessageWrapper> msgs = new ArrayList<>();
    List<MessageWrapper> wrongMsgs = new ArrayList<>();

    @Override
    public void start(Promise<Void> startPromise) {

        WebSocketClientOptions options = new WebSocketClientOptions();
        options.setMaxConnections(1000);
        webSocketClient = vertx.createWebSocketClient(options);

        log.info("Start load test");
        testMetrics.setStartTimeMs(System.currentTimeMillis());
        testMetrics.setPlayersCountAtStart(clients);

        for (int i = 0; i < clients; i++) {
            final String playerId = "player_" + i;
            final ClientMetrics metrics = new ClientMetrics();
            clientMetrics.put(playerId, metrics);
            connect(playerId)
                    .onFailure(err -> {
                        failedConnections++;
                        log.error("{}: Connection failed: {}", playerId, err.getMessage());
                    })
                    .onSuccess(webSocket -> {
                        log.info("{}: Connected successfully", playerId);
                        webSocket.textMessageHandler(text -> {
                            try {
                                Message message = DatabindCodec.mapper().readValue(text, Message.class);
                                long ts = message.getTimestamp();
                                long ms = System.currentTimeMillis();
                                switch (message.getType()) {
                                    case MessageTypes.GAME_ROOM_JOIN_SUCCESS -> metrics.setLastJoinSuccessTime(ms);
                                    case MessageTypes.GAME_ROOM_BATTLE_START -> metrics.setLastBattleStartTime(ms);
                                    case MessageTypes.UPDATE -> {
                                        msgs.add(new MessageWrapper(message, System.currentTimeMillis()));
                                        if (metrics.getLastBattleStartTime() == 0L)
                                            return;
                                        if (ts < metrics.getLastMessageTimestamp()) {
                                            log.warn("ts is wrong");
                                            return;
                                        }
                                        if (metrics.getLastUpdateTime() != 0L) {
                                            long responseMs = System.currentTimeMillis() - metrics.getLastUpdateTime();
                                            if (responseMs == 0) {
                                                log.warn("resp is 0");
                                                wrongMsgs.add(new MessageWrapper(message, System.currentTimeMillis()));
                                            }
                                            metrics.setLastUpdateDelay(responseMs);
                                            testMetrics.getUpdateDelayTimeSeries().add(responseMs);
                                            metrics.setSumUpdateDelay(metrics.getLastUpdateDelay() + responseMs);
                                            if (metrics.getMaxUpdateDelay() < responseMs)
                                                metrics.setMaxUpdateDelay(responseMs);
                                        }
                                        metrics.setLastMessageTimestamp(ts);
                                        metrics.setLastUpdateTime(System.currentTimeMillis());
                                        metrics.setUpdateTicks(metrics.getUpdateTicks() + 1);
                                        metrics.setAverageUpdateDelay(metrics.getSumUpdateDelay() / metrics.getUpdateTicks());
                                    }
                                }
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        webSocket.writeTextMessage(MessageBuilder.joinMessage(RoomType.TRAINING).toString());
                        metrics.setLastJoinRequestTime(System.currentTimeMillis());
                    });
        }

        // Завершение теста
        vertx.setTimer(maxTimeMs, timerId -> {
            testMetrics.setEndTimeMs(System.currentTimeMillis());
            testMetrics.setPlayersCountAtEnd(clients - failedConnections);
            calcAndPrintStats();
            log.info("End load test");
            vertx.close();
        });
    }

    private void calcAndPrintStats() {
        Engine engine = new Engine();

        Line line = new Line()
                .setTitle("Total players")
                .addXAxis(new String[]{"0 ms", testMetrics.endAbsoluteMs().toString().concat(" ms")})
                .addYAxis()
                .addSeries(new Number[]{testMetrics.getPlayersCountAtStart(), testMetrics.getPlayersCountAtEnd()});

        engine.render("./gatling/players.html", line);


        log.info("Updates accepted {}", msgs.size());
        msgs.sort((o1, o2) -> Long.compare(o1.clientTimestamp(), o2.clientTimestamp()));
        Map<Long, Long> updateDelaySeries = testMetrics.getUpdateDelayTimeSeries()
                .stream()
                .collect(Collectors.groupingBy(it -> it, Collectors.counting()));

        String[] xAxis = new String[updateDelaySeries.size()];
        Object[] series = new Object[updateDelaySeries.size()];

        AtomicInteger i = new AtomicInteger();
        updateDelaySeries.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            var msXAxis = entry.getKey().toString().concat(" ms");
            var countYAxis = entry.getValue();
            if (countYAxis > 1000)
                countYAxis = 1000L;
            xAxis[i.get()] = msXAxis;
            series[i.get()] = countYAxis;
            i.incrementAndGet();
        });
        Bar bar = new Bar()
                .setTitle("Update tick delay distribution")
                .addXAxis(xAxis)
                .addYAxis()
                .addSeries(series);

        engine.render("./gatling/update-tick-delay.html", bar);
    }

    private Future<WebSocket> connect(String playerId) {
        WebSocketConnectOptions options = new WebSocketConnectOptions()
                .setURI("/game")
                .setHost("10.20.0.60")
                .setPort(8085);

        return webSocketClient.connect(options);
    }

    public static void main(String[] args) {
        Vertx.clusteredVertx(new VertxOptions())
                .flatMap(vertx -> vertx.deployVerticle(LoadTestVerticle.class, new DeploymentOptions()
                        .setInstances(1)
                        .setThreadingModel(ThreadingModel.VIRTUAL_THREAD)))
                .onFailure(th -> log.error("Failed to deploy verticle", th));
    }
}
