import bagel.Image;
import bagel.util.Point;
public class Tree extends Obstacle{
    private final Image TREE = new Image("res/tree.png");

    /**
     * Constructor for Tree class
     * @param startX the x value of the position for tree
     * @param startY the y value of the position for tree
     */
    public Tree(int startX, int startY){
        obstacleImage = TREE;
        this.position = new Point(startX, startY);
    }
}
