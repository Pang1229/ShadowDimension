import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

public class Player {
    private final static int LEVEL0 = 0;
    private final static int LEVEL1 = 1;
    private final Image FAE_R = new Image("res/fae/faeRight.png");
    private final Image FAE_L = new Image("res/fae/faeLeft.png");
    private final Image FAE_AR = new Image("res/fae/faeAttackRight.png");
    private final Image FAE_AL = new Image("res/fae/faeAttackLeft.png");
    private final static int MAX_HEALTH_POINTS = 100;
    private final static int DAMAGE_POINTS = 20;
    private final static double MOVE_SIZE = 2;
    private final static int WIN_X = 950;
    private final static int WIN_Y = 670;
    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;
    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;
    private final static int FONT_SIZE = 30;
    private final static int COLD_DOWN = 120;
    private final static int ATTACK_TIME = 60;
    private final static int INVINCIBLE_TIME = 180;
    private final Font FONT = new Font("res/frostbite.ttf", FONT_SIZE);
    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);

    private Point position;
    private Point prevPosition;
    private int healthPoints;
    private int prevHealthPoints;
    private int attackTime = 0;
    private int cdTime = 0;
    private int invincibleTime = 0;
    private Image currentImage;
    private boolean facingRight;
    private boolean ifAttack;
    private boolean ifInv;
    private boolean ifPassCD;

    /**
     * Constructor for Player class
     * @param startX the x value of start position for player
     * @param startY the y value of start position for player
     */
    public Player(int startX, int startY){
        this.position = new Point(startX, startY);
        this.healthPoints = MAX_HEALTH_POINTS;
        this.currentImage = FAE_R;
        this.facingRight = true;
        this.ifAttack = false;
        this.ifInv = false;
        this.ifPassCD = true;
        COLOUR.setBlendColour(GREEN);
    }

    /**
     * Method that performs state update
     * @param input is the Key which has been pressed by user
     * @param gameObject is an instance of shadowDimension class we created
     */
    public void update(Input input, ShadowDimension gameObject){
        if (input.isDown(Keys.UP)){
            setPrevPosition();
            move(0, -MOVE_SIZE);
        } else if (input.isDown(Keys.DOWN)){
            setPrevPosition();
            move(0, MOVE_SIZE);
        } else if (input.isDown(Keys.LEFT)){
            setPrevPosition();
            move(-MOVE_SIZE,0);
            if (facingRight) {
                this.currentImage = FAE_L;
                facingRight = false;
            }
        } else if (input.isDown(Keys.RIGHT)){
            setPrevPosition();
            move(MOVE_SIZE,0);
            if (!facingRight) {
                this.currentImage = FAE_R;
                facingRight = true;
            }
        }

        //attacking model, when user presses A and Fae is not in CD time
        if (input.isDown(Keys.A) && getIfPassCD()) {
            setIfAttack(true);
        }

        //Two methods to check if fae is attacking or invincible status
        ifAttack();
        ifInv();

        //checking collisions for 2 levels
        this.currentImage.drawFromTopLeft(position.x, position.y);
        if(!gameObject.getLevel0().ifNext()) {
            gameObject.getLevel0().checkCollisions(this);
            renderHealthPoints();
            gameObject.getLevel0().checkOutOfBounds(this,LEVEL0);
        }
        else{
            gameObject.getLevel1().checkCollisions(this);
            gameObject.getLevel1().checkDemonDamage(this);
            renderHealthPoints();
            gameObject.getLevel1().checkOutOfBounds(this,LEVEL1);
        }
    }


    /**
     * Method that stores Fae's previous position
     */
    private void setPrevPosition(){
        this.prevPosition = new Point(position.x, position.y);
    }

    /**
     * Method that stores Fae's previous health points
     * @param healthPoints an int variable which hasn't been changed before update
     */
    public void setPrevHealthPoints(int healthPoints){
        this.prevHealthPoints = healthPoints;
    }

    /**
     * Method that moves Fae back to previous position
     */
    public void moveBack(){
        this.position = prevPosition;
    }

    /**
     * Method that moves Fae given the direction
     * @param xMove the difference between new position's x value
     *             for player after moving and previous position's
     * @param yMove the difference between new position's y value
     *             for player after moving and previous position's
     */
    private void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        this.position = new Point(newX, newY);
    }

    /**
     * Method that renders the current health as a percentage on screen
     */
    private void renderHealthPoints(){
        double percentageHP = ((double) healthPoints/MAX_HEALTH_POINTS) * 100;
        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        }
        else{
            COLOUR.setBlendColour(GREEN);
        }
        FONT.drawString(Math.round(percentageHP) + "%", HEALTH_X, HEALTH_Y, COLOUR);
    }

    /**
     * Method that checks if Fae's health has depleted
     * @return a boolean value which tells us if Fae is dead.
     */
    public boolean isDead() {
        return healthPoints <= 0;
    }

    /**
     * Methods that check if Fae has found the gate(level0) or defeated Navec(level1)
     * @return a boolean value which tells us if Fae wins.
     */
    public boolean reachedGate(){
        return (this.position.x >= WIN_X) && (this.position.y >= WIN_Y);
    }
    public boolean defeatNave(Navec navec){return !(navec.isActive());}

    /**
     * @return position a Point attribute which tells us the position of Fae now
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @return currentImage an Image attribute which tells us
     * which image of Fae should be used now
     */
    public Image getCurrentImage() {
        return currentImage;
    }

    /**
     * @return healthPoints which tells us the HP of Fae now
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * @return prevHealthPoints which tells us the previous HP of Fae
     */
    public int getPrevHealthPoints() {
        return prevHealthPoints;
    }

    /**
     * @param healthPoints an int attribute to set the hp of Fae
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * @return MAX_HEALTH_POINTS which tells us the max HP of Fae
     */
    public static int getMaxHealthPoints() {
        return MAX_HEALTH_POINTS;
    }

    /**
     * @return DAMAGE_POINTS which tells us the damage point of Fae
     */
    public static int getDamagePoints() {
        return DAMAGE_POINTS;
    }

    /**
     * @return ifAttack which tells us if Fae is in attacking model
     */
    public boolean getIfAttack(){return ifAttack;}

    /**
     * @return ifInv which tells us if Fae is in invincible model
     */
    public boolean getIfInv(){return ifInv;}

    /**
     * @param status which will determine if Fae is in attacking model
     */
    public void setIfAttack(boolean status){ifAttack = status;}

    /**
     * @param status which will determine if Fae is in invincible model
     */
    public  void setIfInv(boolean status){ifInv = status;}

    /**
     * @return attackTime which tells us the attacking time of Fae
     */
    public int getAttackTime() {
        return attackTime;
    }

    /**
     * According to if Fae is attacking, to decide how to set the value of attackTime
     */
    public void setAttackTime() {
        if(attackTime <= ATTACK_TIME && getIfAttack()) {
            this.attackTime++;
        }
        else {
            this.attackTime = 0;
        }
    }

    /**
     * According to if Fae passes her CD, to decide how to set the value of cdTime
     */
    public void setCdTime() {
        if(cdTime <= COLD_DOWN && !getIfPassCD()) {
            this.cdTime++;
        }
        else {
            this.cdTime = 0;
            setIfPassCD(true);
        }
    }

    /**
     * @return invincibleTime which tells us the invincible time of Fae
     */
    public int getInvincibleTime() {
        return invincibleTime;
    }

    /**
     * According to if Fae is in invincible model, to decide how to set the value of invincibleTime
     */
    public void setInvincibleTime() {
        if(getInvincibleTime() <= INVINCIBLE_TIME && getIfInv()) {
            this.invincibleTime++;
        }
        else {
            this.invincibleTime = 0;
            setIfInv(false);
        }
    }

    /**
     * Change the image when Fae attacks
     */
    public void becomeAttacking(){
        if (this.currentImage == FAE_R) {
            this.currentImage = FAE_AR;
        } else if(this.currentImage == FAE_L) {
            this.currentImage = FAE_AL;
        }
    }

    /**
     * Change the image when Fae doesn't attack
     */
    public void becomeNormal(){
        if (this.currentImage == FAE_AR) {
            this.currentImage = FAE_R;
        } else if(this.currentImage == FAE_AL) {
            this.currentImage = FAE_L;
        }
    }

    /**
     * Check if Fae is attcking and change the attributes for Fae
     */
    public void ifAttack(){
        if(getIfAttack() && (getAttackTime() <= ATTACK_TIME)){
            becomeAttacking();
            setIfPassCD(false);
        }
        else {
            becomeNormal();
            setIfAttack(false);
            setCdTime();
        }
        setAttackTime();
    }

    /**
     * @return ifPassCD which tells us if Fae passes CD
     */
    public boolean getIfPassCD(){return ifPassCD;}

    /**
     * @param status reset ifPassCD of Fae
     */
    public void setIfPassCD(boolean status){ifPassCD = status;}

    /**
     * Check if Fae is invincible and change the attributes for Fae
     */
    public void ifInv(){
        int condition = getPrevHealthPoints()-getHealthPoints();
        if((condition == Demon.getDamagePoints() || condition == Navec.getDamagePoints()) && !getIfInv()){
            setIfInv(true);
        }
        setInvincibleTime();
    }
}
