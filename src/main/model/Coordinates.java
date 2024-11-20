package model;

import java.io.Serializable;

/**
 * Class representing coordinates within a room, including X and Y positions.
 *
 * @author Arafa Mohamed
 * @author Shane Menzies
 * @version 11/6/24
 */
public final class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Inner separator between X and Y values.
     */
    private static final String COORDINATE_INNER_SEPARATOR = ", ";

    /**
     * Outer separator between Room coordinates and Tile coordinates.
     */
    private static final String COORDINATE_OUTER_SEPARATOR = "; ";

    /**
     * X coordinate identifying the room.
     */
    private int myRoomX;

    /**
     * Y coordinate identifying the room.
     */
    private int myRoomY;

    /**
     * The x-coordinate position within the specified room.
     */
    private int myX;

    /**
     * The y-coordinate position within the specified room.
     */
    private int myY;

    /**
     * Initializes coordinates with a specified room and x, y positions inside that room.
     * @param theRoomX the x-coordinate for the room
     * @param theRoomY the y-coordinate for the room
     * @param theX the x-coordinate inside the room
     * @param theY the y-coordinate inside the room
     */
    public Coordinates(final int theRoomX, final int theRoomY,
                       final int theX, final int theY) {
        myRoomX = theRoomX;
        myRoomY = theRoomY;
        myX = theX;
        myY = theY;
    }

    /**
     * Gets the room's x-coordinate.
     * @return Room's x-coordinate value
     */
    public int getRoomX() {
        return myRoomX;
    }

    /**
     * Gets the room's y-coordinate.
     * @return Room's y-coordinate value
     */
    public int getRoomY() {
        return myRoomY;
    }

    /**
     * Gets the x-coordinate.
     * @return x-coordinate value
     */
    public int getX() {
        return myX;
    }

    /**
     * Gets the y-coordinate.
     * @return y-coordinate value
     */
    public int getY() {
        return myY;
    }

    /**
     * Sets the room for this coordinate.
     * @param theRoomX New room x-coordinate
     * @param theRoomY New room y-coordinate
     */
    public void setRoom(final int theRoomX, final int theRoomY) {
        myRoomX = theRoomX;
        myRoomY = theRoomY;
    }

    /**
     * Sets the x-coordinate.
     * @param theX the x-coordinate to set
     */
    public void setX(final int theX) {
        myX = theX;
    }

    /**
     * Sets the y-coordinate.
     * @param theY the y-coordinate to set
     */
    public void setY(final int theY) {
        myY = theY;
    }

    /**
     * Gets a string representation of these coordinates.
     *
     * @return String representation of these coordinates.
     */
    @Override
    public String toString() {
        return "Room: (" + myRoomX + COORDINATE_INNER_SEPARATOR + myRoomY + ')'
                + COORDINATE_OUTER_SEPARATOR
                + "Tile: (" + myX + COORDINATE_INNER_SEPARATOR + myY + ')';
    }

    /**
     * Checks if this coordinate is equal to another object.
     * @param theObj the object to compare
     * @return true if both have the same coordinates
     */
    @Override
    public boolean equals(final Object theObj) {

        if (theObj instanceof Coordinates other) {
            return myX == other.myX
                    && myY == other.myY
                    && myRoomX == other.myRoomX
                    && myRoomY == other.myRoomY;
        } else {
            return false;
        }
    }

    /**
     * Generates a hash code for the coordinates.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = myRoomX;
        result = 31 * result + myRoomY;
        result = 31 * result + myX;
        result = 31 * result + myY;
        return result;
    }

}