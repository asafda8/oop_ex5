package pepse.world;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.powerups.PowerupRandomFactory;
import pepse.util.ColorSupplier;
import pepse.util.Utils;
import pepse.world.trees.Tree;
import pepse.util.Noise;
import java.awt.*;
import java.lang.Math;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.floor;
import static java.lang.Math.max;

public class Terrain {
    private static final Random  random = new Random();
    private static final float PERLIN_NOISE_MULTIPLICATION_FACTOR = 200;
    private static final float PERLIN_NOISE_DIVISION_FACTOR = 20;
    private static final int TERRAIN_DEPTH = 20;
    private static final int INITIAL_VALUE_FOR_LOOP = 0;
    private static final float WINDOW_DIMENSION_FACTOR = 2 / 3f;
    private HashMap<Integer, LinkedList<GameObject>> objectsMap;
    private Tree tree;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static double STARTING_X_FACTOR = 2/3.0;
    private GameObjectCollection gameObjects;
    private float groundHeightAtX0;
    private int groundLayer;
    private Vector2 windowDimensions;
    private int seed;
    private Noise perlinNoise;
    private Renderable groundRenderable;
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
    public float groundHeightAt(float x) { return groundHeightAtX0 + PERLIN_NOISE_MULTIPLICATION_FACTOR *
             perlinNoise.noise(x / PERLIN_NOISE_DIVISION_FACTOR);}
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
