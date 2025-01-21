package com.tfkfan.webgame.shared;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class VertxUtils {
    public static Future<JsonObject> loadConfig(Vertx vertx) {
        final ConfigStoreOptions store = new ConfigStoreOptions().setType("file").setFormat("yaml").setConfig(new JsonObject().put("path", "application.yaml"));
        ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(store);
        return ConfigRetriever.create(vertx, options).getConfig();
    }
}
