package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.spatial.query.SpatialArgs;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class IndexReaderImpl<O, T, V extends Vector<V>> implements IndexReader<T, V> {
    private final AbstractIndex<O, T, V> index;

    private org.apache.lucene.index.IndexReader luceneIndexReader;
    private IndexSearcher searcher;

    private void openLuceneIndexReaderIfClosed() throws IOException {
        if (luceneIndexReader == null) {
            luceneIndexReader = index.openReader();
            searcher = new IndexSearcher(luceneIndexReader);
        }
    }

    @Override
    public void close() throws IOException {
        if (luceneIndexReader != null)
            luceneIndexReader.close();
        luceneIndexReader = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <W extends IndexReader<T, V>> W open() {
        try {
            openLuceneIndexReaderIfClosed();
            return (W) this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<T> neighbors(V point, double radius) {
        try {
            return index.neighborsInternal(searcher, point, radius);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<T> neighbors(V stripePointA, V stripePointB, double radius) {
        try {
            return index.neighborsInternal(searcher, stripePointA, stripePointB, radius);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<T> search(SpatialArgs args) {
        try {
            return index.searchInternal(searcher, args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<T> search(SpatialArgs args, int n) {
        try {
            return index.searchInternal(searcher, args, n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
