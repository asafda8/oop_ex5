package pepse.world.trees;

/**
 * enum representing status of a tree
 * in some coordinate
 * doesntExist - tree shouldn't be created in this coordinate
 * notActivate - tree should be created when the coordinate is in camera
 * Activate - tree is created and in sight
 */
public enum TreeStatus {
    doesntExist, notActive, Active;
}
