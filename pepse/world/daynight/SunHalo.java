package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Component;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer, GameObject sun, Color color) {
        GameObject sunHalo = new GameObject(
                Vector2.ZERO,
                sun.getDimensions().mult(2f),
                new OvalRenderable(color)
        );

        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects.addGameObject(sunHalo, layer);

        sunHalo.setTag("halo");

        sunHalo.addComponent(new Component() {
            @Override
            public void update(float deltaTime) {
                sunHalo.setCenter(sun.getCenter());
            }
        });

        return sunHalo;
    }
}
