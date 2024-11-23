package model;

import model.interfaces.MazeGenerator;
import model.interfaces.QuestionSource;

/**
 * Represents the core game model that manages the game's state, generation, and progression.
 *
 * @author Arafa Mohamed
 * @version 11/20/24
 */
public class GameModel {

    /** Maze generator for creating game mazes.*/
    private final MazeGenerator myGenerator;

    /** Question source for generating trivia questions. */
    private final QuestionSource myQuestionDatabase;

    /** Save controller for managing game saves.*/
    private final SaveController mySaveController;

    /** Current game state. */
    private GameState myState;

    /** Game settings for the current game. */
    private final GameSettings mySettings;

    /**
     * Constructs a new GameModel with the specified maze generator and question source.
     * @param theGenerator Maze generator to create game mazes
     * @param theQuestionDatabase Source of trivia questions
     */
    public GameModel(
            final MazeGenerator theGenerator,
            final QuestionSource theQuestionDatabase) {
        this(
                theGenerator,
                theQuestionDatabase,
                new GameSettings(3, 10, -5),
                new SaveController("gamesave.ser")
        );
    }

    /**
     * A constructor for GameModel with custom settings and save controller.
     * @param theGenerator Maze generator to create game mazes
     * @param theQuestionDatabase Source of trivia questions
     * @param theSettings Game settings
     * @param theSaveController Save controller for game state
     */
    public GameModel(
            final MazeGenerator theGenerator,
            final QuestionSource theQuestionDatabase,
            final GameSettings theSettings,
            final SaveController theSaveController) {

        myGenerator = theGenerator;
        myQuestionDatabase = theQuestionDatabase;
        mySettings = theSettings;
        mySaveController = theSaveController;

        // Configures the maze generator
        configureMazeGenerator(theGenerator);

        // Creates initial game state
        newGame();
    }

    /**
     * Configures the maze generator with default or predefined settings.
     * @param theGenerator Maze generator to configure
     */
    private void configureMazeGenerator(final MazeGenerator theGenerator) {
        if (theGenerator instanceof RectangleMazeGenerator) {
            RectangleMazeGenerator rectangleGenerator =
                    (RectangleMazeGenerator) theGenerator;

            // Sets default maze and room dimensions
            rectangleGenerator.setMazeDimensions(3, 3);
            rectangleGenerator.setRoomDimensions(7, 7);

            // Sets start and exit coordinates
            rectangleGenerator.setStartCoordinates(0, 0);
            rectangleGenerator.setExitCoordinates(2, 2);

            // Sets question source
            rectangleGenerator.setQuestionsSource(myQuestionDatabase);
        }
    }

    /**
     * Creates a new game by generating a new maze and initializing game state.
     */
    public void newGame() {
        // Generates a new maze
        Maze newMaze = myGenerator.generate();

        // Creates a new game state with the generated maze and current settings
        myState = new GameState(mySettings, newMaze);
    }

    /**
     * Saves the current game state.
     */
    public void saveGame() {
        if (myState != null) {
            mySaveController.saveGame(myState);
        }
    }

    /**
     * Loads a previously saved game state.
     */
    public void loadGame() {
        myState = mySaveController.loadGame();
    }

    /**
     * Gets the current game state.
     * @return Current GameState
     */
    public GameState getState() {
        return myState;
    }

    /**
     * Gets the question database.
     * @return QuestionSource used in the game
     */
    public QuestionSource getDatabase() {
        return myQuestionDatabase;
    }

    /**
     * Sets the current game state.
     * @param theState New game state to set
     */
    public void setState(final GameState theState) {
        myState = theState;
    }

}



