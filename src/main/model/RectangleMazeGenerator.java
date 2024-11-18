package model;

import java.util.ArrayList;
import java.util.List;
import model.interfaces.MazeGenerator;
import model.interfaces.QuestionSource;
import model.interfaces.Tile;
import model.tiles.EmptyTile;
import model.tiles.WallTile;

/**
 * Rectangular maze generator.
 *
 * @author Shane Menzies
 * @version 11/17/24
 */
public class RectangleMazeGenerator implements MazeGenerator {

    /**
     * Exception message for being provided illegal dimension.
     */
    private static final String ILLEGAL_DIMENSIONS_MESSAGE =
            "Illegal dimensions provided!";

    /**
     * Exception message for being provided illegal coordinates.
     */
    private static final String ILLEGAL_COORDINATES_MESSAGE =
            "Illegal coordinates provided!";

    /**
     * Height of the generated Maze.
     */
    private int myMazeHeight;

    /**
     * Width of the generated Maze.
     */
    private int myMazeWidth;

    /**
     * Height of the generated Room.
     */
    private int myRoomHeight;

    /**
     * Width of the generated Room.
     */
    private int myRoomWidth;

    /**
     * Index of the door in each vertical wall.
     */
    private int myVerticalDoorIndex;

    /**
     * Index of the door in each horizontal wall.
     */
    private int myHorizontalDoorIndex;

    /**
     * X-coordinate of the starting room.
     */
    private int myStartX;

    /**
     * Y-coordinate of the starting room.
     */
    private int myStartY;

    /**
     * X-coordinate of the exit room.
     */
    private int myExitX;

    /**
     * Y-coordinate of the exit room.
     */
    private int myExitY;

    /**
     * QuestionSource to get questions from.
     */
    private QuestionSource myQuestionSource;

    /**
     * Arrays of tiles for each room.
     */
    private Tile[][][][] myTiles;

    /**
     * List of DoorControllers for room doors.
     */
    private List<DoorController> myDoors;

    /**
     * Constructs a rectangular maze generator.
     */
    public RectangleMazeGenerator() {
    }

    /**
     * Sets the maze dimensions in terms of rooms.
     *
     * @param theHeight How many rooms tall a generated Maze should be.
     * @param theWidth How many rooms wide a generated Maze should be.
     */
    public void setMazeDimensions(final int theHeight, final int theWidth) {
        if (theHeight < 1
                || theWidth < 1) {
            throw new IllegalArgumentException(ILLEGAL_DIMENSIONS_MESSAGE);
        }

        myMazeHeight = theHeight;
        myMazeWidth = theWidth;
    }

    /**
     * Sets the room dimensions in terms of tiles.
     *
     * @param theHeight How many tiles tall a generated Room should be.
     * @param theWidth How many tiles wide a generated Room should be.
     */
    public void setRoomDimensions(final int theHeight, final int theWidth) {
        if (theHeight < 1
            || theWidth < 1) {
            throw new IllegalArgumentException(ILLEGAL_DIMENSIONS_MESSAGE);
        }

        myRoomHeight = theHeight;
        myVerticalDoorIndex = myRoomHeight / 2;

        myRoomWidth = theWidth;
        myHorizontalDoorIndex = myRoomWidth / 2;
    }

    /**
     * Sets the coordinates of the generated Maze's starting room.
     *
     * @param theStartX X-coordinate for the starting room.
     * @param theStartY Y-coordinate for the starting room.
     */
    public void setStartCoordinates(final int theStartX, final int theStartY) {
        if (theStartX < 0
            || theStartY < 0) {
            throw new IllegalArgumentException(ILLEGAL_COORDINATES_MESSAGE);
        }

        myStartX = theStartX;
        myStartY = theStartY;
    }

    /**
     * Sets the coordinates for the generated Maze's exit room.
     *
     * @param theExitX X-coordinate for the exit room.
     * @param theExitY Y-coordinate for the exit room.
     */
    public void setExitCoordinates(final int theExitX, final int theExitY) {
        if (theExitX < 0
                || theExitY < 0) {
            throw new IllegalArgumentException(ILLEGAL_COORDINATES_MESSAGE);
        }

        myExitX = theExitX;
        myExitY = theExitY;
    }

    public void setQuestionsSource(final QuestionSource theNewSource) {
        myQuestionSource = theNewSource;
    }

