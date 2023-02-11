package pepse.powerups;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

/**
 * a powerup that makes the avatar bigger
 */
public class EnlargePowerup extends Powerup{
    /**
     * path to image of the powerup
     */
    private static final String ASSET_PATH = "assets\\apple.png";
    /**
     * factor for avatar enlargement
     */
    private static final int DIMENSIONS_ENLARGE_FACTOR = 2;
    /**
     * factor for decreasing the size back to normal
     */
    private static final float DIMENSIONS_DECREASE_FACTOR = 1/2.0f;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param avatar
     */
    public EnlargePowerup(Vector2 topLeftCorner, Vector2 dimensions, ImageReader imageReader, Avatar avatar
    , GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, imageReader, ASSET_PATH, avatar, gameObjects);
    }

    /**
     * makes avatar two times bigger
     */
    @Override
    public void powerup() {
        Avatar avatar = this.getAvatar();
        avatar.setDimensions(avatar.getDimensions().mult(DIMENSIONS_ENLARGE_FACTOR));
    }

    /**
     * brings the avatar back to the size it was
     */
    @Override
    public void returnToRegular() {
        Avatar avatar = this.getAvatar();
        avatar.setDimensions(avatar.getDimensions().mult(DIMENSIONS_DECREASE_FACTOR));
    }
}
