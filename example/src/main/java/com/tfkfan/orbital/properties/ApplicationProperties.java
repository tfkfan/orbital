package com.tfkfan.orbital.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tfkfan.orbital.configuration.props.RoomConfig;
import com.tfkfan.orbital.configuration.props.ServerConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationProperties {
    private ServerConfig server;
    private RoomConfig room;
}
