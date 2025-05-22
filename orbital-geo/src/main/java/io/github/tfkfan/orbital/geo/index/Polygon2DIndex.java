package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector2D;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.store.Directory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.jts.JtsGeometry;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Baltser Artem tfkfan
 */
public class Polygon2DIndex extends Geometry2DIndex<Polygon> {
    public Polygon2DIndex(SpatialContextFactory factory, int maxLevels) {
        super(factory, maxLevels);
    }

    public Polygon2DIndex(SpatialContext ctx, int maxLevels) {
        super(ctx, maxLevels);
    }

    public Polygon2DIndex(SpatialContext ctx, Directory directory, int maxLevels) {
        super(ctx, directory, maxLevels);
    }

    public Polygon2DIndex(Directory directory, SpatialPrefixTree grid, String geoIndexedField) {
        super(directory, grid, geoIndexedField);
    }

    public Polygon2DIndex(SpatialContext ctx, Directory directory, int maxLevels, String geoIndexedField) {
        super(ctx, directory, maxLevels, geoIndexedField);
    }

    public Polygon2DIndex(SpatialStrategy strategy, Directory directory) {
        super(strategy, directory);
    }

    public void index(Vector2D[][] shapes) {
        index(Arrays.stream(shapes).map(it -> {
            AtomicReference<ShapeFactory.PolygonBuilder> builder = new AtomicReference<>(factory.polygon());
            Stream.of(it).forEach(vec -> builder.set(builder.get().pointXY(vec.getX(), vec.getY())));
            return (JtsGeometry) builder.get().pointXY(it[0].getX(), it[0].getY()).build();
        }).collect(Collectors.toSet()));
    }
}
