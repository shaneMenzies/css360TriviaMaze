package model;

import static model.interfaces.GameModelUpdateListener.UpdateType.*;

import java.util.ArrayList;
import java.util.List;
import model.interfaces.GameModelUpdateListener;
import model.interfaces.GameStateUpdateListener;
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

    /** Game settings for the current game. */
    private final GameSettings mySettings;

    /** Save controller for managing game saves.*/
    private final SaveController mySaveController;

    /**
     * Update listeners for the entire Game.
     */
    private final List<GameModelUpdateListener> myListeners;

    /** Current game state. */
    private GameState myState;


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

        myListeners = new ArrayList<>();

        // Initializes state as null - it will be created when newGame() is called
        myState = null;
    }

    /**
     * Creates a new game by generating a new maze and initializing game state.
     */
    public void newGame() {
        // Generates a new maze
        Maze newMaze = myGenerator.generate();

        // Creates a new game state with the generated maze and current settings
        myState = new GameState(mySettings, newMaze);
        myState.addUpdateListener(this::onGameStateUpdate);

        updateListeners(NEW_GAME);
    }

    /**
     * Saves the current game state.
     */
    public void saveGame() {
        if (myState != null) {
            mySaveController.saveGame(myState);

            updateListeners(SAVED);
        }
    }

    /**
     * Loads a previously saved game state.
     */
    public void loadGame() {
        myState = mySaveController.loadGame();
        myState.addUpdateListener(this::onGameStateUpdate);

        updateListeners(LOADED);
        updateListeners(GAME_STATE_PLAYER);
        updateListeners(GAME_STATE_DOORS);
        updateListeners(GAME_STATE_PHASE);
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
     * Adds an update listener to this game.
     *
     * @param theListener New listener to add.
     */
    public void addUpdateListener(final GameModelUpdateListener theListener) {
        myListeners.add(theListener);
    }

    /**
     * Remove a previously added update listener from this game.
     *
     * @param theListener Previously added listener to remove.
     * @return True if successfully removed, false otherwise.
     */
    public boolean removeUpdateListener(final GameModelUpdateListener theListener) {
        return myListeners.remove(theListener);
    }

    /**
     * Updates all listeners with a certain update.
     *
     * @param theUpdateType Type of update.
     */
    private void updateListeners(final GameModelUpdateListener.UpdateType theUpdateType) {
        for (final GameModelUpdateListener listener : myListeners) {
            listener.doUpdate(theUpdateType, this);
        }
    }

    /**
     * Send along the appropriate update to our listeners for a GameState update.
     *
     * @param theType Type of update from GameState
     * @param theState GameState with update
     */
    private void onGameStateUpdate(final GameStateUpdateListener.UpdateType theType,
                                   final GameState theState) {
        updateListeners(GameModelUpdateListener.UpdateType.fromGameStateUpdate(theType));
    }
}