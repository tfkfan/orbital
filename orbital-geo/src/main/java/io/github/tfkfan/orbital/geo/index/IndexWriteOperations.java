package io.github.tfkfan.orbital.geo.index;

import java.util.Collection;

public interface IndexWriteOperations<O> {
    void delete(O o);

    void clear();

    void index(O entity);

    void index(Collection<O> entities);
}
