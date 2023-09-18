import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sinkhole {
    private final Image SINKHOLE = new Image("res/sinkhole.png");
    private final static int DAMAGE_POINTS = 30;
    private final Point position;
    private boolean isActive;

    /**
     * Constructor for Sinkhole class
     * @param startX the x value of the position for hole
     * @param startY the y value of the position for hole
     */
    public Sinkhole(int startX, int startY){
        this.position = new Point(startX, startY);
        this.isActive = true;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        if (isActive){
            SINKHOLE.drawFromTopLeft(this.position.x, this.position.y);
        }
    }

    /**
     * Method that return the bounding box(type of Rectangle) of this sinkhole
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, SINKHOLE.getWidth(), SINKHOLE.getHeight());
    }

    /**
     * @return DAMAGE_POINTS which tells us the damage point of a sinkhole
     */
    public int getDamagePoints(){
        return DAMAGE_POINTS;
    }

    /**
     * Method that checks if hole hasn't met Fae
     * @return isActive: a boolean value which tells us if hole exists.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Method that sets if hole still exists
     * @param active a boolean value
     */
    public void setActive(boolean active) {
        isActive = active;
    }
}
