package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class Leaf extends GameObject {
    private static final Float INITIAL_ROTATE_TRANSITION_VALUE = -5f;
    private static final Float FINAL_ROTATE_TRANSITION_VALUE = 5f;
    private final static Vector2 FALLING_VELOCITY = new Vector2(0, 10);
    private final static int WAITING_BOUND = 100;
    private final static int FADEOUT_TIME = 10;
    private static final float NO_MASS = 0;

    private static final Random timeRandomGenerator = new Random();
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final RectangleRenderable LEAF_RENDERABLE = new RectangleRenderable(LEAF_COLOR);
    private static final Vector2 LEAF_DIMS = new Vector2(Block.SIZE, Block.SIZE);

    private Vector2 initialPosition;

    public Leaf(Vector2 topLeftCorner) {
        super(topLeftCorner, LEAF_DIMS, LEAF_RENDERABLE);
        this.physics().setMass(NO_MASS);
        this.initialPosition = topLeftCorner;
        startCycle();
    }
    public void startCycle(){
        this.setVelocity(Vector2.ZERO);
        this.renderer().setOpaqueness(1);
        this.setTopLeftCorner(this.initialPosition);
        new ScheduledTask(this, Leaf.timeRandomGenerator.nextInt(WAITING_BOUND),
                false, this::setLeafToFall);

    }
    private void setLeafToFall(){
        this.setVelocity(FALLING_VELOCITY);
        this.renderer().fadeOut(FADEOUT_TIME, this::endCycle);
        new Transition<>(
                this, //the game object being changed
                angle -> this.renderer().setRenderableAngle(angle),
                INITIAL_ROTATE_TRANSITION_VALUE, //initial transition value
                FINAL_ROTATE_TRANSITION_VALUE, //final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, //use a cubic interpolator
                10, //transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }
    private void endCycle(){
        new ScheduledTask(this, Leaf.timeRandomGenerator.nextInt(WAITING_BOUND),
                false, this::startCycle);
    }
    @Override
    public void onCollisionEnter(GameObject other, Collision collision){
        setVelocity(Vector2.ZERO);
    }
}
