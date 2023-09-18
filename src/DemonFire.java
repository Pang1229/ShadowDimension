import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Objects;

public class DemonFire {
    private final Image DEMON_FIRE = new Image("res/demon/demonFire.png");
    private static final Image PLAYER = new Image("res/fae/faeLeft.png");
    private static final Image DEMON = new Image("res/demon/demonLeft.png");
    private static final Image NAVEC = new Image("res/navec/navecLeft.png");
    private static final double P_HEIGHT = PLAYER.getHeight();
    private static final double P_WIDTH = PLAYER.getWidth();
    private static final double D_HEIGHT = DEMON.getHeight();
    private static final double D_WIDTH = DEMON.getWidth();
    private static final double N_HEIGHT = NAVEC.getHeight();
    private static final double N_WIDTH = NAVEC.getWidth();
    private static final double FIND_CENTER = 0.5;
    private static final int TOP_LEFT = 1;
    private static final int BOTTOM_LEFT = 2;
    private static final int TOP_RIGHT = 3;
    private static final int BOTTOM_RIGHT = 4;
    private final Point position;

    /**
     * Constructor for DemonFire class
     * @param startX the x value of start position for fire
     * @param startY the y value of start position for fire
     */
    public DemonFire(int startX, int startY){
        this.position = new Point(startX, startY);
    }

    /**
     * This method is used to determine the rotation of fire we need to print
     * @param enemy the x value of print position for fire
     * @param player the y value of print position for fire
     * @param type the type of enemy
     * @return an int attribute to determine the quadrant which fire is in
     */
    public static int checkRotation(Enemy enemy, Player player, String type){
        double demonX;
        double demonY;
        double playerX;
        double playerY;
        // for different kinds of enemies
        if(Objects.equals(type, "DEMON")) {
            demonX = enemy.getPosition().x + FIND_CENTER * D_WIDTH;
            demonY = enemy.getPosition().y + FIND_CENTER * D_HEIGHT;
        }
        else{
            demonX = enemy.getPosition().x + FIND_CENTER*N_WIDTH;
            demonY= enemy.getPosition().y + FIND_CENTER*N_HEIGHT;
        }
        playerX = player.getPosition().x + FIND_CENTER * P_WIDTH;
        playerY = player.getPosition().y + FIND_CENTER * P_HEIGHT;

        // calculation part
        if(playerX<=demonX && playerY<=demonY){
            return TOP_LEFT;
        } else if (playerX<=demonX) {
            return BOTTOM_LEFT;
        }else if (playerY<=demonY) {
            return TOP_RIGHT;
        }else{
            return BOTTOM_RIGHT;
        }
    }

    /**
     * This method is used to return the rectangle of fire
     * @return a rectangle for fire
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, DEMON_FIRE.getWidth(), DEMON_FIRE.getHeight());
    }

    /**
     * Method that checks for collisions between Fae and enemies' fire
     * and perform corresponding actions.
     * @param player is Fae's Player type variable which we control.
     * @param type the kind of enemy
     */
    public void checkFireCollisions(Player player, String type){
        Rectangle faeBox = new Rectangle(player.getPosition(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());
        Rectangle fireBox = this.getBoundingBox();

        // We need to check if Fae is invincible first,
        // and then check if enemy can hit Fae to reduce her HP
        if (faeBox.intersects(fireBox)) {
            if (!player.getIfInv()) {
                player.setPrevHealthPoints(player.getHealthPoints());
                if(Objects.equals(type, "DEMON")) {
                    player.setHealthPoints(Math.max(player.getHealthPoints() - Demon.getDamagePoints(), 0));
                    player.moveBack();
                    System.out.println("Demon inflicts " + Demon.getDamagePoints() + " damage points on Fae. " +
                            "Fae's current health: " + player.getHealthPoints() + "/" + Player.getMaxHealthPoints());
                }
                else{
                    player.setHealthPoints(Math.max(player.getHealthPoints() - Navec.getDamagePoints(), 0));
                    player.moveBack();
                    System.out.println("Navec inflicts " + Navec.getDamagePoints() + " damage points on Fae. " +
                            "Fae's current health: " + player.getHealthPoints() + "/" + Player.getMaxHealthPoints());
                }
            }
        }
    }
}