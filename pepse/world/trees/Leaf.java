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
    private static final Float TRANSITION_FACTOR_FOR_DIMENSIONS = 1.05f;
    private static final Float ANGLE_DIFFERENCE = 5f;
    private static final Float INITIAL_ROTATE_TRANSITION_VALUE = -5f;
    private static final Float FINAL_ROTATE_TRANSITION_VALUE = 5f;
    private static final int BOUND_FOR_RANDOM_WAITING_TO_ADD = 5;
    private final static Vector2 FALLING_VELOCITY = new Vector2(0, 10);
    private final static int WAITING_BOUND = 100;
    private static final int TRANSITION_TIME_FOR_ANGLE = 1;
    private static final float TRANSITION_TIME_FOR_DIMENSIONS = .1f;
    private final static int FADEOUT_TIME = 10;
    private static final float NO_MASS = 0;
    private static final int FULL_OPAQUENESS = 1;
    private static final int TRANSITION_TIME_TO_FALL = 10;
    private static final Float FACTOR_FOR_ANGLE_CALCULATION = 1.5f;
    private static final Float MAXIMUM_ANGLE = 5f;
    private static final Random randomGenerator = new Random();
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
        new ScheduledTask(this,
                randomGenerator.nextFloat() +
                        randomGenerator.nextInt(BOUND_FOR_RANDOM_WAITING_TO_ADD),
                false, this::rockLeaf);
        this.setVelocity(Vector2.ZERO);
        this.renderer().setOpaqueness(FULL_OPAQUENESS);
        this.setTopLeftCorner(this.initialPosition);
        new ScheduledTask(this, Leaf.randomGenerator.nextInt(WAITING_BOUND),
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
                TRANSITION_TIME_TO_FALL, //transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    private void rockLeaf() {
        float angle = Math.max(Math.abs((float) randomGenerator.nextGaussian() *
                FACTOR_FOR_ANGLE_CALCULATION), MAXIMUM_ANGLE);
        new Transition<Float>(
                this,
                this.renderer()::setRenderableAngle,
                angle,
                angle-ANGLE_DIFFERENCE,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                TRANSITION_TIME_FOR_ANGLE,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
        new Transition<Vector2>(
                this,
                this::setDimensions,
                this.getDimensions(),
                this.getDimensions().multX(TRANSITION_FACTOR_FOR_DIMENSIONS),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                TRANSITION_TIME_FOR_DIMENSIONS,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }
    private void endCycle(){
        new ScheduledTask(this, Leaf.randomGenerator.nextInt(WAITING_BOUND),
                false, this::startCycle);
    }
    @Override
    public void onCollisionEnter(GameObject other, Collision collision){
        setVelocity(Vector2.ZERO);
    }
}
