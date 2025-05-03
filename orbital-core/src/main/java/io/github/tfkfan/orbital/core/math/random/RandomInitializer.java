package io.github.tfkfan.orbital.core.math.random;

import io.github.tfkfan.orbital.core.math.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RandomInitializer {
    private final List<Vector2D> positions;

    public RandomInitializer(List<Vector2D> positions) {
        this.positions = new ArrayList<>(positions);
    }

    public void  initialize(int maxCount, Consumer<Vector2D> vectorConsumer) {
        for (int i = 0; i < maxCount; i++) {
            if (positions.isEmpty()) break;
            var position = new Vector2D(positions.get(Random.getRandomIndex(positions.size())));
            positions.remove(position);
            vectorConsumer.accept(position);
        }
    }
}