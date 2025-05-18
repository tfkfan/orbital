package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector2D;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.QuadPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.jts.JtsGeometry;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Baltser Artem tfkfan
 */
@Slf4j
public class GeometryIndex<G extends Geometry> {
    public static final String ID = "id";
    public static final String DEFAULT_INDEXED_GEO_FIELD = "poly";

    final SpatialContext ctx;
    final ShapeFactory factory;

    final SpatialPrefixTree grid;
    final SpatialStrategy strategy;

    final Directory directory;

    final Map<Long, G> shapeMap = new HashMap<>();

    IndexReader indexReader;

    public GeometryIndex(SpatialContext ctx, int maxLevels) {
        this(ctx, new ByteBuffersDirectory(), maxLevels, DEFAULT_INDEXED_GEO_FIELD);
    }

    public GeometryIndex(SpatialContext ctx, Directory directory, int maxLevels) {
        this(ctx, directory, maxLevels, DEFAULT_INDEXED_GEO_FIELD);
    }

    public GeometryIndex(SpatialContext ctx, Directory directory, int maxLevels, String geoIndexedField) {
        this.ctx = ctx;
        factory = ctx.getShapeFactory();
        grid = new QuadPrefixTree(ctx, maxLevels);
        strategy = new RecursivePrefixTreeStrategy(grid, geoIndexedField);
        this.directory = directory;
    }

    @SuppressWarnings("unchecked")
    public void index(Collection<JtsGeometry> shapes) {
        if (shapes.isEmpty()) return;

        try (final IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig())) {
            indexWriter.deleteAll();
            long sequence = 1L;
            for (JtsGeometry shape : shapes) {
                final Document doc = new Document();
                final var id = sequence++;
                shapeMap.put(id, (G) shape.getGeom());

                final Field[] fields = strategy.createIndexableFields(shape);
                for (Field field : fields) doc.add(field);

                doc.add(new StoredField(ID, id));

                indexWriter.addDocument(doc);
                indexWriter.forceMerge(1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<G> neighbors(Vector2D point, double radius) {
        return search(new SpatialArgs(SpatialOperation.Intersects, factory.circle(
                factory.pointXY(point.getX(), point.getY()), radius)));
    }

    public Set<G> search(SpatialArgs args) {
        try {
            if (indexReader == null)
                indexReader = DirectoryReader.open(directory);
            final IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            return Stream.of(indexSearcher.search(strategy.makeQuery(args), 10).scoreDocs).map(it -> {
                try {
                    return shapeMap.get(indexSearcher.storedFields().document(it.doc)
                            .getField(ID).numericValue().longValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toSet());
        } catch (Exception e) {
            indexReader = null;
            throw new RuntimeException(e);
        }
    }

    public void tryClose() {
        try {
            if (indexReader != null)
                indexReader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
