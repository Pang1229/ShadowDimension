import bagel.*;
import bagel.util.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public abstract class Level {
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String LEVEL0_WORLD_FILE = "res/level0.csv";
    private final static String LEVEL1_WORLD_FILE = "res/level1.csv";
    private final static int TITLE_FONT_SIZE = 75;
    private final static int INSTRUCTION_FONT_SIZE = 40;
    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 90;
    private final static int INS_Y_OFFSET = 190;
    private static final Font TITLE_FONT = new Font("res/frostbite.ttf", TITLE_FONT_SIZE);
    private static final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);
    private final static String INSTRUCTION_MESSAGE = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    protected final static String END_MESSAGE = "GAME OVER!";
    private final static int WALL_ARRAY_SIZE = 52;
    private final static int S_HOLE_ARRAY_SIZE = 5;
    private final static int TREE_ARRAY_SIZE = 15;
    private final static int DEMON_ARRAY_SIZE = 5;
    private final static int NAVEC_ARRAY_SIZE = 1;
    private final static Wall[] walls = new Wall[WALL_ARRAY_SIZE];
    private final static Sinkhole[] level0Sinkholes = new Sinkhole[S_HOLE_ARRAY_SIZE];
    private final static Sinkhole[] level1Sinkholes = new Sinkhole[S_HOLE_ARRAY_SIZE];
    private final static Tree[] trees = new Tree[TREE_ARRAY_SIZE];
    private final static Demon[] demons = new Demon[DEMON_ARRAY_SIZE];
    private final static Navec[] navec = new Navec[NAVEC_ARRAY_SIZE];
    private static Point level0TopLeft;
    private static Point level1TopLeft;
    private static Point bottomRight;
    private static Player level0Player;
    private static Player level1Player;

    /**
     * Performs a state update.
     * The exact implementation is determined by the current level
     * @param input is the Key which has been pressed by user
     * @param gameObject is an instance of shadowDimension class we created
     */
    public abstract void update(Input input, ShadowDimension gameObject);

    /**
     * The method which is used to read different csv files
     * for different levels.
     * @param fileName is the name of CSV file for this level
     */
    public void readCSV(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){

            String line;
            int currentWallCount = 0;
            int currentSinkholeCount = 0;
            int currentTreeCount = 0;
            int currentDemonCount = 0;
            int currentNavecCount = 0;

            while((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                switch (sections[0]) {
                    case "Fae":
                        if(fileName.equals(LEVEL0_WORLD_FILE)) {
                            level0Player = new Player(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                            break;
                        }else if (fileName.equals(LEVEL1_WORLD_FILE)) {
                            level1Player = new Player(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                            break;
                        }
                    case "Wall":
                        walls[currentWallCount] = new Wall(Integer.parseInt(sections[1]),Integer.parseInt(sections[2]));
                        currentWallCount++;
                        break;
                    case "Sinkhole":
                        if(fileName.equals(LEVEL0_WORLD_FILE)){
                            level0Sinkholes[currentSinkholeCount] = new Sinkhole(Integer.parseInt(sections[1]),
                                    Integer.parseInt(sections[2]));
                            currentSinkholeCount++;
                        } else if (fileName.equals(LEVEL1_WORLD_FILE)) {
                            level1Sinkholes[currentSinkholeCount] = new Sinkhole(Integer.parseInt(sections[1]),
                                    Integer.parseInt(sections[2]));
                            currentSinkholeCount++;
                        }
                        break;
                    case "Tree":
                        trees[currentTreeCount] = new Tree(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]));
                        currentTreeCount++;
                        break;
                    case "Demon":
                        demons[currentDemonCount] = new Demon(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]), "DEMON");
                        demons[currentDemonCount].setIsAggressive();
                        currentDemonCount++;
                        break;
                    case "Navec":
                        navec[currentNavecCount] = new Navec(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]), "NAVEC");
                        currentNavecCount++;
                        break;
                    case "TopLeft":
                        if(fileName.equals(LEVEL0_WORLD_FILE)) {
                            level0TopLeft = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                            break;
                        }else if (fileName.equals(LEVEL1_WORLD_FILE)) {
                            level1TopLeft = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                            break;
                        }

                    case "BottomRight":
                        bottomRight = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        //determine the moving direction for each of demons and navec randomly
        if(fileName == LEVEL1_WORLD_FILE){
            for (Navec navec : navec) {
                navec.moveWay();
            }
            for (Demon demon : demons) {
                demon.moveWay();
            }
        }
    }

    /**
     * The method which is used to check the collisions between Fae and
     * other entities. The exact implementation is determined by the
     * current level.
     * @param player is Fae's Player type variable which we control.
     */
    public abstract void checkCollisions(Player player);

    /**
     * Method that checks if Fae has gone out-of-bounds and performs corresponding action
     * @param player is Fae's Player type variable which we control.
     * @param level determines the level we are in.
     */
    public void checkOutOfBounds(Player player, int level){
        Point currentPosition = player.getPosition();
        if(level == 0) {
            if ((currentPosition.y > bottomRight.y) || (currentPosition.y < level0TopLeft.y)
                    || (currentPosition.x < level0TopLeft.x)
                    || (currentPosition.x > bottomRight.x)) {
                player.moveBack();
            }
        } else if (level == 1) {
            if ((currentPosition.y > bottomRight.y) || (currentPosition.y < level1TopLeft.y)
                    || (currentPosition.x < level1TopLeft.x)
                    || (currentPosition.x > bottomRight.x)) {
                player.moveBack();
            }
        }
    }

    /**
     * Method used to draw the start screen title and instructions
     */
    protected static void drawStartScreen(){
        TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE, TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
    }

    /**
     * Method used to draw end screen messages
     */
    protected static void drawMessage(String message){
        TITLE_FONT.drawString(message, (Window.getWidth()/2.0 - (TITLE_FONT.getWidth(message)/2.0)),
                (Window.getHeight()/2.0 + (TITLE_FONT_SIZE/2.0)));
    }

    /**
     * Method used to return player variable for different levels
     * @return level0Player
     */
    public static Player getLevel0Player() {
        return level0Player;
    }

    /**
     * Method used to return player variable for different levels
     * @return level1Player
     */
    public static Player getLevel1Player() {
        return level1Player;
    }

    /**
     * Method used to return a list for walls
     * @return walls whose type is Wall[]
     */
    public static Wall[] getWalls() {
        return walls;
    }

    /**
     * Method used to return a list for trees
     * @return trees whose type is Tree[]
     */
    public static Tree[] getTrees() {
        return trees;
    }

    /**
     * Method used to return a list for demons
     * @return demons whose type is Demon[]
     */
    public static Demon[] getDemons() {
        return demons;
    }

    /**
     * Method used to return a list for navec
     * @return navec whose type is Navec[]
     */
    public static Navec[] getNavec() {
        return navec;
    }

    /**
     * Method used to return a list for holes
     * @return level0Sinkholes whose type is Sinkhole[]
     */
    public static Sinkhole[] getLevel0Sinkholes() {
        return level0Sinkholes;
    }

    /**
     * Method used to return a list for holes
     * @return level1Sinkholes whose type is Sinkhole[]
     */
    public static Sinkhole[] getLevel1Sinkholes() {
        return level1Sinkholes;
    }

    /**
     * Method used to return Point level1TopLeft
     * @return level1TopLeft
     */
    public static Point getLevel1TopLeft(){
        return level1TopLeft;
    }

    /**
     * Method used to return Point bottomRight
     * @return  bottomRight
     */
    public static Point getBottomRight(){
        return bottomRight;
    }

}
