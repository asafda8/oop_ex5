package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;

/**
 * class resposible for creating trees
 */
public class Tree{
    /**
     * value for comparing to put the tree
     */
    private static final int VALUE_FOR_PUTTING_TREE = 1;
    /**
     * bound for randomizing and put the tree
     */
    private static final int BOUND_FOR_RANDOMIZING = 10;
    /**
     * number of sides of the top of the tree (left and right, top and bottom)
     */
    private static final int NUMBER_OF_SIDES = 2;
    /**
     * lower bound of blocks for the tree stamp
     */
    private static final int LOWER_BOUND_FOR_NUMBER_OF_BLOCKS = 5;
    /**
     * upper bound of blocks for tree stamp
     */
    private static final int UPPER_BOUND_FOR_NUMBER_OF_BLOCKS = 8;
    /**
     * layer of the trees in leaves
     */
    private static final int LEAF_LAYER = -50;
    /**
     * number of leaves per row for a tree
     */
    private static final int NUMBER_OF_LEAVES_ROWS_PER_TREE = 4;
    /**
     * number of leaves per col for tree
     */
    private static final int NUMBER_OF_LEAVES_COLS_PER_TREE = 4;
    /**
     * hash maps that maps between a number and whether we should put a
     * tree in this number or not
     */
    private HashMap<Integer, Boolean> shouldPutTree = new HashMap<>();
    /**
     * hash maps that maps between coordinate and tree size
     */
    private HashMap<Integer, Integer> treeSizeInCoordinate = new HashMap<>();
    /**
     * function to get height in of ground in x coordinate
     */
    private Function<Float,
            Float> function;
    /**
     * random object
     */
    private Random random = new Random();
    /**
     * objects of the game
     */
    private GameObjectCollection gameObjects;

    /**
     * constructor of the Tree class
     * @param gameObjects: objects of the game
     * @param function: function for getting height
     */
    public Tree(GameObjectCollection gameObjects, Function<Float,
            Float> function) {
        this.function = function;
        this.gameObjects = gameObjects;
    }

    /**
     * creates the trees in the given range
     * @param minX: minimum x to create tree in
     * @param maxX: maximum x to create tree in
     */
    public void createInRange(int minX, int maxX) {
        int startXCoord = (int) Math.floor(minX / (double) Block.SIZE) * Block.SIZE;
        int endXCoord = (int) Math.ceil(maxX / (double)Block.SIZE) * Block.SIZE;
        for(int xCoord = startXCoord; xCoord <= endXCoord; xCoord+=Block.SIZE){
                Boolean shouldCreateTree;
                if(shouldPutTree.containsKey(xCoord)){
                    shouldCreateTree = shouldPutTree.get(xCoord);
                }
                else{
                     shouldCreateTree = (random.nextInt(BOUND_FOR_RANDOMIZING) ==
                             VALUE_FOR_PUTTING_TREE);
                     shouldPutTree.put(xCoord, shouldCreateTree);
                }
                if (shouldCreateTree){
                    createSingleTree(xCoord);
                }

        }
    }

    /**
     * creates a single tree
     * @param xCoord: coordinate for the tree
     */
    private void createSingleTree(int xCoord){
        int yCoord =
                (int) (Math.floor(this.function.apply((float) xCoord) / Block.SIZE) * Block.SIZE);
        if(!treeSizeInCoordinate.containsKey(xCoord)){
            treeSizeInCoordinate.put(xCoord, random.nextInt(LOWER_BOUND_FOR_NUMBER_OF_BLOCKS,
                    UPPER_BOUND_FOR_NUMBER_OF_BLOCKS));
        }
        int factor = treeSizeInCoordinate.get(xCoord);
        gameObjects.addGameObject(new TreeStamp(new Vector2(xCoord, yCoord - factor * Block.SIZE),
                        factor), Layer.BACKGROUND);
        generateLeavesForTree(xCoord, yCoord - factor * Block.SIZE);
    }

    /**
     * generates the leaves for the tree
     * @param xCoordOfTree: coordinate of the tree
     * @param topOfTree: top coordinate for the tree
     */
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
