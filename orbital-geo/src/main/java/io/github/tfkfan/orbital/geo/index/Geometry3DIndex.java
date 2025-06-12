package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector3D;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.Directory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;

import java.util.Set;

/**
 * @author Baltser Artem tfkfan
 */
public class Geometry3DIndex<G extends Geometry> extends GeometryIndex<G, Vector3D> {
    public Geometry3DIndex(SpatialContextFactory factory, int maxLevels) {
        super(factory, maxLevels);
    }

    public Geometry3DIndex(SpatialContext ctx, int maxLevels) {
        super(ctx, maxLevels);
    }

    public Geometry3DIndex(SpatialContext ctx, Directory directory, int maxLevels) {
        super(ctx, directory, maxLevels);
    }

    public Geometry3DIndex(Directory directory, SpatialPrefixTree grid, String geoIndexedField) {
        super(directory, grid, geoIndexedField);
    }

    public Geometry3DIndex(SpatialContext ctx, Directory directory, int maxLevels, String geoIndexedField) {
        super(ctx, directory, maxLevels, geoIndexedField);
    }

    public Geometry3DIndex(SpatialStrategy strategy, Directory directory) {
        super(strategy, directory);
    }

    @Override
    public Set<G> neighbors(Vector3D point, double radius) {
        return search(new SpatialArgs(SpatialOperation.Intersects, circle3D(point, radius)), Integer.MAX_VALUE);
    }

    @Override
    public Set<G> neighbors(Vector3D stripePointA, Vector3D stripePointB, double radius) {
        return search(new SpatialArgs(SpatialOperation.Intersects, stripe3D(stripePointA, stripePointB, radius)), Integer.MAX_VALUE);
    }
}
