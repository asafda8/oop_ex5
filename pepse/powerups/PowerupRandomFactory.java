package pepse.powerups;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.util.Random;

/**
 * factor for powerups
 */
public class PowerupRandomFactory {
    /**
     * string representing the powerup
     * making avatar faster
     */
    private static final String FASTER_POWERUP = "faster";
    /**
     * string representing the powerup
     * making avatar bigger
     */
    private static final String ENLARGE_POWERUP = "enlarge";
    /**
     * available powerups
     */
    private static final String[] POWER_UPS = {FASTER_POWERUP, ENLARGE_POWERUP};
    /**
     * random variable to randomize powerup
     */
    private static final Random random = new Random();

    /**
     * creates a random powerup
     * @param topLeftCorner: position of the powerup
     * @param dimensions: dimensions of the powerup
     * @param imageReader: image reader
     * @param avatar: avater of the game
     * @param gameObjects: objects of the gmae
     * @return: random powerup
     */
    public static Powerup getRandomPowerup(Vector2 topLeftCorner, Vector2 dimensions,
                                            ImageReader imageReader, Avatar avatar
            , GameObjectCollection gameObjects){
        String powerName = POWER_UPS[random.nextInt(POWER_UPS.length)];
        Powerup powerup;
        switch (powerName) {
            case FASTER_POWERUP:
                powerup = new FasterPowerup(topLeftCorner, dimensions, imageReader, avatar, gameObjects);
                break;
            case ENLARGE_POWERUP:
                powerup = new EnlargePowerup(topLeftCorner, dimensions, imageReader, avatar, gameObjects);
                break;
            default:
                powerup = null;
                break;
        }
        return powerup;
    }
}
