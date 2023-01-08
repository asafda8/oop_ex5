package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * class representing the appearance and disappearance of the night
 */
public class Night {
    /**
     * opacity at noon
     */
    private static final Float NOON_OPACITY = 0f;
    /**
     * opacity at midnight
     */
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    /**
     * tag to put for night object
     */
    private static final String NIGHT_TAG = "night";
    /**
     * factor for dividing the cycle length.
     * needed because of the back and forth transition
     */
    private static final int FACTOR_FOR_DIVIDING_CYCLE = 2;

    /**
     * creates a night object
     * @param gameObjects: objects of the game
     * @param layer: layer for the object
     * @param windowDimensions: dimensions of the game window
     * @param cycleLength: length of the cycle
     * @return: night object
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 windowDimensions,
                                    float cycleLength) {
        GameObject night = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(Color.BLACK)
        );

        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects.addGameObject(night, layer);

        night.setTag(NIGHT_TAG);

        new Transition<Float>(
                night, night.renderer()::setOpaqueness,
                MIDNIGHT_OPACITY,NOON_OPACITY ,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength/FACTOR_FOR_DIVIDING_CYCLE,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null
        );

        return night;
    }
}
