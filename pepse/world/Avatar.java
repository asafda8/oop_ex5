package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.*;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.security.Key;

/**
 * class representing avatar of the game
 */

public class Avatar extends GameObject{
    /**
     * amount of energy to increase/decrease
     */
    private static final float ENERGY_TO_SUBSTRACT_OR_FILL = 0.5f;
    /**
     * wanted velocity for the avatar when resting at y
     */
    private static final int RESTING_Y_VELOCITY = 0;
    /**
     * wanted velocity of avatar when standing at x
     */
    private static final int STANDING_X_VELOCITY = 0;
    /**
     * value when energy is empty
     */
    private static final int EMPTY_ENERGY = 0;
    /**
     * dimensions vector of the avatar
     */
    private static final Vector2 DIMENSIONS_OF_AVATAR =  new Vector2(30, 50);
    /**
     * empty string to get the absolute path
     */
    private static final String EMPTY_STRING_FOR_PATH = "";
    /**
     * relative path of avatar when standing
     */
    private static final String AVATAR_STANDING_PATH = "\\assets\\Avatar_Aang_standing.png";
    /**
     * relative path of avatar when running
     */
    private static final String AVATAR_RUNNING_PATH = "\\assets\\Avatar_Aang_running.png";
    /**
     * regular moving speed of avatar
     */
    private static final float MOVE_SPEED = 100f;
    /**
     * jumping height of the avatar
     */
    private static final float JUMP_HEIGHT = 4000f;
    /**
     * maximum energy value of avatar
     */
    private static final float MAX_ENERGY = 100f;
    /**
     * speed when flying
     */
    private static final float FLIGHT_SPEED = 200f;
    /**
     * gravity speed
     */
    private static final float GRAVITY = 3000f;
    /**
     * time between running images
     */
    private static final float TIME_BETWEEN_CLIPS = 0.2f;
    /**
     * renderable representing image when standing
     */
    private static ImageRenderable standingImage;
    /**
     * renderable representing image when running
     */
    private static ImageRenderable runningImage;
    /**
     * animation for walking
     */
    private final AnimationRenderable walkingAnimation;
    /**
     * energy level of the avatar
     */
    private float energyLevel = MAX_ENERGY;
    /**
     * listener for keys
     */
    private UserInputListener inputListener;
    /**
     * move speed of the avatar
     */
    private float moveSpeed;
    /**
     * position of the energy indicator
     */
    private static final Vector2 ENERGY_INDICATOR_POSITION =  new Vector2(50, 50);

    /**
     * constructor for the avatar
     * @param topLeftCorner: starting top left corner
     * @param dimensions: dimensions of avatar
     * @param renderable: renderable of the avatar
     * @param inputListener: input listener for keys
     * @param gameObjects: objects of the game
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;

        walkingAnimation = new AnimationRenderable(new Renderable[]{runningImage, runningImage},
                TIME_BETWEEN_CLIPS);
        moveSpeed = MOVE_SPEED;
    }

    /**
     * multiplys speed of avatar by some factor
     * @param factor: factor to multiply by
     */
    public void multMoveSpeed(float factor){
        moveSpeed *= factor;
    }

    /**
     * updates the avatar
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 velocity = Vector2.ZERO;
        // run left
        if (this.inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            velocity = velocity.add(Vector2.LEFT.mult(moveSpeed));
            this.renderer().setRenderable(walkingAnimation);
            this.renderer().setIsFlippedHorizontally(true);
        }
        // run right
        if (this.inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            velocity = velocity.add(Vector2.RIGHT.mult(moveSpeed));
            this.renderer().setRenderable(walkingAnimation);
            this.renderer().setIsFlippedHorizontally(false);
        }
        // jump
        if ( this.getVelocity().y() == RESTING_Y_VELOCITY &&
                this.inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            velocity = velocity.add(Vector2.UP.multY(JUMP_HEIGHT));
        }
        // fly
        if ( energyLevel > EMPTY_ENERGY &&
                this.inputListener.isKeyPressed(KeyEvent.VK_SHIFT) &&
                this.inputListener.isKeyPressed(KeyEvent.VK_SPACE) ) {
            velocity = velocity.add( Vector2.UP.multY(FLIGHT_SPEED) );
            energyLevel = Math.max(energyLevel - ENERGY_TO_SUBSTRACT_OR_FILL, EMPTY_ENERGY);
        }
        // stand (animation)
        if (velocity.x() == STANDING_X_VELOCITY && velocity.y() == RESTING_Y_VELOCITY) {
            this.renderer().setRenderable(standingImage);
        }
        // rest
        if (isResting())
            energyLevel = Math.min(MAX_ENERGY, energyLevel + ENERGY_TO_SUBSTRACT_OR_FILL);

        transform().setAccelerationY(GRAVITY);
        this.transform().setVelocity(velocity);
    }

    /**
     * true if avatar is resting and false otherwise
     * @return: boolean representing that the avatar is standing
     */
    private boolean isResting() {
        return getVelocity().y() == RESTING_Y_VELOCITY;
    }

    /**
     * creates the avatar game object
     * @param gameObjects: game objects
     * @param layer: layer of avatar
     * @param topLeftCorner: top left corner of avatar
     * @param imageReader: reader of images
     * @param inputListener: input listener for keys
     * @return: avatar object
     */

    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader) {
        initImages(imageReader);
        
        Avatar avatar = new Avatar(
                topLeftCorner,DIMENSIONS_OF_AVATAR, standingImage, inputListener, gameObjects
        );
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);

        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * inits the images of the game
     * @param imageReader: reader for images
     */
    private static void initImages(ImageReader imageReader) {
        String basePath = new File(EMPTY_STRING_FOR_PATH).getAbsolutePath();
        String standingImagePath = basePath.concat(AVATAR_STANDING_PATH);
        String runningImagePath1 = basePath.concat(AVATAR_RUNNING_PATH);
        String runningImagePath2 = basePath.concat(AVATAR_RUNNING_PATH);

        standingImage = imageReader.readImage(standingImagePath, true);
        runningImage = imageReader.readImage(runningImagePath1, true);
    }
}
