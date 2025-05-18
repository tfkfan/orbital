package io.github.tfkfan.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tfkfan.orbital.core.ResourcesLoader;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RequiredArgsConstructor
public class FileLoader<F extends Serializable> implements ResourcesLoader<F> {
    private final String path;
    private final ObjectMapper objectMapper;
    private final Class<F> clazz;

    @Override
    public F load() {
        final Reader reader = new InputStreamReader(Objects.requireNonNull(FileLoader.class.getResourceAsStream(path)), StandardCharsets.UTF_8);
        try {
            return objectMapper.readValue(reader, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}