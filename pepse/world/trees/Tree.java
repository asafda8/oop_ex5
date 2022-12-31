package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

public class Tree{
    private HashMap<Integer, Boolean> hashMap = new HashMap<>();
    private Function<Float,
            Float> function;
    private Random random = new Random();
    private GameObjectCollection gameObjects;
    private static final Color STAMP_COLOR = new Color(100, 50, 20);
    private static final RectangleRenderable STAMP_RENDERABLE = new RectangleRenderable(STAMP_COLOR);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    public Tree(GameObjectCollection gameObjects, Function<Float,
            Float> function) {
        this.function = function;
        this.gameObjects = gameObjects;
    }
    public void createInRange(int minX, int maxX) {
        int startXCoord = (int) Math.floor(minX / (double) Block.SIZE) * Block.SIZE;
        int endXCoord = (int) Math.ceil(maxX / (double)Block.SIZE) * Block.SIZE;
        for(int xCoord = startXCoord; xCoord <= endXCoord; xCoord+=Block.SIZE){
                Boolean shouldCreateTree;
                if(hashMap.containsKey(xCoord)){
                    shouldCreateTree = hashMap.get(xCoord);
                }
                else{
                     shouldCreateTree = (random.nextInt(10) == 1);
                     hashMap.put(xCoord, shouldCreateTree);
                }
                if (shouldCreateTree){
                    createSingleTree(xCoord);
                }

        }
    }
    private void createSingleTree(int xCoord){
        Float yCoord =
                (float) Math.floor(this.function.apply((float) xCoord) / Block.SIZE) * Block.SIZE;
        int factor = random.nextInt(5, 8);
        for(Float currentY = yCoord; currentY > yCoord - Block.SIZE * factor; currentY-=Block.SIZE ){
            gameObjects.addGameObject(new Block(new Vector2(xCoord, currentY), STAMP_RENDERABLE));
        }
    }

}
