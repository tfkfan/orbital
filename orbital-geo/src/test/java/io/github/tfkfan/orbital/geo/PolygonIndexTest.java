package io.github.tfkfan.orbital.geo;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.geo.index.Polygon2DIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.locationtech.spatial4j.shape.jts.JtsGeometry;
import org.locationtech.spatial4j.shape.jts.JtsShapeFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

public class PolygonIndexTest {
    private Polygon2DIndex index;
    private JtsSpatialContext ctx;
    private JtsShapeFactory factory;

    @BeforeEach
    public void setUp() {
        JtsSpatialContextFactory factory = new JtsSpatialContextFactory();
        factory.geo = false;
        factory.worldBounds = new RectangleImpl(-1000, 1000, -1000, 1000, null);
        ctx = new JtsSpatialContext(factory);
        this.factory = ctx.getShapeFactory();
        index = new Polygon2DIndex(ctx, 11);
    }

    private JtsGeometry polygon(Vector2D... coordinates) {
        ShapeFactory.PolygonBuilder builder = factory.polygon();
        for (Vector2D coordinate : coordinates)
            builder = builder.pointXY(coordinate.getX(), coordinate.getY());
        return (JtsGeometry) builder.pointXY(coordinates[0].getX(), coordinates[0].getY())
                .build();
    }

    /*
      |X X X|* * *|* * *|
      |X X X|* * *|* * *|
      |X X X|* * *|* * *|

      |* * *|* * *|* * *|
      |* * *|* * *|* * *|
      |* * *|* * *|* * *|

      |* * *|* * *|* * *|
      |* * *|* * *|* * *|
      |* * *|* * *|* * *|

   */

    @Test
    public void test1() {
        index.index(Collections.singletonList(polygon(new Vector2D(0, 0),
                new Vector2D(49.9, 0),
                new Vector2D(49.9, 49.9),
                new Vector2D(0, 49.9),
                new Vector2D(0, 0))));

        assertEquals(1, index.neighbors(new Vector2D(), 50.0).size());
        assertEquals(1, index.neighbors(new Vector2D(), 49.9).size());
        assertEquals(0, index.neighbors(new Vector2D(150.0, 150.0), 50.0).size());
    }

    @Test
    public void test2() {
        index.index(List.of(polygon(new Vector2D(0, 0),
                        new Vector2D(49.9, 0),
                        new Vector2D(49.9, 49.9),
                        new Vector2D(0, 49.9),
                        new Vector2D(0, 0)),
                polygon(new Vector2D(70, 70),
                        new Vector2D(140, 140),
                        new Vector2D(70, 210),
                        new Vector2D(70, 70))
        ));

        assertEquals(1, index.neighbors(new Vector2D(), 50.0).size());
        assertEquals(1, index.neighbors(new Vector2D(70,70), 10).size());
        assertEquals(2, index.neighbors(new Vector2D(), 210.0).size());
    }
}
