package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;

/**
 * class representing the stamp of the tree
 */
public class TreeStamp extends GameObject {
    /**
     * color of stamp
     */
    private static final Color STAMP_COLOR = new Color(100, 50, 20);
    /**
     * dimensions of a single block
     */
    private static final Vector2 SINGLE_BLOCK_DIMENSIONS = new Vector2(Block.SIZE, Block.SIZE);
    /**
     * the renderable of the stamp
     */
    private static final RectangleRenderable STAMP_RENDERABLE = new RectangleRenderable(STAMP_COLOR);
    /**
     * vector to multiply with when building the stamp
     */
    private static final Vector2 VECTOR_TO_FACTOR_FOR_DIMENSIONS = new Vector2(0, Block.SIZE);
    /**
     * mass of stamp
     */

    private static final int INIT_MASS = 0;

    /**
     * constructor for the stamp
     * @param topLeftCorner: top left corner coordinates
     * @param stampLength: length of the stamp with blocks
     */
    public TreeStamp(Vector2 topLeftCorner, int stampLength) {
        super(topLeftCorner,
                SINGLE_BLOCK_DIMENSIONS.add(VECTOR_TO_FACTOR_FOR_DIMENSIONS.mult(stampLength)) ,
                STAMP_RENDERABLE);
        this.physics().setMass(INIT_MASS);
    }
}
