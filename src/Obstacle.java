import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
public abstract class Obstacle {
    protected Image obstacleImage;
    protected Point position;
    /**
     * Method that performs state update
     */
    public void update() {
        obstacleImage.drawFromTopLeft(this.position.x, this.position.y);
    }

    /**
     * Method that return the bounding box of this obstacle
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, obstacleImage.getWidth(), obstacleImage.getHeight());
    }
}
