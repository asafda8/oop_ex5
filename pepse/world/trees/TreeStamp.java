package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;

public class TreeStamp extends GameObject {
    private static final Color STAMP_COLOR = new Color(100, 50, 20);
    private static final Vector2 SINGLE_BLOCK_DIMENSIONS = new Vector2(Block.SIZE, Block.SIZE);
    private static final RectangleRenderable STAMP_RENDERABLE = new RectangleRenderable(STAMP_COLOR);
    private static final Vector2 VECTOR_TO_FACTOR_FOR_DIMENSIONS = new Vector2(0, Block.SIZE);

    private static final int INIT_MASS = 0;
    public TreeStamp(Vector2 topLeftCorner, int stampLength) {
        super(topLeftCorner,
                SINGLE_BLOCK_DIMENSIONS.add(VECTOR_TO_FACTOR_FOR_DIMENSIONS.mult(stampLength)) ,
                STAMP_RENDERABLE);
        this.physics().setMass(INIT_MASS);
    }
}
