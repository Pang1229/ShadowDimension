public class TimeScale {
    protected final static int MAX_BOUND = 3;
    protected final static  int MIN_BOUND = -3;
    private int timeScale;
    private double speed;

    /**
     * Instantiates a new Time scale object.
     */
    public TimeScale() {
        timeScale = 0;
    }

    /**
     * Method to return an int attribute called timeScale between -3 and 3.
     * @return timeScale
     */
    public int getTimeScale(){return timeScale;}

    /**
     * Method to change an int attribute called timeScale between -3 and 3.
     * @param num 1 or -1 to change timeScale
     */
    public void setTimeScale(int num){ timeScale = timeScale + num;}

    /**
     * Method to decrease a double attribute called speed.
     */
    public void DecreaseMoveSize(){speed = speed/2;}

    /**
     * Method to increase a double attribute called speed.
     */
    public void IncreaseMoveSize(){speed = speed*2;}

    /**
     * Method to set a double attribute called speed in this class
     * (move size is created in other classes).
     * @param move_size between 0.2 and 0.7
     */
    public void setSpeed(double move_size){speed = move_size;}

    /**
     * Method to return speed changed to other update methods to show on screen.
     * @return speed
     */
    public double getSpeed(){return speed;}

    /**
     * Method that performs state update
     * @param Key the key user pressed to speed up or slow down
     */
    public void update(String Key) {
        if(Key.equals("L")) {
            timeScaleControl_L();
        } else if (Key.equals("K")) {
            timeScaleControl_K();
        }
    }

    /**
     * Method that decrease the speed when user press "L"
     */
    public void timeScaleControl_L(){
        if(getTimeScale() > MIN_BOUND){
            setTimeScale(-1);
            DecreaseMoveSize();
            System.out.println("Slowed down, Speed: " + getTimeScale());
        }
    }

    /**
     * Method that increase the speed when user press "K"
     */
    public void timeScaleControl_K(){
        if(getTimeScale() < MAX_BOUND) {
            setTimeScale(1);
            IncreaseMoveSize();
            System.out.println("Sped up, Speed: " + getTimeScale());
        }
    }
}
