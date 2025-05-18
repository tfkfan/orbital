package io.github.tfkfan.resources;

import io.github.tfkfan.orbital.core.ResourcesLoader;
import lombok.Getter;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.ShapeFactory;

@Getter
public class GeometryResources implements ResourcesLoader<GeometryResources> {
    protected SpatialContext ctx;
    protected ShapeFactory shapeFactory;

    @Override
    public GeometryResources load() {
        ctx = SpatialContext.GEO;
        shapeFactory = ctx.getShapeFactory();
        return this;
    }
}
