package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector;
import io.github.tfkfan.orbital.core.model.BaseGameEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.store.Directory;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Baltser Artem tfkfan
 */
@Slf4j
public abstract class GameEntityIndex<V extends Vector<V>, E extends BaseGameEntity<?, V>> extends AbstractIndex<E, E, V> {
    public GameEntityIndex(SpatialContextFactory factory, int maxLevels) {
        super(factory, maxLevels);
    }

    public GameEntityIndex(SpatialContext ctx, int maxLevels) {
        super(ctx, maxLevels);
    }

    public GameEntityIndex(SpatialContext ctx, Directory directory, int maxLevels) {
        super(ctx, directory, maxLevels);
    }

    public GameEntityIndex(Directory directory, SpatialPrefixTree grid, String geoIndexedField) {
        super(directory, grid, geoIndexedField);
    }

    public GameEntityIndex(SpatialContext ctx, Directory directory, int maxLevels, String geoIndexedField) {
        super(ctx, directory, maxLevels, geoIndexedField);
    }

    public GameEntityIndex(SpatialStrategy strategy, Directory directory) {
        super(strategy, directory);
    }

    @Override
    public void delete(E o) {
        wrapCall(false, w -> deleteInternal(w, o));
    }

    @Override
    void deleteInternal(IndexWriter luceneIndexWriter, E e) throws IOException {
        luceneIndexWriter.deleteDocuments(new Term(ID, String.valueOf(e.getId().hashCode())));
    }
}
