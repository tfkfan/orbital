package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector;
import org.apache.lucene.spatial.query.SpatialArgs;

import java.util.Collection;
import java.util.Set;

/**
 * @author Baltser Artem tfkfan
 */
public interface Index<O, T, V extends Vector<V>> {
    void delete(O o);

    void clear();

    void index(O entity);

    void index(Collection<O> entities);

    Set<T> neighbors(V point, double radius);

    Set<T> neighbors(V stripePointA, V stripePointB, double radius);

    Set<T> search(SpatialArgs args);

    Set<T> search(SpatialArgs args, int n);
}
