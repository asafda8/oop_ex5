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
import pepse.powerups.EnlargePowerup;
import pepse.powerups.FasterPowerup;
import pepse.powerups.Powerup;
import pepse.powerups.PowerupRandomFactory;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class PepseGameManager extends GameManager {
    private static final int POWERUP_WAITING_TIME = 20;
    private static final Random randomForPowerups = new Random();
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int SUN_HALO_LAYER = SUN_LAYER + 1;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private ScheduledTask scheduledTask;
    private float lastXCoordinateOfAvatar;
    private Terrain terrain;
    private Powerup currentPowerup = null;
    private Avatar avatar;
    private ImageReader imageReader;
    public PepseGameManager(String title, Vector2 windowDimensions) {
        super(title, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();

        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, 30f);
        GameObject sun = pepse.util.world.daynight.Sun.create(gameObjects(), SUN_LAYER,
                windowDimensions
                ,30);
        gameObjects().addGameObject(sun, SUN_LAYER);
        SunHalo.create(gameObjects(), SUN_HALO_LAYER, sun, new Color(255, 255, 0, 20));
        terrain = new Terrain(gameObjects(), TERRAIN_LAYER, windowDimensions, 0);
       terrain.createInRange(-10, 1000);
        this.imageReader = imageReader;
       avatar = Avatar.create(gameObjects(), Layer.DEFAULT, windowDimensions.mult(0.5f), imageReader,
                inputListener);
        lastXCoordinateOfAvatar = avatar.getCenter().x();
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        createPowerupRandomly();
        scheduledTask = new ScheduledTask(this.currentPowerup, POWERUP_WAITING_TIME,  true, this::createPowerupRandomly);
    }

    public static void main(String[] args) {
        new PepseGameManager("Pepse", new Vector2(700, 500)).run();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float margin = getCamera().windowDimensions().x() / 2 + Block.SIZE;
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
    private void createPowerupRandomly(){
        if(this.currentPowerup != null){
            gameObjects().removeGameObject(currentPowerup);
        }
        int lowerBound = (int)getCamera().getTopLeftCorner().x();
        int upperBound =(int)(lowerBound + getCamera().windowDimensions().x());
        int nonDivideableCoordinate = randomForPowerups.nextInt(lowerBound, upperBound);
        int finalCoordinate = (nonDivideableCoordinate / Block.SIZE) * Block.SIZE;
        Vector2 positionVector = new Vector2(finalCoordinate,
                terrain.groundHeightAt(finalCoordinate) - Block.SIZE);
        this.currentPowerup = PowerupRandomFactory.getRandomPowerup(positionVector, new Vector2(Block.SIZE,
                Block.SIZE), imageReader,avatar, gameObjects());
        this.scheduledTask = new ScheduledTask(this.currentPowerup, POWERUP_WAITING_TIME, true,
                this::createPowerupRandomly);
    }
}
