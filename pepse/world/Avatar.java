package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.*;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.security.Key;

public class Avatar extends GameObject{
    private static final float MOVE_SPEED = 100f;
    private static final float JUMP_HEIGHT = 4000f;
    private static final float MAX_ENERGY = 100f;
    private static final float FLIGHT_SPEED = 200f;
    private static final float GRAVITY = 3000f;
    private static ImageRenderable standingImage;
    private static ImageRenderable runningImage1;
    private static ImageRenderable runningImage2;
    private final GameObject energuLevelIndicator;
    private final TextRenderable textRenderable;
    private final AnimationRenderable walkingAnimation;
    private float energyLevel = MAX_ENERGY;
    private UserInputListener inputListener;

    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.textRenderable = new TextRenderable(String.valueOf(energyLevel));
        this.energuLevelIndicator = new GameObject(Vector2.ZERO, new Vector2(50, 50), textRenderable);
        gameObjects.addGameObject(energuLevelIndicator, Layer.UI);

        walkingAnimation = new AnimationRenderable(new Renderable[]{runningImage1, runningImage2}, 0.2);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 velocity = Vector2.ZERO;
        // run left
        if (this.inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            velocity = velocity.add(Vector2.LEFT.mult(MOVE_SPEED));
            this.renderer().setRenderable(walkingAnimation);
            this.renderer().setIsFlippedHorizontally(true);
        }
        // run right
        if (this.inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            velocity = velocity.add(Vector2.RIGHT.mult(MOVE_SPEED));
            this.renderer().setRenderable(walkingAnimation);
            this.renderer().setIsFlippedHorizontally(false);
        }
        // jump
        if ( this.getVelocity().y() == 0 && this.inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            velocity = velocity.add(Vector2.UP.multY(JUMP_HEIGHT));
//            new Transition<Vector2>(
//                    this,
//                    this.transform()::setVelocity,
//                    velocity,
//                    velocity.add(Vector2.UP.multY(JUMP_HEIGHT)),
//                    Transition.LINEAR_INTERPOLATOR_VECTOR,
//                    0.3f,
//                    Transition.TransitionType.TRANSITION_ONCE,
//                    null);
        }
        // fly
        if ( energyLevel > 0 &&
                this.inputListener.isKeyPressed(KeyEvent.VK_SHIFT) &&
                this.inputListener.isKeyPressed(KeyEvent.VK_SPACE) ) {
            velocity = velocity.add( Vector2.UP.multY(FLIGHT_SPEED) );
            energyLevel = Math.max(energyLevel - 0.5f, 0);
        }
        // stand (animation)
        if (velocity.x() == 0 && velocity.y() == 0) {
            this.renderer().setRenderable(standingImage);
        }
        // rest
        if (isResting())
            energyLevel = Math.min(MAX_ENERGY, energyLevel + 0.5f);

        transform().setAccelerationY(GRAVITY);
        this.transform().setVelocity(velocity);
        textRenderable.setString(String.valueOf(energyLevel));
    }

    private boolean isResting() {
        return getVelocity().y() == 0;
    }

    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                ImageReader imageReader, UserInputListener inputListener) {
        initImages(imageReader);
        
        Avatar avatar = new Avatar(
                topLeftCorner, new Vector2(30, 50), standingImage, inputListener, gameObjects
        );
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    private static void initImages(ImageReader imageReader) {
        String basePath = new File("").getAbsolutePath();
        String standingImagePath = basePath.concat("\\assets\\standing.png");
        String runningImagePath1 = basePath.concat("\\assets\\running1.png");
        String runningImagePath2 = basePath.concat("\\assets\\running2.png");

        standingImage = imageReader.readImage(standingImagePath, true);
        runningImage1 = imageReader.readImage(runningImagePath1, true);
        runningImage2 = imageReader.readImage(runningImagePath2, true);
    }
}
