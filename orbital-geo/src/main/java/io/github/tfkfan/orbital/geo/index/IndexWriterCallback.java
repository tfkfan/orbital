package io.github.tfkfan.orbital.geo.index;

import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

/**
 * @author Baltser Artem tfkfan
 */
@FunctionalInterface
public interface IndexWriterCallback {
    void index(IndexWriter indexWriter) throws IOException;
}
