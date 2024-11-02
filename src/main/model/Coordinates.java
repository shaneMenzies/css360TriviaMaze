package model;

/**
 * Class representing coordinates within a room, including X and Y positions.
 *
 * @author Arafa Mohamed
 * @version 10/30/24
 */
public final class Coordinates {

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
    public Room getRoom() {
        return myRoom;
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
     * @param room the room to set
     */
    public void setRoom(final Room room) {
        myRoom = room;
    }

    /**
     * Sets the x-coordinate.
     * @param x the x-coordinate to set
     */
    public void setX(final int x) {
        myX = x;
    }

    /**
     * Sets the y-coordinate.
     * @param y the y-coordinate to set
     */
    public void setY(final int y) {
        myY = y;
    }

    /**
     * Checks if this coordinate is equal to another object.
     * @param obj the object to compare
     * @return true if both have the same coordinates and room
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        final Coordinates other = (Coordinates) obj;
        return myX == other.myX &&
                myY == other.myY &&
                (myRoom == null ? other.myRoom == null : myRoom.equals(other.myRoom));
    }

    /**
     * Generates a hash code for the coordinates.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = myRoom != null ? myRoom.hashCode() : 0;
        result = 31 * result + myX;
        result = 31 * result + myY;
        return result;
    }

}









