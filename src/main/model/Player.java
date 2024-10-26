package model;

import model.interfaces.PlayerUpdateListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game, tracking their position, score, and lives.
 *
 * @author Arafa Mohamed
 * @version 10/25/24
 */
public class Player {
    private Coordinates myPosition;
    private int myScore;
    private int myLives;
    private List<PlayerUpdateListener> myListeners;

    /**
     * Constructs a new Player with the specified position, score, and number of lives.
     * @param position the starting coordinates of the player
     * @param score    the initial score of the player
     * @param lives    the initial number of lives of the player
     */
    public Player(Coordinates position, int score, int lives) {
        myPosition = position;
        myScore = score;
        myLives = lives;
        myListeners = new ArrayList<>();
    }

    /**
     * Notifies all registered listeners about player updates.
     */
    private void updateListeners() {
        for (PlayerUpdateListener listener : myListeners) {
            listener.doUpdate(this);
        }
    }

    /**
     * Gets the current position of the player.
     * @return the current coordinates of the player
     */
    public Coordinates getPosition() {
        return myPosition;
    }

    /**
     * Gets the current score of the player.
     * @return the current score of the player
     */
    public int getScore() {
        return myScore;
    }

    /**
     * Gets the current number of lives of the player.
     * @return the current number of lives of the player
     */
    public int getLives() {
        return myLives;
    }

    /**
     * Sets the player's position and updates listeners.
     * @param position the new coordinates for the player
     */
    public void setPosition(Coordinates position) {
        myPosition = position;
        updateListeners();
    }

    /**
     * Sets the player's score and updates listeners.
     * @param score the new score for the player
     */
    public void setScore(int score) {
        myScore = score;
        updateListeners();
    }

    /**
     * Sets the player's number of lives and updates listeners.
     * @param lives the new number of lives for the player
     */
    public void setLives(int lives) {
        myLives = lives;
        updateListeners();
    }

    /**
     * Adds a listener that will be notified of updates to this player.
     * @param listener the listener to be added
     */
    public void addUpdateListener(PlayerUpdateListener listener) {
        myListeners.add(listener);
    }

    /**
     * Removes a listener so that it will no longer receive updates.
     * @param listener the listener to be removed
     */
    public void removeUpdateListener(PlayerUpdateListener listener) {
        myListeners.remove(listener);
    }
}













