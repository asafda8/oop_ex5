package pepse.util;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.powerups.EnlargePowerup;
import pepse.powerups.FasterPowerup;
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

public class PepseGameManager extends GameManager {

    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int SUN_HALO_LAYER = SUN_LAYER + 1;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private float lastXCoordinateOfAvatar;
    private Terrain terrain;
    private Avatar avatar;
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


       avatar = Avatar.create(gameObjects(), Layer.DEFAULT, windowDimensions.mult(0.5f), imageReader,
                inputListener);
        lastXCoordinateOfAvatar = avatar.getCenter().x();
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
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
}
