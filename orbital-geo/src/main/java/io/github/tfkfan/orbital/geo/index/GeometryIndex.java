package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.store.Directory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.jts.JtsGeometry;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Baltser Artem tfkfan
 */
@Slf4j
public abstract class GeometryIndex<G extends Geometry, V extends Vector<V>> extends
        AbstractIndex<JtsGeometry, G, V> {
    public GeometryIndex(SpatialContextFactory factory, int maxLevels) {
        super(factory, maxLevels);
    }

    public GeometryIndex(SpatialContext ctx, int maxLevels) {
        super(ctx, maxLevels);
    }

    public GeometryIndex(SpatialContext ctx, Directory directory, int maxLevels) {
        super(ctx, directory, maxLevels);
    }

    public GeometryIndex(Directory directory, SpatialPrefixTree grid, String geoIndexedField) {
        super(directory, grid, geoIndexedField);
    }

    public GeometryIndex(SpatialContext ctx, Directory directory, int maxLevels, String geoIndexedField) {
        super(ctx, directory, maxLevels, geoIndexedField);
    }

    public GeometryIndex(SpatialStrategy strategy, Directory directory) {
        super(strategy, directory);
    }

    @SuppressWarnings("unchecked")
    @Override
    void indexInternal(IndexWriter luceneIndexWriter, JtsGeometry entity) throws IOException {
        put(luceneIndexWriter, entity.hashCode(), (G) entity.getGeom(), entity);
    }

    @Override
    void deleteInternal(IndexWriter luceneIndexWriter, JtsGeometry o) throws IOException {
        luceneIndexWriter.deleteDocuments(new Term(ID, String.valueOf(o.hashCode())));
    }


    @Override
    public void index(JtsGeometry shape) {
        wrapCall(false, (indexWriter) -> indexInternal(indexWriter, shape));
    }

    @Override
    public void index(Collection<JtsGeometry> shapes) {
        if (shapes.isEmpty()) return;
        wrapCall(true, (indexWriter) -> indexInternal(indexWriter, shapes));
    }

    @Override
    public void delete(JtsGeometry o) {
        wrapCall(false, w -> deleteInternal(w, o));
    }
}
