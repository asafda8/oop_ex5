package pepse.world;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.components.GameObjectPhysics;

/**
 * class representing a block of ground
 */
public class Block extends GameObject{
    /**
     * size of the block
     */
    public static final int SIZE = 30;

    /**
     * constructor of a ground block
     * @param topLeftCorner: vector representing the top left corner of the block
     * @param renderable: renderable of the block
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
