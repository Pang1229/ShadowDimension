import bagel.*;

/**
 * SWEN20003 Project 1, Semester 2, 2022
 *
 * @author Xiaoyu Pang 1224627
 */
public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String LEVEL0_WORLD_FILE = "res/level0.csv";
    private final static String LEVEL1_WORLD_FILE = "res/level1.csv";
    private final static int LEVEL_UP_TIME = 180;
    private final static int TITLE_FONT_SIZE = 75;
    private final static int INSTRUCTION_FONT_SIZE = 40;
    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 90;
    private final Font TITLE_FONT = new Font("res/frostbite.ttf", TITLE_FONT_SIZE);
    private final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);
    private final static String LEVEL_UP = "LEVEL COMPLETE!";
    private final static String LEVEL1_START = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
    private int time = 0;
    private final static int LEVEL1INS_Y_OFFSET = 100;
    private final Level0 level0 = new Level0();
    private final Level1 level1 = new Level1();
    /**
     * Constructor for shadowDimension class
     */
    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        readCSV();
    }
    /**
     * The method which is used to read different csv files
     * for different levels
     */
    private void readCSV(){
        level0.readCSV(LEVEL0_WORLD_FILE);
        level1.readCSV(LEVEL1_WORLD_FILE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * @param input is the Key which has been pressed by user
     * @return nothing
     */
    @Override
    public void update(Input input){
        if(!level0.ifNext()) {
            level0.update(input, this);
        }
        else{
            if(!level1.ifStart()) {
                if (time >= LEVEL_UP_TIME) {
                    drawLevelScreen();
                } else {
                    drawMessage();
                    time++;
                }
            }
            level1.update(input, this);
        }
    }

    /**
     * Method used to draw LEVEL UP screen messages
     */
    private void drawMessage(){
        TITLE_FONT.drawString(ShadowDimension.LEVEL_UP,
                (Window.getWidth()/2.0 - (TITLE_FONT.getWidth(ShadowDimension.LEVEL_UP)/2.0)),
                (Window.getHeight()/2.0 + (TITLE_FONT_SIZE/2.0)));
    }

    /**
     * Method used to draw LEVEL1 instructions-screen messages
     */
    private void drawLevelScreen(){
        INSTRUCTION_FONT.drawString(ShadowDimension.LEVEL1_START,
                TITLE_X + INS_X_OFFSET, TITLE_Y + LEVEL1INS_Y_OFFSET);
    }

    /**
     * Method used to count time for showing LEVEL UP screen
     * @return int: the value of time variable
     */
    public boolean ifThreeSeconds(){
        return (time >= LEVEL_UP_TIME);
    }
    /**
     * Method used to return level0 variable
     * @return level0: a Level0 type variable
     */
    public Level0 getLevel0(){return level0;}
    /**
     * Method used to return level1 variable
     * @return level0: a Level1 type variable
     */
    public Level1 getLevel1(){return level1;}
}