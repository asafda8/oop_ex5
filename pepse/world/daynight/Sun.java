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
    /**
     * Starting x coordinate for the sun
     */
    private static final int STARTING_X_COORDINATE = 50;
    /**
     * starting y coordinate for the sun
     */
    private static final int STARTING_Y_COORDINATE = 50;
    /**
     * radius of the sun
     */
    private static final int DIMENSION = 20;
    /***
     * Vector for initial position of the sun
     */
    private static final Vector2 STARTING_POSITION_VECTOR =new Vector2(STARTING_X_COORDINATE, STARTING_Y_COORDINATE);
    /**
     * vector of dimensions of the sun
     */
    private static final Vector2 DIMENSIONS =new Vector2(DIMENSION, DIMENSION);
    /**
     * First angle of movement at the start of the day
     */
    private static final float FIRST_ANGLE = (float)Math.PI * 0.53f;
    /**
     * last angle of movement at the end of the day
     */
    private static final float LAST_ANGLE = (float)Math.PI * 0.45f;
    /***
     * factor to multiply vector with for movement
     */
    private static final float FACTOR_FOR_ANGLE = 0.45f;

    /**
     * creates a sun and returns its' instance
     * @param gameObjects: objects of the game
     * @param layer: layer to put sun in
     * @param windowDimensions: dimensions of the window
     * @param cycleLength: length of the cycle in which
     * @return gameobject instance of the sun
     */
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

    /**
     * returns the sun to its' initial position
     */
    private void returnToInitial() {
        this.setCenter(STARTING_POSITION_VECTOR);
    }

    /**
     * private constructor for the sun
     * @param topLeftCorner: top left corner vector of initial position
     * @param dimensions: dimensions of the sun
     * @param renderable: renderable for the sun
     */
    private Sun(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }
}
