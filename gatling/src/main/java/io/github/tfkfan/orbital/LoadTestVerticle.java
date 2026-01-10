package io.github.tfkfan.orbital;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.tfkfan.orbital.core.configuration.MessageTypes;
import io.github.tfkfan.orbital.core.network.message.Message;
import io.github.tfkfan.orbital.core.room.RoomType;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoadTestVerticle extends AbstractVerticle {
    int clients = 100;

    int connections = clients;
    int failedConnections = 0;

    WebSocketClient webSocketClient;

    Map<String, ClientMetrics> clientMetrics = new HashMap<>();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        WebSocketClientOptions options = new WebSocketClientOptions();

        webSocketClient = vertx.createWebSocketClient(options);

        log.info("Start load test");
        for (int i = 0; i < clients; i++) {
            final int clientId = i;
            vertx.setTimer(i + 1, timerId -> {
                String playerId = "player_" + clientId;
                ClientMetrics metrics = new ClientMetrics();
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
                                    switch (message.getType()) {
                                        case MessageTypes.GAME_ROOM_JOIN_SUCCESS -> {
                                            metrics.setLastJoinSuccessTime(System.currentTimeMillis());
                                        }
                                        case MessageTypes.GAME_ROOM_BATTLE_START -> {
                                            metrics.setLastBattleStartTime(System.currentTimeMillis());
                                        }
                                        case MessageTypes.UPDATE -> {
                                            if (metrics.getLastUpdateTime() != 0L) {
                                                long delay = System.currentTimeMillis() - metrics.getLastUpdateTime();
                                                metrics.setLastUpdateDelay(delay);
                                                metrics.setSumUpdateDelay(metrics.getLastUpdateDelay() + delay);
                                                if (metrics.getMaxUpdateDelay() < delay)
                                                    metrics.setMaxUpdateDelay(delay);
                                            }
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
            });
        }

        // Завершение теста
        vertx.setTimer(10000, timerId -> {
            printStats();
            log.info("End load test");
            vertx.close();
        });

        startPromise.complete();
    }

    private void printStats() {
        log.info("---");
        log.info("Client\t\tTotal update ticks\tAvg update delay\tMax update delay");
        for (Map.Entry<String, ClientMetrics> entry : clientMetrics.entrySet()) {
            log.info("{}\t{}\t\t\t\t\t{}\t\t\t\t\t{}", entry.getKey(),
                    entry.getValue().getUpdateTicks(),
                    entry.getValue().getAverageUpdateDelay(),
                    entry.getValue().getMaxUpdateDelay());
        }
    }

    private Future<WebSocket> connect(String playerId) {
        WebSocketConnectOptions options = new WebSocketConnectOptions()
                .setURI("/game")
                .setHost("localhost")
                .setPort(8085);

        return webSocketClient.connect(options);
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new LoadTestVerticle());
    }
}
