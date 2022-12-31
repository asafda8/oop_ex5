package pepse;

import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;

public class PepseGameManager extends GameManager {

    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int SUN_HALO_LAYER = SUN_LAYER + 1;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;

    public PepseGameManager(String title, Vector2 windowDimensions) {
        super(title, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 windowDimensions = windowController.getWindowDimensions();

        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, 30f);
//        GameObject sun = new GameObject(Vector2.ONES.multY(200), new Vector2(50, 50), new OvalRenderable(Color.WHITE));
//        gameObjects().addGameObject(sun, SUN_LAYER);
//        SunHalo.create(gameObjects(), SUN_HALO_LAYER, sun, new Color(255, 255, 0, 20));
    }

    public static void main(String[] args) {
        new PepseGameManager("Pepse", new Vector2(700, 500)).run();
    }
}
