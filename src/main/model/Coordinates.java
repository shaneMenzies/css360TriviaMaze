package model;

// Working on it

/**
 * Class representing coordinates within a room, including X and Y positions.
 *
 * @author Arafa Mohamed
 * @version 10/26/24
 */
public class Coordinates {

    /** The room associated with these coordinates. */
    private Room myRoom;

    /** The x-coordinate position within the specified room. */
    private int myX;

    /** The y-coordinate position within the specified room.*/
    private int myY;


    /**
     * Initializes coordinates with a specified room and x, y positions.
     * @param room the room associated with these coordinates
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Coordinates(final Room room, final int x, final int y) {
        myRoom = room;
        myX = x;
        myY = y;
    }

    /**
     * Gets the room for this coordinate.
     * @return the room associated with these coordinates
     */
    public final Room getRoom() {
        return myRoom;
    }

    /**
     * Gets the x-coordinate.
     * @return x-coordinate value
     */
    public final int getX() {
        return myX;
    }




}









