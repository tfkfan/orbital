package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector;
import org.apache.lucene.spatial.query.SpatialArgs;

import java.util.Collection;
import java.util.Set;

/**
 * @author Baltser Artem tfkfan
 */
public interface Index<O, T, V extends Vector<V>> extends IndexWriteOperations<O>, IndexReadOperations<T, V> {
    IndexWriter<O> writer();

    IndexReader<T, V> reader();
}
