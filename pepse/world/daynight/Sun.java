package pepse.util.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun extends GameObject {
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        Sun sun = new Sun(new Vector2(50, 50), new Vector2(20, 20),new OvalRenderable(Color.YELLOW));
        gameObjects.addGameObject(sun, layer);
        new Transition<Float>(
                sun, (x) -> sun.setCenter(sun.getCenter().add((new Vector2((float) Math.sin(x),
                (float) Math.cos(x))).mult(0.45f))),
                ((float)Math.PI * 0.53f), ((float)Math.PI * 0.45f),
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength/2,
                Transition.TransitionType.TRANSITION_LOOP, sun::returnToInitial);
        return sun;
    }

    private void returnToInitial() {
        this.setCenter(new Vector2(50, 50));
    }

    private Sun(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }
}
