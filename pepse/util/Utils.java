package pepse.util;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

public class Utils {
    public static void removeInLayerAndRange(float minX, float maxX, int layer,
                                             GameObjectCollection gameObjects){
        if(minX >= maxX){
            return;
        }
        for(GameObject object: gameObjects.objectsInLayer(layer)){
            if(minX <= object.getCenter().x() && maxX >= object.getCenter().x()){
                gameObjects.removeGameObject(object, layer);
            }
        }
    }

}
