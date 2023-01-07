package pepse.powerups;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

/**
 * abstract class representing a powerup that
 * makes the avatar different for a fixed time
 */
abstract public class Powerup extends GameObject {
    /**
     * value for making powerup transparent
     */
    private static final int NO_OPAQUENESS = 0;
    /**
     * time of powering up the avatar
     */
    private static final int POWER_UP_TIME = 10;
    /**
     * objects of the game
     */
    private GameObjectCollection gameObjects;
    /**
     * whether the avatar collided with powerup or not
     */
    private Boolean collided = false;
    /**
     * avatar of the game
     */
    private Avatar avatar;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Powerup(Vector2 topLeftCorner, Vector2 dimensions, ImageReader imageReader,
                   String assetPath, Avatar avatar, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, imageReader.readImage(assetPath, true));
        this.avatar = avatar;
        this.gameObjects = gameObjects;
        gameObjects.addGameObject(this);
    }

    /**
     * should only collide with avatar
     * @param other The other GameObject.
     * @return: whether we should collide with object
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other == avatar;
    }

    /**
     * what to do when colliding with avatar
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(shouldCollideWith(other) && !collided){
            powerup();
            new ScheduledTask(this.avatar, POWER_UP_TIME, false, this::returnToRegular);
            this.renderer().setOpaqueness(NO_OPAQUENESS);
            collided = true;
        }

    }

    /**
     * returns avatar instance
     * @return: Avatar of the game
     */
    public Avatar getAvatar() {
        return avatar;
    }

    /**
     * function to activate when activating power up
     */
    abstract public void powerup();

    /**
     * function to activate when getting the avatar back to normal
     */
    abstract public void returnToRegular();
}
