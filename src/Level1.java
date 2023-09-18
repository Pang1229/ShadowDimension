import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Level1 extends Level{
    private static final Image BACKGROUND_IMAGE = new Image("res/background1.png");
    private static final Image NAVEC = new Image("res/navec/navecRight.png");
    private final static String END_MESSAGE = "GAME OVER!";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private static final double CHECK_BOUND = 0.5;
    private final static int INV_TIME = 180;
    private static boolean hasStarted;
    private static boolean gameOver;
    private static boolean playerWin;
    private static TimeScale timeScale;

    /**
     * Constructor for Level1 class
     */
    public Level1(){
        timeScale = new TimeScale();
    }

    /**
     * Performs a state update.
     * @param input is the Key which has been pressed by user
     * @param gameObject is an instance of shadowDimension class we created
     */
    public void update(Input input, ShadowDimension gameObject) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if(!hasStarted){
            if (input.wasPressed(Keys.SPACE) && gameObject.ifThreeSeconds()){
                hasStarted = true;
            }
        }

        if (gameOver){
            drawMessage(END_MESSAGE);

        } else if (playerWin) {
            drawMessage(WIN_MESSAGE);
        }

        // game is running
        if (hasStarted && !gameOver && !playerWin){
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            timeScale.setSpeed(getDemons()[0].move_size);
            if(input.wasPressed(Keys.L)){
                timeScale.update("L");
            } else if (input.wasPressed(Keys.K)) {
                timeScale.update("K");
            }

            for(Tree current: getTrees()){
                current.update();
            }
            for(Sinkhole current: getLevel1Sinkholes()){
                current.update();
            }
            for(Demon current: getDemons()){
                current.update();
                current.setMoveSize();
                if(current.isInv()){
                    current.setTime();
                }
            }
            for(Navec current: getNavec()) {
                current.update();
                current.setMoveSize();
                if(current.isInv()){
                    current.setTime();
                }
            }
            getLevel1Player().update(input, gameObject);

            if (getLevel1Player().isDead()){
                gameOver = true;
            }

            if (getLevel1Player().defeatNave(getNavec()[0])){
                playerWin = true;
            }
        }
    }

    /**
     * Method that checks for collisions between Fae and the other entities, and performs
     * corresponding actions.
     * @param player is Fae's Player type variable which we control.
     */
    // In level 1, we need to check collisions for trees and sinkholes
    public void checkCollisions(Player player){
        Rectangle faeBox = new Rectangle(player.getPosition(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());
        for (Tree current : getTrees()){
            Rectangle treeBox = current.getBoundingBox();
            if (faeBox.intersects(treeBox)){
                player.moveBack();
            }
        }

        for (Sinkhole hole : getLevel1Sinkholes()){
            Rectangle holeBox = hole.getBoundingBox();
            if (hole.isActive() && faeBox.intersects(holeBox)){
                player.setHealthPoints(Math.max(player.getHealthPoints() - hole.getDamagePoints(), 0));
                player.moveBack();
                hole.setActive(false);
                System.out.println("Sinkhole inflicts " + hole.getDamagePoints() + " damage points on Fae. " +
                        "Fae's current health: " + player.getHealthPoints() + "/" + Player.getMaxHealthPoints());
            }
        }
    }

    /**
     * Method that checks if damages point exists from Fae to two kinds of demons, and performs
     * corresponding actions.
     * @param player is Fae's Player type variable which we control.
     */
    // We need to check if the demon is still alive after take the damage from Fae
    // and print the log messages.
    public void checkDemonDamage(Player player) {
        Rectangle faeBox = new Rectangle(player.getPosition(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());
        for (Demon demon : getDemons()) {
            Rectangle navecBox = demon.getBoundingBox();
            if (demon.isActive() && faeBox.intersects(navecBox) && !demon.isInv() && player.getIfAttack()) {
                demon.setHealthPoints(Math.max(demon.getHealthPoints() - Player.getDamagePoints(), 0));

                if(demon.getTime() <= INV_TIME){
                    demon.setInv(true);
                }

                if(demon.getHealthPoints() <= 0) {
                    demon.setActive(false);
                }
                System.out.println("Player inflicts " + Player.getDamagePoints() + " damage points on Demon. " +
                        "Demon's current health: " + demon.getHealthPoints() + "/" + demon.getMaxHealthPoints("DEMON"));
            }
            else if(demon.isActive() && demon.isInv()){
                if(demon.getTime() > INV_TIME){
                    demon.setInv(false);
                    demon.resetTime();
                }
            }
        }

        for (Navec navec : getNavec()) {
            Rectangle navecBox = navec.getBoundingBox();
            if (navec.isActive() && faeBox.intersects(navecBox) && !navec.isInv() && player.getIfAttack()) {
                navec.setHealthPoints(Math.max(navec.getHealthPoints() - Player.getDamagePoints(), 0));

                if(navec.getTime() <= INV_TIME){
                    navec.setInv(true);
                }

                if(navec.getHealthPoints() <= 0) {
                    navec.setActive(false);
                }
                System.out.println("Player inflicts " + Player.getDamagePoints() + " damage points on Navec. " +
                        "Navec's current health: " + navec.getHealthPoints() + "/" + navec.getMaxHealthPoints("NAVEC"));
            }
            else if(navec.isActive() && navec.isInv()){
                if(navec.getTime() > INV_TIME){
                    navec.setInv(false);
                    navec.resetTime();
                }
            }
        }
    }

    /**
     * Method that return a boolean variable
     * @return isStart is used to check if this level starts
     */
    public boolean ifStart(){
        return hasStarted;
    }

    /**
     * Method that return a Player variable
     * @return player, a Player type variable which we use to represent Fae in level1
     */
    public static Player getPlayer() {
        return getLevel1Player();
    }

    /**
     * Method that checks for collisions between enemies and the other entities, and performs
     * corresponding actions.
     * @param enemy: might be demon or navec
     */
    public static void checkDCollisions(Enemy enemy){
        Rectangle demonBox = new Rectangle(enemy.getPosition(), enemy.getCurrentImage().getWidth(),
                enemy.getCurrentImage().getHeight());
        for (Tree current : getTrees()){
            Rectangle treeBox = current.getBoundingBox();
            if (demonBox.intersects(treeBox)){
                enemy.moveBack();
            }
        }

        for (Sinkhole hole : getLevel1Sinkholes()){
            Rectangle holeBox = hole.getBoundingBox();
            if (hole.isActive() && demonBox.intersects(holeBox)) {
                enemy.moveBack();
            }
        }
    }

    /**
     * Method that checks if Enemies have gone out-of-bounds and performs corresponding action
     * @param enemy: might be demon or navec
     */
    public static void checkDOutOfBounds(Enemy enemy){
        Point currentPosition = enemy.getPosition();
        if ((currentPosition.y > getBottomRight().y) || (currentPosition.y < getLevel1TopLeft().y)
                || (currentPosition.x < getLevel1TopLeft().x)
                || (currentPosition.x + CHECK_BOUND*NAVEC.getWidth() > getBottomRight().x)){
            enemy.moveBack();
        }
    }

    /**
     * Method that return a Timescale variable
     * @return timeScale is used to control timescale-function in level1
     */
    public static TimeScale getTimeScale(){return timeScale;}

}
