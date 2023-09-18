import bagel.Image;
import bagel.util.Rectangle;

public class Navec extends Enemy{
    private final Image NAVEC_R = new Image("res/navec/navecRight.png");
    private final Image NAVEC_L = new Image("res/navec/navecLeft.png");
    private final Image NAVEC_IR = new Image("res/navec/navecInvincibleRight.png");
    private final Image NAVEC_IL = new Image("res/navec/navecInvincibleLeft.png");
    private final static int DAMAGE_POINTS = 20;
    private Image currentImage;

    /**
     * Constructor for Navec class
     * @param startX the x value of start position for navec
     * @param startY the y value of start position for navec
     * @param type type of enemy
     */
    public Navec(int startX, int startY, String type){
        super(startX,startY,type);
    }

    /**
     * Method that performs state update
     */
    // Firstly check if this navce is alive, then check the moving direction,
    // last part is checking collisions with bounds and other entities
    // also have a status part to control if it is invincible and reduce hp
    public void update() {
        if (isActive()) {
            if (!isHorizontal()) {
                this.currentImage = NAVEC_R;
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
                        this.currentImage = NAVEC_L;
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
                        this.currentImage = NAVEC_R;
                        setFacingRight(true);
                    }
                }
            }
            ifInv();
            this.currentImage.drawFromTopLeft(getPosition().x, getPosition().y);
            printFire("NAVEC");
            renderHealthPoints("NAVEC");
        }
    }

    // javadoc is in Enemy class
    // (In ED, one tutor said we can only write javadoc for the abstract method)
    public Rectangle getBoundingBox(){
        return new Rectangle(this.getPosition(), NAVEC_R.getWidth(), NAVEC_R.getHeight());
    }

    // javadoc is in Enemy class
    public static int getDamagePoints(){
        return DAMAGE_POINTS;
    }

    // javadoc is in Enemy class
    public Image getCurrentImage() {
        return NAVEC_R;
    }

    // javadoc is in Enemy class
    public void becomeInv(){
        if (this.currentImage == NAVEC_R) {
            this.currentImage = NAVEC_IR;
        } else if(this.currentImage == NAVEC_L) {
            this.currentImage = NAVEC_IL;
        }
    }

    // javadoc is in Enemy class
    public void becomeNormal(){
        if (this.currentImage == NAVEC_IR) {
            this.currentImage = NAVEC_R;
        } else if(this.currentImage == NAVEC_IL) {
            this.currentImage = NAVEC_L;
        }
    }

    /**
     * determine if navec is invincible and perform corresponding actions.
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
