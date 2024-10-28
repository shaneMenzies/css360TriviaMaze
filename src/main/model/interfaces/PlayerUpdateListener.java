package model.interfaces;

import model.Player;

/**
 * Represents an interface for player update listener
 *
 * @author Arafa Mohamed
 * @version 10/25/24
 */
public interface PlayerUpdateListener {

    /**
     * Updates the listener with the latest state of the player.
     * @param player the player whose state has been updated
     */
    void doUpdate(Player player);

}




