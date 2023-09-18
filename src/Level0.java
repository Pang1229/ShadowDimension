import bagel.*;
import bagel.util.Rectangle;


public class Level0 extends Level{
    private static final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private boolean hasStarted;
    private boolean gameOver;
    private boolean playerWin;
    private boolean levelUp ;

    /**
     * Constructor for Level0 class
     */
    public Level0(){
        this.hasStarted= false;
        this.gameOver = false;
        this.playerWin = false;
        this.levelUp = false;
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
            drawStartScreen();
            if (input.wasPressed(Keys.SPACE)){
                hasStarted = true;
            }
        }

        if (gameOver){
            drawMessage(END_MESSAGE);

        }
        else if (playerWin) {
            levelUp = true;
        }

        // game is running
        if (hasStarted && !gameOver && !playerWin){
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            for(Wall current: getWalls()){
                current.update();
            }
            for(Sinkhole current: getLevel0Sinkholes()){
                current.update();
            }
            getLevel0Player().update(input, gameObject);

            if (getLevel0Player().isDead()){
                gameOver = true;
            }

            if (getLevel0Player().reachedGate()){
                playerWin = true;
                levelUp = true;
            }
        }
    }

    /**
     * Method that checks for collisions between Fae and the other entities, and performs
     * corresponding actions.
     * @param player is Fae's Player type variable which we control.
     */
    // In level 0, we only need to check collisions for walls and sinkholes
    public void checkCollisions(Player player){
        Rectangle faeBox = new Rectangle(player.getPosition(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());
        for (Wall current : getWalls()){
            Rectangle wallBox = current.getBoundingBox();
            if (faeBox.intersects(wallBox)){
                player.moveBack();
            }
        }

        for (Sinkhole hole : getLevel0Sinkholes()){
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
     * Method that return a boolean variable
     * @return levelUp is used to check if level0 is finished
     */
    public boolean ifNext(){
        return levelUp;
    }

}
