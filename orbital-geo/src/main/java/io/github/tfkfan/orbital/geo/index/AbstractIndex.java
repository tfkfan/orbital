package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector;
import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.math.Vector3D;
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
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Baltser Artem tfkfan
 */
@Slf4j
public abstract class AbstractIndex<O, T, V extends Vector<V>> implements Index<O, T, V> {
    public static final String ID = "id";
    public static final String DEFAULT_INDEXED_GEO_FIELD = "_sf_";
    public static final int N = 10;

    protected final ShapeFactory factory;
    protected final SpatialStrategy strategy;
    protected final Directory directory;
    private final Map<Long, T> entitiesMap = new HashMap<>();

    public AbstractIndex(SpatialContextFactory factory, int maxLevels) {
        this(factory instanceof JtsSpatialContextFactory jtsFactory ?
                        new JtsSpatialContext(jtsFactory) : new SpatialContext(factory),
                maxLevels);
    }

    public AbstractIndex(SpatialContext ctx, int maxLevels) {
        this(ctx, new ByteBuffersDirectory(), maxLevels, DEFAULT_INDEXED_GEO_FIELD);
    }

    public AbstractIndex(SpatialContext ctx, Directory directory, int maxLevels) {
        this(ctx, directory, maxLevels, DEFAULT_INDEXED_GEO_FIELD);
    }

    public AbstractIndex(Directory directory, SpatialPrefixTree grid, String geoIndexedField) {
        this(new RecursivePrefixTreeStrategy(grid, geoIndexedField), directory);
    }

    public AbstractIndex(SpatialContext ctx, Directory directory, int maxLevels, String geoIndexedField) {
        this(new RecursivePrefixTreeStrategy(new QuadPrefixTree(ctx, maxLevels), geoIndexedField), directory);
    }

    public AbstractIndex(SpatialStrategy strategy, Directory directory) {
        factory = strategy.getSpatialContext().getShapeFactory();
        this.strategy = strategy;
        this.directory = directory;
    }

    protected void put(IndexWriter indexWriter, long id, T entity, Shape shape) throws IOException {
        if (indexWriter == null) return;

        entitiesMap.put(id, entity);

        final Document doc = new Document();
        final Field[] fields = strategy.createIndexableFields(shape);
        for (Field field : fields) doc.add(field);
        doc.add(new StoredField(ID, id));

        indexWriter.addDocument(doc);
        indexWriter.forceMerge(1);
    }

    abstract void indexInternal(org.apache.lucene.index.IndexWriter luceneIndexWriter, O entity) throws IOException;

    void indexInternal(org.apache.lucene.index.IndexWriter luceneIndexWriter, Collection<O> entities) throws IOException {
        for (O e : entities) indexInternal(luceneIndexWriter, e);
    }

    void clearInternal(org.apache.lucene.index.IndexWriter luceneIndexWriter) throws IOException {
        if (luceneIndexWriter == null) return;
        luceneIndexWriter.deleteAll();
    }

    abstract void deleteInternal(org.apache.lucene.index.IndexWriter luceneIndexWriter, O o) throws IOException;

    IndexWriter openWriter() throws IOException {
        return new IndexWriter(directory, new IndexWriterConfig());
    }

    IndexReader openReader() throws IOException {
        return DirectoryReader.open(directory);
    }

    @Override
    public io.github.tfkfan.orbital.geo.index.IndexWriter<O> writer() {
        return new IndexWriterImpl<>(this);
    }

    @Override
    public io.github.tfkfan.orbital.geo.index.IndexReader<T, V> reader() {
        return new IndexReaderImpl<>(this);
    }

    @Override
    public void clear() {
        wrapCall(true, null);
    }

    @Override
    public Set<T> search(SpatialArgs args) {
        return search(args, N);
    }

    @Override
    public Set<T> search(SpatialArgs args, int n) {
        try (var indexReader = DirectoryReader.open(directory)) {
            return searchInternal(new IndexSearcher(indexReader), args, n);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Set<T> searchInternal(IndexSearcher indexSearcher, SpatialArgs args) throws IOException {
        return searchInternal(indexSearcher, args, N);
    }

    Set<T> searchInternal(IndexSearcher indexSearcher, SpatialArgs args, int n) throws IOException {
        return Stream.of(indexSearcher.search(strategy.makeQuery(args), n).scoreDocs).map(it -> {
            try {
                return entitiesMap.get(indexSearcher.storedFields().document(it.doc)
                        .getField(ID).numericValue().longValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());
    }

    abstract Set<T> neighborsInternal(IndexSearcher indexSearcher, V point, double radius) throws IOException;

    abstract Set<T> neighborsInternal(IndexSearcher indexSearcher, V stripePointA, V stripePointB, double radius) throws IOException;

    @Override
    public Set<T> neighbors(V point, double radius) {
        try (var indexReader = DirectoryReader.open(directory)) {
            return neighborsInternal(new IndexSearcher(indexReader), point, radius);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<T> neighbors(V stripePointA, V stripePointB, double radius) {
        try (var indexReader = DirectoryReader.open(directory)) {
            return neighborsInternal(new IndexSearcher(indexReader), stripePointA, stripePointB, radius);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void wrapCall(boolean clearBeforeIndex, IndexWriterCallback indexWriterAction) {
        try (final IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig())) {
            try {
                if (clearBeforeIndex)
                    indexWriter.deleteAll();
                if (indexWriterAction != null)
                    indexWriterAction.index(indexWriter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Shape circle2D(Vector2D point, double radius) {
        return factory.circle(factory.pointXY(point.getX(), point.getY()), radius);
    }

    protected Shape circle3D(Vector3D point, double radius) {
        return factory.circle(factory.pointXYZ(point.getX(), point.getY(), point.getZ()), radius);
    }

    protected Shape stripe2D(Vector2D stripePointA, Vector2D stripePointB, double radius) {
        return factory.multiShape(Shape.class)
                .add(factory.circle(factory.pointXY(stripePointB.getX(), stripePointB.getY()), radius))
                .add(factory.rect(Math.min(stripePointA.getX(), stripePointB.getX()),
                        Math.max(stripePointA.getX(), stripePointB.getX()),
                        Math.min(stripePointA.getY(), stripePointB.getY()),
                        Math.max(stripePointA.getY(), stripePointB.getY())
                ))
                .build();
    }

    protected Shape stripe3D(Vector3D stripePointA, Vector3D stripePointB, double radius) {
        return factory.multiShape(Shape.class)
                .add(factory.circle(factory.pointXYZ(stripePointB.getX(), stripePointB.getY(), stripePointB.getZ()), radius))
                .add(factory.rect(
                        Math.min(stripePointA.getX(), stripePointB.getX()),
                        Math.max(stripePointA.getX(), stripePointB.getX()),
                        Math.min(stripePointA.getY(), stripePointB.getY()),
                        Math.max(stripePointA.getY(), stripePointB.getY())
                ))
                .build();
    }
}
