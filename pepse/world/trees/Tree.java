package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

public class Tree{
    private static final int NUMBER_OF_SIDES = 2;
    private static final int LOWER_BOUND_FOR_NUMBER_OF_BLOCKS = 5;
    private static final int UPPER_BOUND_FOR_NUMBER_OF_BLOCKS = 8;
    private static final int LEAF_LAYER = -50;
    private static final int NUMBER_OF_LEAVES_ROWS_PER_TREE = 4;
    private static final int NUMBER_OF_LEAVES_COLS_PER_TREE = 4;
    private HashMap<Integer, Boolean> hashMap = new HashMap<>();
    private Function<Float,
            Float> function;
    private Random random = new Random();
    private GameObjectCollection gameObjects;

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
        int yCoord =
                (int) (Math.floor(this.function.apply((float) xCoord) / Block.SIZE) * Block.SIZE);
        int factor = random.nextInt(LOWER_BOUND_FOR_NUMBER_OF_BLOCKS, UPPER_BOUND_FOR_NUMBER_OF_BLOCKS);
        gameObjects.addGameObject(new TreeStamp(new Vector2(xCoord, yCoord - factor * Block.SIZE), factor),
                Layer.BACKGROUND);
        generateLeavesForTree(xCoord, yCoord - factor * Block.SIZE);

    }
    private void generateLeavesForTree(int xCoordOfTree, int topOfTree){
        for(int x = xCoordOfTree - NUMBER_OF_LEAVES_COLS_PER_TREE * Block.SIZE / NUMBER_OF_SIDES;
            x <= xCoordOfTree + NUMBER_OF_LEAVES_COLS_PER_TREE * Block.SIZE / NUMBER_OF_SIDES;
            x+= Block.SIZE){
            for(int y = topOfTree - NUMBER_OF_LEAVES_COLS_PER_TREE * Block.SIZE / NUMBER_OF_SIDES;
                y <= topOfTree + NUMBER_OF_LEAVES_COLS_PER_TREE * Block.SIZE / NUMBER_OF_SIDES;
                y+= Block.SIZE){
                gameObjects.addGameObject(new Leaf(new Vector2(x,y)), LEAF_LAYER);
                gameObjects.layers().shouldLayersCollide(Tree.LEAF_LAYER, Layer.STATIC_OBJECTS,
                        true);
            }
        }
    }

}
