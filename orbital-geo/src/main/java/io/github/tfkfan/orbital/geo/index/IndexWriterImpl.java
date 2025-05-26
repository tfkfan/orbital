package io.github.tfkfan.orbital.geo.index;

import io.github.tfkfan.orbital.core.math.Vector;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class IndexWriterImpl<O, T, V extends Vector<V>> implements IndexWriter<O> {
    private final AbstractIndex<O, T, V> index;

    private org.apache.lucene.index.IndexWriter luceneIndexWriter;

    @Override
    public void delete(O o) {

    }

    @Override
    public void clear() {
        try {
            openLuceneIndexWriterIfClosed();
            index.clearInternal(luceneIndexWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void index(O entity) {
        try {
            openLuceneIndexWriterIfClosed();
            index.indexInternal(luceneIndexWriter, entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void index(Collection<O> entities) {
        try {
            openLuceneIndexWriterIfClosed();
            index.indexInternal(luceneIndexWriter, entities);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <W extends IndexWriter<O>> W open() {
        try {
            close();
            luceneIndexWriter = index.openWriter();
            return (W) this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openLuceneIndexWriterIfClosed() throws IOException {
        if (luceneIndexWriter == null)
            luceneIndexWriter = open();
    }

    @Override
    public void close() throws IOException {
        if (luceneIndexWriter != null)
            luceneIndexWriter.close();
        luceneIndexWriter = null;
    }
}
