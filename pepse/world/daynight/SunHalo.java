package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Component;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * class representing the halo of the sun
 */
public class SunHalo {
    /**
     * factor to get dimension of the halo
     */
    private static final float FACTOR_FOR_MULTIPLYING_DIMENSIONS = 2f;
    /**
     * tag of halo in game objects
     */
    private static final String TAG_OF_HALO = "halo";
    /**
     * creates the halo of the sun
     * @param gameObjects: objects of the game
     * @param layer: layer of the halo
     * @param sun: sun of the game
     * @param color: color for halo
     * @return: halo object
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer, GameObject sun, Color color) {
        GameObject sunHalo = new GameObject(
                Vector2.ZERO,
                sun.getDimensions().mult(FACTOR_FOR_MULTIPLYING_DIMENSIONS),
                new OvalRenderable(color)
        );

        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects.addGameObject(sunHalo, layer);

        sunHalo.setTag(TAG_OF_HALO);

        sunHalo.addComponent(new Component() {
            /**
             * updates halo center to sun's center
             * @param deltaTime the time, in seconds, since the previous update
             */
            @Override
            public void update(float deltaTime) {
                sunHalo.setCenter(sun.getCenter());
            }
        });

        return sunHalo;
    }
}
