package io.github.tfkfan.orbital.geo.index;

public interface IndexWriter<O> extends AutoCloseable, IndexWriteOperations<O> {
    <W extends IndexWriter<O>> W open();
}
