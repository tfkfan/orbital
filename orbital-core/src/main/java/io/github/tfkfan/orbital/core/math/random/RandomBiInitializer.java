package io.github.tfkfan.orbital.core.math.random;

import io.github.tfkfan.orbital.core.math.Vector2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class RandomBiInitializer<T> {
    private final Collection<T> objects;
    private final List<Vector2D> positions;
    private Vector2D lastPosition;

    public RandomBiInitializer(Collection<T> objects, List<Vector2D> positions) {
        this.objects = objects;
        this.positions = new ArrayList<>(positions);
    }

    public void initialize(BiConsumer<T, Vector2D> vectorConsumer) {
        objects.forEach ( obj ->{
            int idx = Random.getRandomIndex(positions.size());
            var position = positions.isEmpty() ? lastPosition : new  Vector2D(positions.remove(idx));
            lastPosition = position;
            vectorConsumer.accept(obj, position);
        });
    }
}