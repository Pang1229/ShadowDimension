import bagel.Image;
import bagel.util.Point;
public class Wall extends Obstacle{
    private final Image WALL = new Image("res/wall.png");

    /**
     * Constructor for Wall class
     * @param startX the x value of the position for wall
     * @param startY the y value of the position for wall
     */
    public Wall(int startX, int startY){
        obstacleImage = WALL;
        this.position = new Point(startX, startY);
    }
}
