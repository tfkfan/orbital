package io.github.tfkfan.orbital.geo;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.geo.index.GameEntity2DIndex;
import io.github.tfkfan.orbital.geo.index.IndexReader;
import io.github.tfkfan.orbital.geo.index.IndexReaderImpl;
import io.github.tfkfan.orbital.geo.index.IndexWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameEntityIndexReaderWriterTest extends BaseGameEntityIndexTest {
    private GameEntity2DIndex<TestEntity> index;
    private IndexWriter<TestEntity> indexWriter;
    private IndexReader<TestEntity, Vector2D> indexReader;


    @BeforeEach
    public void setUp() {
        index = new GameEntity2DIndex<>(setUpJts(), 11);
        indexWriter = index.writer();
        indexReader = index.reader();

        indexWriter.open();
    }

    @Test
    public void test1() {
        indexWriter.index(List.of(
                create(new Vector2D()),
                create(new Vector2D(5.0, 0.0)),
                create(new Vector2D(10.0, 0.0))
        ));
        indexWriter.flush();

        indexReader.open();
        assertEquals(1, indexReader.neighbors(new Vector2D(10.0, 0.0), 2.0).size());
        assertEquals(1, indexReader.neighbors(new Vector2D(), 0.0).size());
        assertEquals(2, indexReader.neighbors(new Vector2D(), 5.0).size());
        assertEquals(3, indexReader.neighbors(new Vector2D(), 10.0).size());
    }
}
