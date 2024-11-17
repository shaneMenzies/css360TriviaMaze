package model;

import java.io.*;

/**
 * SaveController is responsible for saving and loading game state to and from a specified file.
 *
 * @author Arafa Mohamed
 * @version 11/13/24
 */
public final class SaveController {

    /** The file path where game states will be saved */
    private final String mySaveLocation;

    /**
     * Constructs a SaveController with the specified save location.
     * @param saveLocation the location where the game state will be saved
     */
    public SaveController(final String saveLocation) {
        mySaveLocation = saveLocation;
    }

    /**
     * Saves the current game state to the specified save location.
     * @param state the GameState object to be saved
     * @throws RuntimeException if the game state cannot be saved
     */
    public void saveGame(final GameState state) {
        if (state == null) {
            throw new NullPointerException("GameState cannot be null");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(mySaveLocation))) {
            oos.writeObject(state);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save game: " + e.getMessage());
        }
    }

    /**
     * Loads the game state from the specified save location.
     * @return the loaded GameState object
     * @throws RuntimeException if the game state cannot be loaded
     */
    public GameState loadGame() {
        File file = new File(mySaveLocation);
        if (!file.exists() || file.length() == 0) {
            throw new RuntimeException("Failed to load game: save file is empty or does not exist");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(mySaveLocation))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load game: " + e.getMessage());
        }
    }

}



