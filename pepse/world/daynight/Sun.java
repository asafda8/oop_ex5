package pepse.util.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun extends GameObject {
    private static final int STARTING_X_COORDINATE = 50;
    private static final int STARTING_Y_COORDINATE = 50;
    private static final int DIMENSION = 20;
    private static final Vector2 STARTING_POSITION_VECTOR =new Vector2(STARTING_X_COORDINATE, STARTING_Y_COORDINATE);
    private static final Vector2 DIMENSIONS =new Vector2(DIMENSION, DIMENSION);
    private static final float FIRST_ANGLE = (float)Math.PI * 0.53f;
    private static final float LAST_ANGLE = (float)Math.PI * 0.45f;
    private static final float FACTOR_FOR_ANGLE = 0.45f;

    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        Sun sun = new Sun(STARTING_POSITION_VECTOR, DIMENSIONS, new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        new Transition<Float>(
                sun, (x) -> sun.setCenter(sun.getCenter().add((new Vector2((float) Math.sin(x),
                (float) Math.cos(x))).mult(FACTOR_FOR_ANGLE))), (FIRST_ANGLE), (LAST_ANGLE),
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, sun::returnToInitial);
        return sun;
    }

    private void returnToInitial() {
        this.setCenter(STARTING_POSITION_VECTOR);
    }

    private Sun(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }
}
