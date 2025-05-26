package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector;
import org.apache.lucene.spatial.query.SpatialArgs;

import java.util.Set;

public interface IndexReadOperations<T, V extends Vector<V>> {
    Set<T> neighbors(V point, double radius);

    Set<T> neighbors(V stripePointA, V stripePointB, double radius);

    Set<T> search(SpatialArgs args);

    Set<T> search(SpatialArgs args, int n);
}
