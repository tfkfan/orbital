package io.github.tfkfan.orbital.core.configuration.props;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ServerConfig {
    private int port;
    private Integer roomVerticleInstances;
}
