package io.github.tfkfan.orbital.geo;

import io.github.tfkfan.orbital.core.math.Vector2D;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

public abstract class BaseGameEntityIndexTest {
    protected JtsSpatialContext ctx;
    protected Long seq = 1L;

    public JtsSpatialContext setUpJts() {
        JtsSpatialContextFactory factory = new JtsSpatialContextFactory();
        factory.geo = false;
        factory.worldBounds = new RectangleImpl(-1000, 1000, -1000, 1000, null);

        ctx = new JtsSpatialContext(factory);
        return ctx;
    }

    public TestEntity create(Vector2D position) {
        return new TestEntity(seq++, position);
    }
}
