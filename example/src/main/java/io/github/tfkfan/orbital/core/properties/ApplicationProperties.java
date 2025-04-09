package io.github.tfkfan.orbital.core.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.tfkfan.orbital.core.configuration.props.RoomConfig;
import io.github.tfkfan.orbital.core.configuration.props.ServerConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationProperties {
    private ServerConfig server;
    private RoomConfig room;
}
