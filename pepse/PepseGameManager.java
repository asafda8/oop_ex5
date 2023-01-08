package pepse.util;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.powerups.Powerup;
import pepse.powerups.PowerupRandomFactory;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import java.util.Random;

/**
 * class representing the manager of
 * pepse game
 */
public class PepseGameManager extends GameManager {
    /**
     * waiting time between spawning of new powerup
     */
    private static final int POWERUP_WAITING_TIME = 20;
    /**
     * random variable for generating coordinates for powerup
     */
    private static final Random RANDOM_FOR_POWERUPS = new Random();
    /**
     * layer of the sky
     */
    private static final int SKY_LAYER = Layer.BACKGROUND;
    /**
     * layer of the sun
     */
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    /**
     * layer of the halo
     */
    private static final int SUN_HALO_LAYER = SUN_LAYER + 1;
    /**
     * layer of the blocks
     */
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    /**
     * layer of the night object
     */
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    /**
     * last record x coordinate of the avatar for
     * tracking world to create
     */
    private float lastXCoordinateOfAvatar;
    /**
     * object for creating terrain
     */
    private Terrain terrain;
    /**
     * current powerup in the screen
     */
    private Powerup currentPowerup = null;
    /**
     * avatar of the game
     */
    private Avatar avatar;
    /**
     * reader of images
     */
    private ImageReader imageReader;
    /**
     * seed to give perlin noise
     */
    private static final int SEED_OF_GAME = 0;
    /**
     * Color for halo of the sun
     */
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    /**
     * starting coordinate for creating world
     */
    private static final int STARTING_COORDINATE = 0;
    /**
     * cycle number for the day
     */
    private static final float DAY_CYCLE = 30f;
    /**
     * width of window of the game
     */
    private static final int WINDOW_WIDTH = 700;
    /**
     * name of the game
     */
    private static final String NAME_OF_GAME = "pepse";
    /**
     * height of window of the game
     */
    private static final int WINDOW_HEIGHT = 500;
    /**
     * factor for dividing with to get margin
     */
    private static final int FACTOR_FOR_MARGIN = 2;
    /**
     * constructor for the game manager
     * @param title: title for the game
     * @param windowDimensions: dimensions of the window
     */
    public PepseGameManager(String title, Vector2 windowDimensions) {
        super(title, windowDimensions);
    }

    /**
     * This function initializes the game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();

        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, DAY_CYCLE);
        GameObject sun = pepse.util.world.daynight.Sun.create(gameObjects(), SUN_LAYER,
                windowDimensions
                ,DAY_CYCLE);
        gameObjects().addGameObject(sun, SUN_LAYER);
        SunHalo.create(gameObjects(), SUN_HALO_LAYER, sun, HALO_COLOR);
        terrain = new Terrain(gameObjects(), TERRAIN_LAYER, windowDimensions, SEED_OF_GAME);
        terrain.createInRange(STARTING_COORDINATE, (int)windowController.getWindowDimensions().x());
        this.imageReader = imageReader;
        avatar = Avatar.create(gameObjects(), Layer.DEFAULT, windowDimensions.mult(0.5f), imageReader,
                inputListener);
        lastXCoordinateOfAvatar = avatar.getCenter().x();
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        createPowerupRandomly();
        new ScheduledTask(this.currentPowerup, POWERUP_WAITING_TIME,  true, this::createPowerupRandomly);
    }

    /**
     * main function of the project. creates and runs the manager
     * @param args
     */
    public static void main(String[] args) {
        new PepseGameManager(NAME_OF_GAME, new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
    }

    /**
     * update manager
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float margin = getCamera().windowDimensions().x() / FACTOR_FOR_MARGIN + Block.SIZE;
        terrain.removeInRange(lastXCoordinateOfAvatar - margin,
               avatar.getCenter().x() -  margin);
        terrain.removeInRange(avatar.getCenter().x() +  margin
                ,lastXCoordinateOfAvatar + margin);
        terrain.createInRange((int)(avatar.getCenter().x() -  margin),
                (int)(lastXCoordinateOfAvatar - margin));
        terrain.createInRange((int)(lastXCoordinateOfAvatar + margin),
                (int)(avatar.getCenter().x() + margin));
        lastXCoordinateOfAvatar = avatar.getCenter().x();
    }

    /**
     * creates the a random powerup in a random position
     */
    private void createPowerupRandomly(){
        if(this.currentPowerup != null){
            gameObjects().removeGameObject(currentPowerup);
        }
        int lowerBound = (int)getCamera().getTopLeftCorner().x();
        int upperBound =(int)(lowerBound + getCamera().windowDimensions().x());
        int nonDivideableCoordinate = RANDOM_FOR_POWERUPS.nextInt(lowerBound, upperBound);
        int finalCoordinate = (nonDivideableCoordinate / Block.SIZE) * Block.SIZE;
        Vector2 positionVector = new Vector2(finalCoordinate,
                terrain.groundHeightAt(finalCoordinate) - Block.SIZE);
        this.currentPowerup = PowerupRandomFactory.getRandomPowerup(positionVector, new Vector2(Block.SIZE,
                Block.SIZE), imageReader,avatar, gameObjects());
        new ScheduledTask(this.currentPowerup, POWERUP_WAITING_TIME, true,
                this::createPowerupRandomly);
    }
}
