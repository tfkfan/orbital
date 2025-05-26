package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector3D;
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
public class GameEntity3DIndex<E extends BaseGameEntity<?, Vector3D>> extends GameEntityIndex<Vector3D, E> {
    public GameEntity3DIndex(SpatialContextFactory factory, int maxLevels) {
        super(factory, maxLevels);
    }

    public GameEntity3DIndex(SpatialContext ctx, int maxLevels) {
        super(ctx, maxLevels);
    }

    public GameEntity3DIndex(SpatialContext ctx, Directory directory, int maxLevels) {
        super(ctx, directory, maxLevels);
    }

    public GameEntity3DIndex(Directory directory, SpatialPrefixTree grid, String geoIndexedField) {
        super(directory, grid, geoIndexedField);
    }

    public GameEntity3DIndex(SpatialContext ctx, Directory directory, int maxLevels, String geoIndexedField) {
        super(ctx, directory, maxLevels, geoIndexedField);
    }

    public GameEntity3DIndex(SpatialStrategy strategy, Directory directory) {
        super(strategy, directory);
    }

    @Override
    void indexInternal(IndexWriter luceneIndexWriter, E entity) throws IOException {
        put(luceneIndexWriter, entity.getId().hashCode(), entity,
                factory.pointXY(entity.getPosition().getX(), entity.getPosition().getY()));
    }

    @Override
    public void index(E entity) {
        wrapCall(false, (indexWriter) -> indexInternal(indexWriter, entity));
    }

    @Override
    public void index(Collection<E> entities) {
        if (entities.isEmpty()) return;

        wrapCall(true, (indexWriter) -> {
            for (E entity : entities)
                put(indexWriter, entity.getId().hashCode(), entity,
                        factory.pointXY(entity.getPosition().getX(), entity.getPosition().getY()));
        });
    }

    @Override
    Set<E> neighborsInternal(IndexSearcher indexSearcher, Vector3D point, double radius) throws IOException {
        return searchInternal(indexSearcher, new SpatialArgs(SpatialOperation.Intersects, circle3D(point, radius)), Integer.MAX_VALUE);
    }

    @Override
    Set<E> neighborsInternal(IndexSearcher indexSearcher, Vector3D stripePointA, Vector3D stripePointB, double radius) throws IOException {
        return searchInternal(indexSearcher, new SpatialArgs(SpatialOperation.Intersects, stripe3D(stripePointA, stripePointB, radius)), Integer.MAX_VALUE);
    }
}
