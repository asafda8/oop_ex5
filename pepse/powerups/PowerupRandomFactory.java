package pepse.powerups;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Vector2;
import pepse.world.Avatar;

import java.util.Random;

public class PowerupRandomFactory {
    private static final String[] POWER_UPS = {"faster", "enlarge"};
    private static final Random random = new Random();
    public static Powerup getRandomPowerup(Vector2 topLeftCorner, Vector2 dimensions,
                                            ImageReader imageReader, Avatar avatar
            , GameObjectCollection gameObjects){
        String powerName = POWER_UPS[random.nextInt(POWER_UPS.length)];
        Powerup powerup;
        switch (powerName) {
            case "faster":
                powerup = new FasterPowerup(topLeftCorner, dimensions, imageReader, avatar, gameObjects);
                break;
            case "enlarge":
                powerup = new EnlargePowerup(topLeftCorner, dimensions, imageReader, avatar, gameObjects);
                break;
            default:
                powerup = null;
                break;
        }
        return powerup;
    }
}
