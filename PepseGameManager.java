package pepse.util;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;

public class PepseGameManager extends GameManager {
    public PepseGameManager(String title, Vector2 windowDimensions) {
        super(title, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();

        Sky.create(gameObjects(), windowDimensions, Layer.BACKGROUND);
        Night.create(gameObjects(), Layer.FOREGROUND, windowDimensions, 30f);
    }

    public static void main(String[] args) {
        new PepseGameManager("Pepse", new Vector2(700, 500)).run();
    }
}
