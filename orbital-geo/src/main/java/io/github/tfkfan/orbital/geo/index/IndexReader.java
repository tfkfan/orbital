package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector;

public interface IndexReader<T, V extends Vector<V>> extends AutoCloseable, IndexReadOperations<T, V> {
    <R extends IndexReader<T, V>> R open();
}
