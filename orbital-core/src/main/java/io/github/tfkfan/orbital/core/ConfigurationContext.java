package io.github.tfkfan.orbital.core;

import io.github.tfkfan.orbital.core.configuration.props.OrbitalConfig;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Value
@Builder(toBuilder = true)
@With
public class ConfigurationContext {
    JsonObject rawConfig;
    OrbitalConfig config;

    Map<String, Object> extensions = new HashMap<>();

    public ConfigurationContext withExtension(String property, Object extension) {
        extensions.put(property, extension);
        return this;
    }

    public <T> Optional<T> getExtension(String property) {
        return Optional.ofNullable(extensions.get(property)).map(o -> (T) o);
    }
}