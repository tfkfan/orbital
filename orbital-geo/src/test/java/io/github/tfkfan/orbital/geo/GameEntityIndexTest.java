package io.github.tfkfan.orbital.geo;

import io.github.tfkfan.orbital.core.math.Vector2D;
import io.github.tfkfan.orbital.core.model.BaseGameEntity;
import io.github.tfkfan.orbital.core.model.players.BasePlayer;
import io.github.tfkfan.orbital.core.network.pack.InitPack;
import io.github.tfkfan.orbital.core.network.pack.PrivateUpdatePack;
import io.github.tfkfan.orbital.core.network.pack.UpdatePack;
import io.github.tfkfan.orbital.core.room.GameRoom;
import io.github.tfkfan.orbital.core.session.PlayerSession;
import io.github.tfkfan.orbital.geo.index.GameEntity2DIndex;
import io.github.tfkfan.orbital.geo.index.Polygon2DIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.jts.JtsSpatialContext;
import org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;
import org.locationtech.spatial4j.shape.jts.JtsGeometry;
import org.locationtech.spatial4j.shape.jts.JtsShapeFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameEntityIndexTest extends BaseGameEntityIndexTest {
    private GameEntity2DIndex<TestEntity> index;

    @BeforeEach
    public void setUp() {
        index = new GameEntity2DIndex<>(setUpJts(), 11);
    }

    @Test
    public void test1() {
        index.index(List.of(
                create(new Vector2D()),
                create(new Vector2D(5.0, 0.0)),
                create(new Vector2D(10.0, 0.0))
        ));

        assertEquals(1, index.neighbors(new Vector2D(10.0, 0.0), 2.0).size());
        assertEquals(1, index.neighbors(new Vector2D(), 0.0).size());
        assertEquals(2, index.neighbors(new Vector2D(), 5.0).size());
        assertEquals(3, index.neighbors(new Vector2D(), 10.0).size());
    }

    @Test
    public void test2() {
        index.index(List.of(
                create(new Vector2D()),
                create(new Vector2D(5.0, 0.0)),
                create(new Vector2D(10.0, 0.0))
        ));

        assertEquals(3, index.neighbors(new Vector2D(), 10.0).size());
    }
}
