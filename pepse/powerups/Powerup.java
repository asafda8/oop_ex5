package pepse.powerups;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

abstract public class Powerup extends GameObject {
    private static final int POWER_UP_TIME = 10;
    private GameObjectCollection gameObjects;
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

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other == avatar;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(shouldCollideWith(other)){
            powerup();
            new ScheduledTask(this.avatar, POWER_UP_TIME, false, this::returnToRegular);
            this.gameObjects.removeGameObject(this);
        }

    }

    public Avatar getAvatar() {
        return avatar;
    }

    abstract public void powerup();
    abstract public void returnToRegular();
}
