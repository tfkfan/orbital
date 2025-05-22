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

public class GameEntityIndexTest {
    static final class TestEntity extends BasePlayer<Vector2D> {
        public TestEntity(Long id, Vector2D position) {
            super(id, null, new PlayerSession());
            setPosition(position);
        }

        @Override
        public PrivateUpdatePack getPrivateUpdatePack() {
            return null;
        }

        @Override
        public InitPack getInitPack() {
            return null;
        }

        @Override
        public UpdatePack getUpdatePack() {
            return null;
        }
    }

    private GameEntity2DIndex<TestEntity> index;
    private JtsSpatialContext ctx;

    private Long seq = 1L;

    @BeforeEach
    public void setUp() {
        JtsSpatialContextFactory factory = new JtsSpatialContextFactory();
        factory.geo = false;
        factory.worldBounds = new RectangleImpl(-1000, 1000, -1000, 1000, null);

        ctx = new JtsSpatialContext(factory);
        index = new GameEntity2DIndex<>(ctx, 11);
    }

    private TestEntity create(Vector2D position) {
        return new TestEntity(seq++, position);
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
    public void test3() {
        index.index(List.of(
                create(new Vector2D()),
                create(new Vector2D(5.0, 0.0)),
                create(new Vector2D(10.0, 0.0))
        ));

        assertEquals(3, index.neighbors(new Vector2D(), 10.0).size());
    }
}
