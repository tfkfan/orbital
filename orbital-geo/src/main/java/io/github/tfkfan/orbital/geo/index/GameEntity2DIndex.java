package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.model.BaseGameEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.Directory;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * @author Baltser Artem tfkfan
 */
@Slf4j
public class GameEntity2DIndex<E extends BaseGameEntity<?, Vector2D>> extends GameEntityIndex<Vector2D, E> {
    public GameEntity2DIndex(SpatialContextFactory factory, int maxLevels) {
        super(factory, maxLevels);
    }

    public GameEntity2DIndex(SpatialContext ctx, int maxLevels) {
        super(ctx, maxLevels);
    }

    public GameEntity2DIndex(SpatialContext ctx, Directory directory, int maxLevels) {
        super(ctx, directory, maxLevels);
    }

    public GameEntity2DIndex(Directory directory, SpatialPrefixTree grid, String geoIndexedField) {
        super(directory, grid, geoIndexedField);
    }

    public GameEntity2DIndex(SpatialContext ctx, Directory directory, int maxLevels, String geoIndexedField) {
        super(ctx, directory, maxLevels, geoIndexedField);
    }

    public GameEntity2DIndex(SpatialStrategy strategy, Directory directory) {
        super(strategy, directory);
    }

    @Override
    void indexInternal(IndexWriter indexWriter, E entity) throws IOException {
        put(indexWriter, entity.getId().hashCode(), entity,
                factory.pointXY(entity.getPosition().getX(), entity.getPosition().getY()));
    }

    @Override
    public void index(E entity) {
        wrapCall(false, (indexWriter) -> indexInternal(indexWriter, entity));
    }

    @Override
    public void index(Collection<E> entities) {
        if (entities.isEmpty()) return;

        wrapCall(true, (indexWriter) -> indexInternal(indexWriter, entities));
    }

    @Override
    public Set<E> neighborsInternal(IndexSearcher searcher, Vector2D point, double radius) throws IOException {
        return searchInternal(searcher, new SpatialArgs(SpatialOperation.Intersects, circle2D(point, radius)), Integer.MAX_VALUE);
    }

    @Override
    public Set<E> neighborsInternal(IndexSearcher searcher, Vector2D stripePointA, Vector2D stripePointB, double radius) throws IOException {
        return searchInternal(searcher, new SpatialArgs(SpatialOperation.Intersects, stripe2D(stripePointA, stripePointB, radius)), Integer.MAX_VALUE);
    }
}
