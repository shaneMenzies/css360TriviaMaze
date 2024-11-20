package model.interfaces;

import model.Maze;

/**
 * Interface for general maze generators.
 * Each should provide its own methods for configuring
 * the parameters for generation.
 *
 * @author Shane Menzies
 * @version 11/15/24
 */
public interface MazeGenerator {

    /**
     * Generates a maze as configured.
     *
     * @return A new Maze following configured settings.
     */
    Maze generate();
}