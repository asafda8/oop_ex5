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

/**
 * class representing a gameobject of a leaf
 */
public class Leaf extends GameObject {
    /**
     * factor for getting the last value of dimensions
     */
    private static final Float TRANSITION_FACTOR_FOR_DIMENSIONS = 1.05f;
    /**
     * difference between angles of the leaves
     */
    private static final Float ANGLE_DIFFERENCE = 5f;
    /**
     * initial value for rotation transition
     */
    private static final Float INITIAL_ROTATE_TRANSITION_VALUE = -5f;
    /**
     * final value for rotation transition
     */
    private static final Float FINAL_ROTATE_TRANSITION_VALUE = 5f;
    /**
     * bound for waiting between rocking leaves
     */
    private static final int BOUND_FOR_RANDOM_WAITING_TO_ADD = 5;
    /**
     * velocity of the fall of the leaves
     */
    private final static Vector2 FALLING_VELOCITY = new Vector2(0, 10);
    /**
     * bound for waiting when leaves fall
     */
    private final static int WAITING_BOUND = 100;
    /**
     * transition time for leaves between angles
     */
    private static final int TRANSITION_TIME_FOR_ANGLE = 1;
    /**
     * transition time for leaves between dimensions
     */
    private static final float TRANSITION_TIME_FOR_DIMENSIONS = .1f;
    /**
     * time for a leaf to fade out
     */
    private final static int FADEOUT_TIME = 10;
    /**
     * mass value of the leaf
     */
    private static final float NO_MASS = 0;
    /**
     * opaqueness value when creating the leaf
     */
    private static final int FULL_OPAQUENESS = 1;
    /**
     * transition time when the leaves fall
     */
    private static final int TRANSITION_TIME_TO_FALL = 10;
    /**
     * factor to multiply gaussian when calculating angle
     */
    private static final Float FACTOR_FOR_ANGLE_CALCULATION = 1.5f;
    /**
     * maximum angle for the leaf
     */
    private static final Float MAXIMUM_ANGLE = 5f;
    /**
     * generator for randomness
     */
    private static final Random randomGenerator = new Random();
    /**
     * color of the leaf
     */
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    /**
     * renderable to put on the leaf
     */
    private static final RectangleRenderable LEAF_RENDERABLE = new RectangleRenderable(LEAF_COLOR);
    /**
     * dimensions of the leaf
     */
    private static final Vector2 LEAF_DIMS = new Vector2(Block.SIZE, Block.SIZE);
    /**
     * initial position of the leaf
     */

    private Vector2 initialPosition;

    /**
     * constructor for the leaf
     * @param topLeftCorner: position of the leaf
     */

    public Leaf(Vector2 topLeftCorner) {
        super(topLeftCorner, LEAF_DIMS, LEAF_RENDERABLE);
        this.physics().setMass(NO_MASS);
        this.initialPosition = topLeftCorner;
        startCycle();
    }

    /**
     * start cycle of the life of the leaf
     */
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

    /**
     * set leaf to fall down
     */
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

    /**
     * set leaf to rock
     */
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

    /**
     * end cycle of falling
     */
    private void endCycle(){
        new ScheduledTask(this, Leaf.randomGenerator.nextInt(WAITING_BOUND),
                false, this::startCycle);
    }

    /**
     * upon collision makes the leaf stay
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision){
        setVelocity(Vector2.ZERO);
    }
}
