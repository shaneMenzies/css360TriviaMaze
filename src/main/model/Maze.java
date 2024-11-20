package model;

import java.io.Serializable;
import model.enums.Direction;
import model.interfaces.Tile;

/**
 * Represents an entire maze consisting of a 2D array of Rooms.
 *
 * @author Shane Menzies
 * @version 11/2/24
 */
public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Exception message for an invalid rooms array with height of 0.
     */
    private static final String INVALID_ROOMS_HEIGHT_0_MESSAGE
            = "Invalid Rooms Array with height 0 provided to Maze()!";

    /**
     * Exception message for an invalid rooms array with width of 0.
     */
    private static final String INVALID_ROOMS_WIDTH_0_MESSAGE
            = "Invalid Rooms Array with width 0 provided to Maze()!";

    /**
     * Exception message for an invalid rooms array with width of 0.
     */
    private static final String INVALID_ROOMS_NOT_RECTANGLE_MESSAGE
            = "Invalid Rooms Array with non-rectangular shape provided to Maze()!";

    /**
     * Exception message for an invalid direction.
     */
    private static final String INVALID_DIRECTION_MESSAGE = "Invalid Direction provided!";

    /**
     * 2D array of this maze's rooms.
     */
    private final Room[][] myRooms;

    /**
     * This maze's door controllers. One for each pair of doors.
     */
    private final DoorController[] myDoors;

    /**
     * X coordinate of the starting point in the maze.
     */
    private final int myStartingRoomX;

    /**
     * Y coordinate of the starting point in the maze.
     */
    private final int myStartingRoomY;

    /**
     * X coordinate of the exit point in the maze.
     */
    private final int myExitRoomX;

    /**
     * Y coordinate of the exit point in the maze.
     */
    private final int myExitRoomY;

    /**
     * Constructs a Maze using the provided 2D array of Rooms.
     *
     * @param theRooms 2D Array of Rooms, must be rectangular.
     * @param theDoors Array of DoorControllers for the doors in this maze.
     * @param theStartX X coordinate of the starting room.
     * @param theStartY Y coordinate of the starting room.
     * @param theExitX X coordinate of the exit room.
     * @param theExitY Y coordinate of the exit room.
     * @throws IllegalArgumentException If theRooms was not a rectangular array of rooms.
     */
    public Maze(final Room[][] theRooms, final DoorController[] theDoors,
                final int theStartX, final int theStartY,
                final int theExitX, final int theExitY)
            throws IllegalArgumentException {
        myRooms = theRooms;
        myDoors = theDoors;
        myStartingRoomX = theStartX;
        myStartingRoomY = theStartY;
        myExitRoomX = theExitX;
        myExitRoomY = theExitY;

        // Double check room Array is valid
        verifyRooms();
    }

    /**
     * Gets this maze's array of Rooms.
     *
     * @return This maze's rectangular array of Rooms.
     */
    public Room[][] getRooms() {
        return myRooms;
    }

    /**
     * Gets this maze's array of DoorControllers.
     * Each one corresponds to a pair of doors in the maze.
     *
     * @return This maze's array of DoorControllers.
     */
    public DoorController[] getDoors() {
        return myDoors;
    }

    /**
     * Gets a certain Room inside this Maze.
     *
     * @param theX X / Column coordinate.
     * @param theY Y / Row coordinate.
     * @return Room at the provided coordinates.
     */
    public Room getRoom(final int theX, final int theY) {
        return myRooms[theY][theX];
    }

    /**
     * Gets the tile at a certain set of Coordinates inside this maze.
     *
     * @param theCoordinates Target coordinates for tile to get.
     * @return Tile at specified coordinates.
     */
    public Tile getTile(final Coordinates theCoordinates) {
        return myRooms[theCoordinates.getRoomY()][theCoordinates.getRoomX()].
                getTile(theCoordinates.getX(), theCoordinates.getY());
    }

    /**
     * Checks if a certain Room inside this Maze has a neighboring room
     * in the specified direction.
     *
     * @param theX X coordinate of target Room.
     * @param theY Y coordinate of target Room.
     * @param theDirection Direction to check for a neighbor in.
     * @return True if the target Room has a neighbor in the specified direction,
     *          false otherwise.
     */
    public boolean hasNeighbor(final int theX, final int theY, final Direction theDirection) {
        return switch (theDirection) {
            case UP:
                yield (theY + 1) < getHeight() && myRooms[theY + 1][theX] != null;

            case DOWN:
                yield (theY - 1) >= 0 && myRooms[theY - 1][theX] != null;

            case RIGHT:
                yield (theX + 1) < getWidth() && myRooms[theY][theX + 1] != null;

            case LEFT:
                yield (theX - 1) >= 0 && myRooms[theY][theX - 1] != null;
        };
    }

    /**
     * Returns the neighbor of a certain Room inside this Maze
     *  in the specified direction.
     *
     * @param theX X coordinate of the target Room.
     * @param theY Y coordinate of the target Room.
     * @param theDirection Direction from target Room to the neighboring Room.
     * @return Neighbor Room of target Room in specified Direction.
     */
    public Room getNeighbor(final int theX, final int theY, final Direction theDirection) {
        if (!hasNeighbor(theX, theY, theDirection)) {
            // This room doesn't have a neighbor here
            return null;
        }

        return switch (theDirection) {
            case UP:
                yield myRooms[theY + 1][theX];

            case DOWN:
                yield myRooms[theY - 1][theX];

            case RIGHT:
                yield myRooms[theY][theX + 1];

            case LEFT:
                yield myRooms[theY][theX - 1];
        };
    }

    /**
     * Moves a set of Coordinates in a certain direction.
     * This method handles both the coordinates inside the room
     * and moving between rooms.
     *
     * @param theCoordinates Initial Coordinates
     * @param theDirection Direction to move coordinates.
     * @return New Coordinates moved one tile in provided direction.
     */
    public Coordinates moveCoordinates(final Coordinates theCoordinates,
                                       final Direction theDirection) {
        final int roomX = theCoordinates.getRoomX();
        final int roomY = theCoordinates.getRoomY();
        int x = theCoordinates.getX();
        int y = theCoordinates.getY();

        // Move x and y coordinates inside room.
        switch (theDirection) {
            case UP:
                y++;
                break;
            case DOWN:
                y--;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;

            default:
                // This shouldn't be able to happen?
                throw new IllegalArgumentException(INVALID_DIRECTION_MESSAGE);
        }

        // Check if these coordinates are still inside the current room
        final Coordinates newCoordinates;
        if (getRoom(roomX, roomY).insideBounds(x, y)) {
            newCoordinates = new Coordinates(roomX, roomY, x, y);
        } else {
            if (hasNeighbor(roomX, roomY, theDirection)) {
                newCoordinates = moveCoordinatesAcrossRooms(
                        new Coordinates(roomX, roomY, x, y), theDirection);
            } else {
                // Can't move this direction.
                newCoordinates = theCoordinates;
            }
        }

        // Return moved Coordinates
        return newCoordinates;
    }

    /**
     * Gets the X coordinate of the starting room for this Maze.
     *
     * @return X coordinate of the starting room for this Maze.
     */
    public int getStartingRoomX() {
        return myStartingRoomX;
    }

    /**
     * Gets the Y coordinate of the starting room for this Maze.
     *
     * @return Y coordinate of the starting room for this Maze.
     */
    public int getStartingRoomY() {
        return myStartingRoomY;
    }

    /**
     * Gets the X coordinate of the exit room for this Maze.
     *
     * @return X coordinate of the exit room for this Maze.
     */
    public int getExitRoomX() {
        return myExitRoomX;
    }

    /**
     * Gets the Y coordinate of the exit room for this Maze.
     *
     * @return Y coordinate of the exit room for this Maze.
     */
    public int getExitRoomY() {
        return myExitRoomY;
    }

    /**
     * Gets the height of this Maze.
     *
     * @return Height of this Maze.
     */
    public int getHeight() {
        return myRooms.length;
    }

    /**
     * Gets the width of this Maze.
     *
     * @return Width of this Maze.
     */
    public int getWidth() {
        return myRooms[0].length;
    }

    /**
     * Verifies myRooms array is a valid array for a maze.
     *
     * @throws IllegalArgumentException If myRooms is not a rectangular array of rooms.
     */
    private void verifyRooms() throws IllegalArgumentException {
        if (myRooms.length == 0) {
            throw new IllegalArgumentException(INVALID_ROOMS_HEIGHT_0_MESSAGE);
        }

        // Make sure room Array is a rectangle
        final int width = myRooms[0].length;
        if (width == 0) {
            throw new IllegalArgumentException(INVALID_ROOMS_WIDTH_0_MESSAGE);
        } else {
            for (Room[] row : myRooms) {
                if (row.length != width) {
                    throw new IllegalArgumentException(INVALID_ROOMS_NOT_RECTANGLE_MESSAGE);
                }
            }
        }
    }

    /**
     * Moves a Coordinate to a neighboring room.
     *
     * @param theCoordinate Initial coordinates.
     * @param theDirection Direction to move.
     * @return Coordinates after moving to neighboring room.
     */
    private Coordinates moveCoordinatesAcrossRooms(final Coordinates theCoordinate,
                                                   final Direction theDirection) {
        int roomX = theCoordinate.getRoomX();
        int roomY = theCoordinate.getRoomY();
        int x = theCoordinate.getX();
        int y = theCoordinate.getY();

        // Need to move to neighbor room
        switch (theDirection) {
            case UP:
                roomY++;
                y = 0;
                break;

            case DOWN:
                roomY--;
                y = getRoom(roomX, roomY).getHeight() - 1;
                break;

            case RIGHT:
                roomX++;
                x = 0;
                break;

            case LEFT:
                roomX--;
                x = getRoom(roomX, roomY).getWidth() - 1;
                break;

            default:
                // This shouldn't be able to happen?
                throw new IllegalArgumentException(INVALID_DIRECTION_MESSAGE);
        }

        return new Coordinates(roomX, roomY, x, y);
    }

}