    @Override
    public Maze generate() {

        // Make room and tile arrays
        final Room[][] rooms = new Room[myMazeHeight][myMazeWidth];
        myTiles = new Tile[myMazeHeight][myMazeWidth][myRoomHeight][myRoomWidth];
        myDoors = new ArrayList<>();

        // Internal Room borders
        makeAllRoomBorders();

        // Create rooms from prepared tiles.
        for (int i = 0; i < myMazeHeight; i++) {
            for (int j = 0; j < myMazeWidth; j++) {
                rooms[i][j] = prepareRoom(j, i);
            }
        }

        // Print out generated maze
        for (int y = (myMazeHeight * myRoomHeight) - 1; y >= 0; y--) {
            for (int x = 0; x < (myMazeWidth * myRoomWidth); x++) {
                System.out.print(myTiles[y / myRoomHeight][x / myRoomWidth]
                                 [y % myRoomHeight][x % myRoomWidth]);
            }
            System.out.println();
        }

        // Can finally put together Maze.
        return new Maze(
                rooms, myDoors.toArray(new DoorController[0]),
                myStartX, myStartY,
                myExitX, myExitY
        );
    }

    /**
     * Makes the necessary borders for all the rooms in the maze.
     */
    private void makeAllRoomBorders() {
        for (int roomY = 0; roomY < myMazeHeight; roomY++) {
            for (int roomX = 0; roomX < myMazeWidth; roomX++) {
                fillRoomInterior(roomX, roomY);
                makeRoomBorders(roomX, roomY);
            }
        }
    }

    /**
     * Fills the interior of a room with empty tiles.
     *
     * @param theRoomX X-coordinate of the room.
     * @param theRoomY Y-coordinate of the room.
     */
    private void fillRoomInterior(final int theRoomX, final int theRoomY) {
        for (int y = 1; y < (myRoomHeight - 1); y++) {
            for (int x = 1; x < (myRoomWidth - 1); x++) {
                myTiles[theRoomY][theRoomX][y][x]
                        = new EmptyTile();
            }
        }
    }

    private void makeRoomBorders(final int theRoomX, final int theRoomY) {
        // Right and Left
        for (int y = 0; y < myRoomHeight; y++) {
            if (y != myVerticalDoorIndex) {
                myTiles[theRoomY][theRoomX][y][0]
                        = new WallTile();
                myTiles[theRoomY][theRoomX][y][myRoomWidth - 1]
                        = new WallTile();
            }
        }

        // Top and Bottom
        for (int x = 0; x < myRoomWidth; x++) {
            if (x != myHorizontalDoorIndex) {
                myTiles[theRoomY][theRoomX][0][x]
                        = new WallTile();
                myTiles[theRoomY][theRoomX][myRoomHeight - 1][x]
                        = new WallTile();
            }
        }

        // Need to fill bottom and left edges if this room has no neighbors
        // in those directions.
        if (theRoomY == 0) {
            // No bottom door
            myTiles[theRoomY][theRoomX][0][myHorizontalDoorIndex]
                    = new WallTile();
        }
        if (theRoomX == 0) {
            // No left door
            myTiles[theRoomY][theRoomX][myVerticalDoorIndex][0]
                    = new WallTile();
        }

        // Top door
        if (theRoomY == (myMazeHeight - 1)) {
            // No top door
            myTiles[theRoomY][theRoomX][myRoomHeight - 1][myHorizontalDoorIndex]
                   = new WallTile();
        } else {
            // Make a top door
            final DoorController topDoor
                    = new DoorController(myQuestionSource.getQuestion());
            myDoors.add(topDoor);
            myTiles[theRoomY][theRoomX][myRoomHeight - 1][myHorizontalDoorIndex]
                    = topDoor.getDoors()[0];
            myTiles[theRoomY + 1][theRoomX][0][myHorizontalDoorIndex]
                    = topDoor.getDoors()[1];
        }

        // Right door
        if (theRoomX == (myMazeWidth - 1)) {
            // No right door
            myTiles[theRoomY][theRoomX][myVerticalDoorIndex][myRoomWidth - 1]
                    = new WallTile();
        } else {
            // Make a right door
            final DoorController rightDoor
                    = new DoorController(myQuestionSource.getQuestion());
            myDoors.add(rightDoor);
            myTiles[theRoomY][theRoomX][myVerticalDoorIndex][myRoomWidth - 1]
                    = rightDoor.getDoors()[0];
            myTiles[theRoomY][theRoomX + 1][myVerticalDoorIndex][0]
                    = rightDoor.getDoors()[1];
        }
    }

    /**
     * Creates a single room from the prepared tiles.
     *
     * @param theRoomX X-coordinate of room to create.
     * @param theRoomY Y-coordinate of room to create.
     * @return Newly created Room with appropriate tiles.
     */
    private Room prepareRoom(final int theRoomX, final int theRoomY) {
        // Tiles should already have been set.

        // Determine correct type
        Room.RoomType newRoomType = Room.RoomType.STANDARD;
        if (theRoomY == myStartY
                && theRoomX == myStartX) {
            newRoomType = Room.RoomType.START;
        } else if (theRoomY == myExitY
                && theRoomX == myExitX) {
            newRoomType = Room.RoomType.EXIT;
        }

        // Make room
        return new Room(newRoomType,
                myTiles[theRoomY][theRoomX]);

    }
}