import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Objects;

public abstract class Enemy {
    private final Image NAVEC_R = new Image("res/navec/navecRight.png");
    private final Image DEMON = new Image("res/demon/demonRight.png");
    private final Font FONT = new Font("res/frostbite.ttf", FONT_SIZE);
    protected double move_size = (double)(Math.random() * 0.5 + 0.2);
    private final static DrawOptions COLOUR = new DrawOptions();
    protected final static DrawOptions ROTATION = new DrawOptions();
    private final static int MAX_NAVEC_HEALTH_POINTS = 80;
    private final static int MAX_DEMON_HEALTH_POINTS = 40;
    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;
    private final static int FONT_SIZE = 15;
    private final int DEMON_ATTACK_RANGE = 150;
    private final int NAVEC_ATTACK_RANGE = 200;
    protected static final int TOP_LEFT = 1;
    protected static final int BOTTOM_LEFT = 2;
    protected static final int TOP_RIGHT = 3;
    protected static final int ROTATION_BL = -90;
    protected static final int ROTATION_TR = -270;
    protected static final double HALF = 2.0;
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);
    protected int time = 0;
    private Point prevPosition;
    private int healthPoints;
    protected Image currentImage;
    private Point position;
    private boolean isActive;
    private boolean isAggressive;
    private boolean facingRight;
    private boolean isInv;
    private boolean isHorizontal;
    private boolean inRange;
    private boolean meetBound;
    private int prevHealthPoints;
    private TimeScale timeScale;

    /**
     * Constructor for Enemy class
     * @param startX the x value of start position for enemy
     * @param startY the y value of start position for enemy
     * @param type the kind of this enemy
     */
    public Enemy(int startX, int startY, String type) {
        if(Objects.equals(type, "NAVEC")) {
            this.healthPoints = MAX_NAVEC_HEALTH_POINTS;
        }
        else{
            this.healthPoints = MAX_DEMON_HEALTH_POINTS;
        }
        this.position = new Point(startX, startY);
        this.isActive = true;
        this.isInv = false;
        this.facingRight = true;
        this.meetBound= false;
    }
    /**
     * Method that performs state update
     * The exact implementation is determined by enemy's kind
     */
    public abstract void update();

    /**
     * Method that return the bounding box of this enemy
     * The exact implementation is determined by enemy's kind
     */
    public abstract Rectangle getBoundingBox();

    /**
     * Method that checks if enemy is still alive
     * @return isActive: a boolean value which tells us if enemy is alive.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Method that sets if enemy is alive
     * @param active a boolean value
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Method that checks if enemy is invincible
     * @return isInv: a boolean value which tells us if enemy is invincible.
     */
    public boolean isInv() {
        return isInv;
    }

    /**
     * Method that sets if enemy is invincible
     * @param active a boolean value
     */
    public void setInv(boolean active) {
        isInv = active;
    }

    /**
     * Method that sets enemy's hp value
     * @param healthPoints an int value
     */
    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    /**
     * Method that sets enemy's previous hp value
     */
    public void setPrevHealthPoints() {
        prevHealthPoints = healthPoints;
    }

    /**
     * Method that sets enemy's previous position
     */
    protected void setPrevPosition(){
        this.prevPosition = new Point(position.x, position.y);
    }

    /**
     * Method that gets enemy's hp value
     * @return healthPoints an int value
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Method that gets enemy's invincible time value
     * @return time an int value
     */
    public int getTime(){
        return time;
    }

    /**
     * Method that gets enemy's max hp value
     * @param type of enemy
     * @return max health points an int value
     */
    public int getMaxHealthPoints(String type) {
        if(type == "DEMON") {
            return MAX_DEMON_HEALTH_POINTS;
        }
        else{
            return MAX_NAVEC_HEALTH_POINTS;
        }
    }

    /**
     * Method that gets enemy's position
     * @return position of enemy
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Method that gets enemy's previous position
     * @return prevPosition of enemy
     */
    public Point getPrevPosition() {
        return prevPosition;
    }

    /**
     * Method that gets enemy's current image
     * @return current image of enemy
     */
    public Image getCurrentImage() {
        return NAVEC_R;
    }

    /**
     * Method that determines enemy's moving direction
     */
    public void moveWay(){
        double rand = Math.random();
        isHorizontal = rand > 0.5;
    }

    /**
     * Method that moves enemy given the direction
     * @param xMove the difference between new position's x value
     *             for enemy after moving and previous position's
     * @param yMove the difference between new position's y value
     *             for enemy after moving and previous position's
     */
    protected void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        position = new Point(newX, newY);
    }

    /**
     * Method that moves enemy back to previous position
     */
    public void moveBack(){
        position = prevPosition;
    }

    /**
     * Change the image(different kinds have different images) when enemy is invincible
     */
    public abstract void becomeInv();
    /**
     * Change the image(different kinds have different images) when enemy is normal
     */
    public abstract void becomeNormal();
    /**
     * add invincible time for enemy
     */
    public void setTime(){
        time = time + 1;
    }
    /**
     * reset invincible time for enemy
     */
    public void resetTime(){
        time = 0;
    }
    /**
     * determine if enemy is invincible
     */
    public void ifInv(){
        if(isInv()){
            becomeInv();
        }
        else{
            becomeNormal();
        }
    }

    /**
     * @return isHorizontal which tells us the moving direction of enemy
     */
    public boolean isHorizontal(){
        return isHorizontal;
    }
    /**
     * @return meetBound which tells us if enemy meets bounds
     */
    public boolean isMeetBound(){
        return meetBound;
    }
    /**
     * set the boolean value of meetBound
     * @param meet which tells us if enemy meets bounds
     */
    public void setMeetBound(boolean meet){meetBound = meet;}

    /**
     * @return facingRight determines the direction where the enemy should face to
     */
    public boolean getFacingRight(){return facingRight;}
    /**
     * set the boolean value of facingRight
     * @param dire which tells us where the enemy faces to
     */
    public void setFacingRight(boolean dire){facingRight = dire;}
    /**
     * get the speed
     * @return move_size
     */
    public double getMoveSize(){return move_size;}
    /**
     * set the speed after changing
     */
    public void setMoveSize(){
        timeScale = Level1.getTimeScale();
        move_size = timeScale.getSpeed();
    }
    /**
     * determine if this demon is aggressive, half chance
     */
    public void setIsAggressive(){
        double rand = Math.random();
        isAggressive = rand > 0.5;
    }
    /**
     * let us know if this demon is aggressive
     * @return isAggressive
     */
    public boolean getIsAggressive(){return isAggressive;}

    /**
     * get the attack range
     * @return attack range
     */
    public int getRange(String type) {
        if (type == "DEMON") {
            return DEMON_ATTACK_RANGE;
        }
        else {
            return NAVEC_ATTACK_RANGE;
        }
    }

    /**
     * determine if Fae is in attack range
     * @param status
     */
    public void setInRange(boolean status){
        inRange = status;
    }

    /**
     * let us know if Fae is in attack range
     * @return inRange
     */
    public boolean getInRange(){return inRange;}
    /**
     * Method that renders the current health as a percentage on screen
     * @param type of enemy
     */
    public void renderHealthPoints(String type){
        int healthX = (int) position.x;
        int healthY = (int) position.y - 6;
        double percentageHP;
        if(type == "DEMON"){
            percentageHP = ((double) healthPoints/MAX_DEMON_HEALTH_POINTS) * 100;
        }
        else {
            percentageHP = ((double) healthPoints/MAX_NAVEC_HEALTH_POINTS) * 100;
        }
        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        }
        else{
            COLOUR.setBlendColour(GREEN);
        }
        FONT.drawString(Math.round(percentageHP) + "%", healthX, healthY, COLOUR);
    }

    /**
     * Method that renders the fire of this enemy on screen
     * @param type of enemy to determine the attack range
     */
    public void printFire(String type){
        Image fire;
        Image demonType;
        if(type =="NAVEC"){
            fire = new Image("res/navec/navecFire.png");
            demonType = NAVEC_R;
        }
        else {
            fire = new Image("res/demon/demonFire.png");
            demonType = DEMON;
        }
        int fireX = 0;
        int fireY = 0;

        //when player is in attack range of enemy,
        // we need to calculate the direction of fire
        if (checkDistance(Level1.getPlayer(), type)) {
            int rotation = DemonFire.checkRotation(this, Level1.getPlayer(), type);
            if (rotation == TOP_LEFT) {
                fire.draw(getPosition().x - fire.getWidth()/HALF, getPosition().y - fire.getHeight()/HALF,
                        ROTATION.setRotation(HALF * Math.PI));
                fireX = (int) (getPosition().x - fire.getWidth()/HALF);
                fireY = (int) (getPosition().y - fire.getHeight()/HALF);
            } else if (rotation == BOTTOM_LEFT) {
                fire.draw(getPosition().x - fire.getWidth()/HALF,
                        getPosition().y + fire.getHeight()/HALF+demonType.getHeight(),
                        ROTATION.setRotation(Math.toRadians(ROTATION_BL+HALF*Math.PI)));
                fireX = (int) (getPosition().x - fire.getWidth()/HALF);
                fireY = (int) (getPosition().y + fire.getHeight()/HALF+demonType.getHeight());
            } else if (rotation == TOP_RIGHT) {
                fire.draw(getPosition().x + fire.getWidth()/HALF+demonType.getWidth(),
                        getPosition().y - fire.getHeight()/HALF,
                        ROTATION.setRotation(Math.toRadians(ROTATION_TR+Math.PI)));
                fireX = (int) (getPosition().x + fire.getWidth()/HALF+demonType.getWidth());
                fireY = (int) (getPosition().y - fire.getHeight()/HALF);
            } else {
                fire.draw(getPosition().x + fire.getWidth()/HALF+demonType.getWidth(),
                        getPosition().y + fire.getHeight()/HALF+demonType.getHeight(),
                        ROTATION.setRotation(Math.PI));
                fireX = (int) (getPosition().x + fire.getWidth()/HALF+demonType.getWidth());
                fireY = (int) (getPosition().y + fire.getHeight()/HALF+demonType.getHeight());
            }
        }

        //check the collisions between player and fire
        if (fireX != 0 && fireY != 0) {
            DemonFire fireImage = new DemonFire(fireX, fireY);
            fireImage.checkFireCollisions(Level1.getPlayer(),type);
        }
    }

    /**
     * Method that check if Fae is in attack range
     * @param player Fae
     * @param type of enemy to determine the attack range
     */
    public boolean checkDistance(Player player, String type){
        Image demonType;
        if(type =="NAVEC"){
            demonType = NAVEC_R;
        }
        else {
            demonType = DEMON;
        }
        //this center is enemy's enter
        Point playerPoint = player.getPosition();
        Point center = new Point(getPosition().x + demonType.getWidth()/2,getPosition().y +demonType.getHeight()/2);
        if(center.distanceTo(playerPoint) <= getRange(type)){
            setInRange(true);
        }
        else{
            setInRange(false);
        }
        return getInRange();
    }
}
