package io.github.tfkfan.resources;

import io.github.tfkfan.orbital.core.ResourcesLoader;
import io.github.tfkfan.resources.model.World;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
public class GeometryResources implements ResourcesLoader<GeometryResources> {
    protected SpatialContextFactory spatialContextFactory;
    protected SpatialContext spatialContext;
    protected ShapeFactory shapeFactory;

    protected final World world;

    public GeometryResources() {
        this(null);
    }

    public GeometryResources(World world) {
        this.world = world;
    }

    @Override
    public GeometryResources load() {
        spatialContextFactory = new JtsSpatialContextFactory();
        spatialContextFactory.geo = false;

        if (world != null)
            spatialContextFactory.worldBounds = new RectangleImpl(-world.getWidth().doubleValue(), world.getWidth().doubleValue(),
                    -world.getHeight().doubleValue(), world.getHeight().doubleValue(), null);

        spatialContext = new JtsSpatialContext((JtsSpatialContextFactory) spatialContextFactory);
        shapeFactory = spatialContext.getShapeFactory();
        return this;
    }

    public SpatialContextFactory getSpatialContextFactory() {
        return spatialContextFactory;
    }

    public SpatialContext getSpatialContext() {
        return spatialContext;
    }

    public ShapeFactory getShapeFactory() {
        return shapeFactory;
    }

    public World getWorld() {
        return world;
    }
}
