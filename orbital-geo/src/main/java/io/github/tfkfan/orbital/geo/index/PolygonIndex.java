package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector2D;
import org.apache.lucene.store.Directory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.jts.JtsGeometry;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PolygonIndex extends GeometryIndex<Polygon> {
    public PolygonIndex(SpatialContext ctx, int maxLevels) {
        super(ctx, maxLevels);
    }

    public PolygonIndex(SpatialContext ctx, Directory directory, int maxLevels) {
        super(ctx, directory, maxLevels);
    }

    public PolygonIndex(SpatialContext ctx, Directory directory, int maxLevels, String geoIndexedField) {
        super(ctx, directory, maxLevels, geoIndexedField);
    }

    public void index(Vector2D[][] shapes) {
        index(Arrays.stream(shapes).map(it -> {
            AtomicReference<ShapeFactory.PolygonBuilder> builder = new AtomicReference<>(factory.polygon());
            Stream.of(it).forEach(vec -> builder.set(builder.get().pointXY(vec.getX(), vec.getY())));
            return (JtsGeometry) builder.get().pointXY(it[0].getX(), it[0].getY()).build();
        }).collect(Collectors.toSet()));
    }
}
