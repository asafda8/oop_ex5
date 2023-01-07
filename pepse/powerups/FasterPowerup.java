package pepse.powerups;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.world.Avatar;

public class FasterPowerup extends Powerup{
    private static final String ASSET_PATH = "assets//shoe.png";
    private static final int INCREASE_SPEED_FACTOR = 3;
    private static final float DECREASE_SPEED_FACTOR = 1/3.0f;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param imageReader
     * @param assetPath
     * @param avatar
     * @param gameObjects
     */
    public FasterPowerup(Vector2 topLeftCorner, Vector2 dimensions, ImageReader imageReader, Avatar avatar
            , GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, imageReader, ASSET_PATH, avatar, gameObjects);
    }

    @Override
    public void powerup() {
        this.getAvatar().multMoveSpeed(INCREASE_SPEED_FACTOR);
    }

    @Override
    public void returnToRegular() {
        this.getAvatar().multMoveSpeed(DECREASE_SPEED_FACTOR);
    }
}
