import bagel.*;
import bagel.util.Rectangle;

public class Demon extends Enemy {
    private final Image DEMON_R = new Image("res/demon/demonRight.png");
    private final Image DEMON_L = new Image("res/demon/demonLeft.png");
    private final Image DEMON_IL = new Image("res/demon/demonInvincibleRight.png");
    private final Image DEMON_IR = new Image("res/demon/demonInvincibleRight.png");
    private static final int DAMAGE_POINTS = 10;

    /**
     * Constructor for Demon class
     * @param startX the x value of start position for demon
     * @param startY the y value of start position for demon
     * @param type type of enemy
     */
    public Demon(int startX, int startY,String type){
        super(startX,startY,type);
    }

    /**
     * Method that performs state update
     */
    // Firstly check if this demon is alive and if it is an aggressive demon,
    // then check the moving direction,
    // last part is checking collisions with bounds and other entities
    // also have a state part to control if it is invincible and reduce its hp
    public void update() {
        if (isActive()) {
            if (getIsAggressive()) {
                if (!isHorizontal()) {
                    this.currentImage = DEMON_R;
                    if (!isMeetBound()) {
                        setPrevPosition();
                        setPrevHealthPoints();
                        move(0, -getMoveSize());
                        Level1.checkDCollisions(this);
                        Level1.checkDOutOfBounds(this);

                        if (getPosition() == getPrevPosition()) {
                            setMeetBound(true);
                        }
                    } else {
                        setPrevPosition();
                        setPrevHealthPoints();
                        move(0, +getMoveSize());
                        Level1.checkDCollisions(this);
                        Level1.checkDOutOfBounds(this);

                        if (getPosition() == getPrevPosition()) {
                            setMeetBound(false);
                        }
                    }
                } else {
                    if (!isMeetBound()) {
                        setPrevPosition();
                        setPrevHealthPoints();
                        move(-getMoveSize(), 0);
                        Level1.checkDCollisions(this);
                        Level1.checkDOutOfBounds(this);

                        if (getPosition() == getPrevPosition()) {
                            setMeetBound(true);
                        }

                        if (getFacingRight()) {
                            currentImage = DEMON_L;
                            setFacingRight(false);
                        }
                    } else {
                        setPrevPosition();
                        setPrevHealthPoints();
                        move(+getMoveSize(), 0);
                        Level1.checkDCollisions(this);
                        Level1.checkDOutOfBounds(this);

                        if (getPosition() == getPrevPosition()) {
                            setMeetBound(false);
                        }

                        if (!getFacingRight()) {
                            currentImage = DEMON_R;
                            setFacingRight(true);
                        }
                    }
                }
                ifInv();
                currentImage.drawFromTopLeft(getPosition().x, getPosition().y);
            }
            if(!getIsAggressive()) {
                currentImage = DEMON_R;
                ifInv();
            }
            currentImage.drawFromTopLeft(getPosition().x, getPosition().y);
            printFire("DEMON");
            renderHealthPoints("DEMON");
        }
    }

    // javadoc is in Enemy class
    // (In ED, one tutor said we can only write javadoc for the abstract method)
    public Rectangle getBoundingBox(){
        return new Rectangle(this.getPosition(), DEMON_R.getWidth(), DEMON_R.getHeight());
    }

    // javadoc is in Enemy class
    public static int getDamagePoints(){
        return DAMAGE_POINTS;
    }

    // javadoc is in Enemy class
    public void becomeInv(){
        if (this.currentImage == DEMON_R) {
            this.currentImage = DEMON_IR;
        } else if(this.currentImage == DEMON_L) {
            this.currentImage = DEMON_IL;
        }
    }

    // javadoc is in Enemy class
    public void becomeNormal(){
        if (this.currentImage == DEMON_IR) {
            this.currentImage = DEMON_R;
        } else if(this.currentImage == DEMON_IL) {
            this.currentImage = DEMON_L;
        }
    }

    /**
     * determine if demon is invincible and perform corresponding actions.
     */
    public void ifInv(){
        if(isInv()){
            becomeInv();
        }
        else{
            becomeNormal();
        }
    }
}
