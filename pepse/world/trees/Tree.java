package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Vector2;
import pepse.util.Utils;
import pepse.world.Block;

import java.util.HashMap;
import java.util.LinkedList;
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
    private static final int TREE_STAMP_LAYER = -51;
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
    private HashMap<Integer, TreeStatus> coordinateToStatus = new HashMap<>();
    /**
     * hash maps that maps between coordinate and tree size
     */
    private HashMap<Integer, Integer> treeSizeInCoordinate = new HashMap<>();
    private HashMap<Integer, LinkedList<Leaf>> coordinateToLeaves = new HashMap<>();
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
                if(!coordinateToStatus.containsKey(xCoord)){
                    if (random.nextInt(BOUND_FOR_RANDOMIZING) ==
                            VALUE_FOR_PUTTING_TREE){
                        coordinateToStatus.put(xCoord, TreeStatus.notActive);
                    }
                    else{
                        coordinateToStatus.put(xCoord, TreeStatus.doesntExist);
                    }
                }
                if (coordinateToStatus.get(xCoord) == TreeStatus.notActive){
                    createSingleTree(xCoord);
                    coordinateToStatus.put(xCoord, TreeStatus.Active);
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
                        factor), TREE_STAMP_LAYER);
        generateLeavesForTree(xCoord, yCoord - factor * Block.SIZE);
    }

    /**
     * generates the leaves for the tree
     * @param xCoordOfTree: coordinate of the tree
     * @param topOfTree: top coordinate for the tree
     */
    private void generateLeavesForTree(int xCoordOfTree, int topOfTree){
        coordinateToLeaves.put(xCoordOfTree, new LinkedList<>());
        for(int x = xCoordOfTree - NUMBER_OF_LEAVES_COLS_PER_TREE * Block.SIZE / NUMBER_OF_SIDES;
            x <= xCoordOfTree + NUMBER_OF_LEAVES_COLS_PER_TREE * Block.SIZE / NUMBER_OF_SIDES;
            x+= Block.SIZE){
            for(int y = topOfTree - NUMBER_OF_LEAVES_COLS_PER_TREE * Block.SIZE / NUMBER_OF_SIDES;
                y <= topOfTree + NUMBER_OF_LEAVES_COLS_PER_TREE * Block.SIZE / NUMBER_OF_SIDES;
                y+= Block.SIZE){
                Leaf leaf = new Leaf(new Vector2(x,y));
                gameObjects.addGameObject(leaf, LEAF_LAYER);
                gameObjects.layers().shouldLayersCollide(Tree.LEAF_LAYER, Layer.STATIC_OBJECTS,
                        true);
                coordinateToLeaves.get(xCoordOfTree).push(leaf);
            }
        }
    }
    public void removeInRange(float minX, float maxX){
        if(minX >= maxX){
            return;
        }
        for(GameObject object: gameObjects.objectsInLayer(TREE_STAMP_LAYER)){
            if(minX <= object.getCenter().x() && maxX >= object.getCenter().x()){
                gameObjects.removeGameObject(object, TREE_STAMP_LAYER);
                coordinateToStatus.put((int)object.getTopLeftCorner().x(), TreeStatus.notActive);
                for(Leaf leaf : coordinateToLeaves.get((int)object.getTopLeftCorner().x())){
                    gameObjects.removeGameObject(leaf, LEAF_LAYER);
                }
            }
        }
    }

}
