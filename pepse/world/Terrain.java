package pepse.world;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Utils;
import pepse.world.trees.Tree;
import pepse.util.Noise;
import java.awt.*;
import java.lang.Math;
import java.util.HashMap;
import java.util.LinkedList;

import static java.lang.Math.floor;
import static java.lang.Math.max;

/**
 * class responsible for creating terrain
 */
public class Terrain {
    /**
     * factor for multiplying perlin noise for terrain
     * creation
     */
    private static final float PERLIN_NOISE_MULTIPLICATION_FACTOR = 200;
    /**
     * factor for dividing when calling perlin noise in
     * order to make terrain more smooth
     */
    private static final float PERLIN_NOISE_DIVISION_FACTOR = 20;
    /**
     * depth of the terrain in blocks
     */
    private static final int TERRAIN_DEPTH = 20;
    /**
     * initial value for creating blocks for terrain
     */
    private static final int INITIAL_VALUE_FOR_LOOP = 0;
    /**
     * factor for calculating ground at coordinate 0
     */
    private static final float WINDOW_DIMENSION_FACTOR = 2 / 3f;
    /**
     * maps between x coordinates and the blocks of terrains in coordinate
     */
    private HashMap<Integer, LinkedList<GameObject>> objectsMap;
    /**
     * class for creating trees
     */
    private Tree tree;
    /**
     *  color of the terrain
     */
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    /**
     * objects of the game
     */
    private GameObjectCollection gameObjects;
    /**
     * the height at x0
     */
    private float groundHeightAtX0;
    /**
     * thee layer of the ground
     */
    private int groundLayer;
    /**
     * perlin noise generator
     */
    private Noise perlinNoise;
    /**
     * renderable to give the ground
     */
    private Renderable groundRenderable;

    /**
     * constructor for terrain creator class
     * @param gameObjects: objects of the game
     * @param groundLayer: layer of the ground
     * @param windowDimensions: dimensions of the game window
     * @param seed: seed to give perlin noise generator
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.perlinNoise = new Noise((float) seed);
        groundRenderable = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        this.tree = new Tree(gameObjects, this::groundHeightAt);
        this.groundHeightAtX0 = windowDimensions.y() * (WINDOW_DIMENSION_FACTOR);
        objectsMap = new HashMap<>();
    }

    /**
     * calculates ground height at some position
     * @param x: x coordinate to calculate height in
     * @return: height in coordinate
     */
    public float groundHeightAt(float x) { return groundHeightAtX0 + PERLIN_NOISE_MULTIPLICATION_FACTOR *
             perlinNoise.noise(x / PERLIN_NOISE_DIVISION_FACTOR);}

    /**
     * creates objects in some range
     * @param minX: minimum x to create objects in
     * @param maxX: maximum x to create objects in
     */
    public void createInRange(int minX, int maxX) {
        if(minX >= maxX){
            return;
        }
        int startXCoord = (int) Math.floor(minX / (double)Block.SIZE) * Block.SIZE;
        int endXCoord = (int) Math.ceil(maxX / (double)Block.SIZE) * Block.SIZE;
        for(int xCoord = startXCoord; xCoord <= endXCoord; xCoord+=Block.SIZE){
            int yCoord = (int) Math.ceil(groundHeightAt(xCoord) / Block.SIZE) * Block.SIZE;
            if (!objectsMap.containsKey(xCoord) || objectsMap.get(xCoord).isEmpty()){
                objectsMap.put(xCoord, new LinkedList<>());
                for(int i = INITIAL_VALUE_FOR_LOOP; i < TERRAIN_DEPTH; i ++){
                    Block block = new Block(new Vector2(xCoord, yCoord),groundRenderable);
                    gameObjects.addGameObject(block, groundLayer);
                    objectsMap.get(xCoord).push(block);
                    yCoord += Block.SIZE;
                }
            }

        }
        tree.createInRange(minX, maxX);
    }

    /**
     * removes objects in the range
     * @param minX: minimum x to remove objects in
     * @param maxX: maxmimum x to remove objects in
     */
    public void removeInRange(float minX, float maxX){
//        Utils.removeInLayerAndRange(minX, maxX, groundLayer, gameObjects);
        for(var key : objectsMap.entrySet()){
            if(key.getKey() >= minX && key.getKey() <= maxX){
                while(!key.getValue().isEmpty()){
                    gameObjects.removeGameObject(key.getValue().pop(), groundLayer);
                }
            }

        }
        tree.removeInRange(minX, maxX);
    }
}
