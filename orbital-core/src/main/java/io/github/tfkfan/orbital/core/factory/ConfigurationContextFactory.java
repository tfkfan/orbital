package io.github.tfkfan.orbital.core.factory;

import io.github.tfkfan.orbital.core.ConfigurationContext;
import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.function.Consumer;

@FunctionalInterface
public interface ConfigurationContextFactory {
    String DEFAULT_CONFIG_PATH = "application.yaml";

    Future<ConfigurationContext> load();

    static ConfigurationContextFactory localConfig() {
        return () -> loadLocalConfig()
                .map(it -> ConfigurationContext
                        .builder()
                        .rawConfig(it)
                        .config(it.mapTo(OrbitalConfig.class))
                        .build());
    }

    static ConfigurationContextFactory localConfig(String fullPath, Consumer<ConfigurationContext> contextCustomizer) {
        return () -> (fullPath != null ? loadLocalConfig(fullPath) : loadLocalConfig())
                .map(it -> {
                    final ConfigurationContext ctx = ConfigurationContext
                            .builder()
                            .rawConfig(it)
                            .config(it.mapTo(OrbitalConfig.class))
                            .build();
                    if (contextCustomizer != null)
                        contextCustomizer.accept(ctx);
                    return ctx;
                });
    }

    static Future<JsonObject> loadLocalConfig(final String fullPath) {
        return loadConfig(fullPath);
    }

    static Future<JsonObject> loadLocalConfig() {
        return loadLocalConfig(DEFAULT_CONFIG_PATH);
    }

    static Future<JsonObject> loadConfig(Vertx vertx) {
        return loadConfig((String) null);
    }

    static Future<JsonObject> loadConfig(String path) {
        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(new ConfigStoreOptions()
                        .setType("env"));
        if (path != null)
            options = options.addStore(new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("yaml")
                    .setConfig(new JsonObject().put("path", path)));

        return ConfigRetriever.create(Vertx.currentContext().owner(), options).getConfig();
    }
}
