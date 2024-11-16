package model.utilities;

import java.util.ArrayList;
import java.util.List;
import model.DoorController;
import model.Maze;
import model.Room;
import model.TriviaQuestion;
import model.interfaces.MazeGenerator;
import model.interfaces.Tile;
import model.tiles.EmptyTile;
import model.tiles.WallTile;

public class RealisticSquareMazeGenerator implements MazeGenerator {

    /**
     * Size of the generated Maze,
     * it will be a square with this value as the length of each side.
     */
    final private int myMazeSize;

    /**
     * Size of each generated Room,
     * each will be a square with the value as the length of each side.
     */
    final private int myRoomSize;

    /**
     * Index of the door in each wall.
     */
    final private int myDoorIndex;

    /**
     * X-coordinate of the starting room.
     */
    final private int myStartX;

    /**
     * Y-coordinate of the starting room.
     */
    final private int myStartY;

    /**
     * X-coordinate of the exit room.
     */
    final private int myExitX;

    /**
     * Y-coordinate of the exit room.
     */
    final private int myExitY;

    /**
     * Trivia Question to give to all doors.
     */
    final private TriviaQuestion myQuestion;

    /**
     * Arrays of tiles for each room.
     */
    private Tile[][][][] myTiles;

    /**
     * Array for Rooms inside the maze.
     */
    private Room[][] myRooms;

    /**
     * List of DoorControllers for room doors.
     */
    private List<DoorController> myDoors;

    /**
     * Constructs a RealisticSquareMazeGenerator for creating realistic square
     *  mazes.
     *
     * @param theMazeSize Length and width of generated Maze.
     * @param theRoomSize Length and width of rooms inside generated Maze.
     * @param theStartX X-coordinate of starting room.
     * @param theStartY Y-coordinate of starting room.
     * @param theExitX X-coordinate of exit room.
     * @param theExitY Y-coordinate of exit room.
     */
    public RealisticSquareMazeGenerator(final int theMazeSize, final int theRoomSize,
                                        final int theStartX, final int theStartY,
                                        final int theExitX, final int theExitY,
                                        final TriviaQuestion theQuestion) {
        myMazeSize = theMazeSize;
        myRoomSize = theRoomSize;
        myDoorIndex = myRoomSize / 2;

        myStartX = theStartX;
        myStartY = theStartY;

        myExitX = theExitX;
        myExitY = theExitY;

        myQuestion = theQuestion;
    }

    @Override
    public Maze generate() {

        // Make room and tile arrays
        myRooms = new Room[myMazeSize][myMazeSize];
        myTiles = new Tile[myMazeSize][myMazeSize][myRoomSize][myRoomSize];
        myDoors = new ArrayList<>();

        // Edge borders
        makeMazeBorders();

        // Internal Room borders
        makeRoomBorders();

        // Create rooms from prepared tiles.
        for (int i = 0; i < myMazeSize; i++) {
            for (int j = 0; j < myMazeSize; j++) {
                myRooms[i][j] = prepareRoom(j, i);
            }
        }

        // Print out generated maze
        for (int y = (myMazeSize * myRoomSize) - 1; y >= 0; y--) {
            for (int x = 0; x < (myMazeSize * myRoomSize); x++) {
                System.out.print(myTiles[y / myRoomSize][x / myRoomSize]
                                 [y % myRoomSize][x % myRoomSize]);
            }
            System.out.println();
        }

        // Can finally put together Maze.
        return new Maze(
                myRooms, myDoors.toArray(new DoorController[0]),
                myStartX, myStartY,
                myExitX, myExitY
        );
    }

    /**
     * Makes the exterior maze borders.
     */
    private void makeMazeBorders() {
        for (int x = 0; x < myMazeSize * myRoomSize; x++) {
            myTiles[0][x / myRoomSize]
                    [0][x % myRoomSize]
                    = new WallTile();
            myTiles[myMazeSize - 1][x / myRoomSize]
                    [myMazeSize - 1][x % myRoomSize]
                    = new WallTile();
        }
        for (int y = 0; y < myMazeSize * myRoomSize; y++) {
            myTiles[y / myRoomSize][0]
                    [y % myRoomSize][0]
                    = new WallTile();
            myTiles[y / myRoomSize][myMazeSize - 1]
                    [y % myRoomSize][myMazeSize - 1]
                    = new WallTile();
        }
    }

    /**
     * Makes the necessary borders for all the rooms in the maze.
     */
    private void makeRoomBorders() {
        int roomY = 0;
        int roomX = 0;
        for (roomY = 0; roomY < (myMazeSize - 1); roomY++) {
            for (roomX = 0; roomX < (myMazeSize - 1); roomX++) {
                fillRoomInterior(roomX, roomY);
                makeRoomRightBorder(roomX, roomY);
                makeRoomTopBorder(roomX, roomY);
            }

            // Last in row only needs top edge
            fillRoomInterior(roomX, roomY);
            makeRoomTopBorder(roomX, roomY);
        }

        // Last row doesn't need top borders
        for (roomX = 0; roomX < (myMazeSize - 1); roomX++) {
            fillRoomInterior(roomX, roomY);
            makeRoomRightBorder(roomX, roomY);
        }

        // Very last one only needs the interior
        fillRoomInterior(roomX, roomY);
    }

    /**
     * Fills the interior of a room with empty tiles.
     *
     * @param theRoomX X-coordinate of the room.
     * @param theRoomY Y-coordinate of the room.
     */
    private void fillRoomInterior(final int theRoomX, final int theRoomY) {
        for (int y = 1; y < (myRoomSize - 1); y++) {
            for (int x = 1; x < (myRoomSize - 1); x++) {
                myTiles[theRoomY][theRoomX][y][x]
                        = new EmptyTile();
            }
        }
    }

    /**
     * Makes the right border for a room.
     *
     * @param theRoomX X-coordinate of the room.
     * @param theRoomY Y-coordinate of the room.
     */
    private void makeRoomRightBorder(final int theRoomX, final int theRoomY) {
        // Right border
        for (int y = 0; y < myRoomSize; y++) {
            if (y != myDoorIndex) {
                myTiles[theRoomY][theRoomX][y][myRoomSize - 1]
                        = new WallTile();
            }
        }

        // Right door
        final DoorController rightDoor = new DoorController(myQuestion);
        myDoors.add(rightDoor);
        myTiles[theRoomY][theRoomX][myDoorIndex][myRoomSize - 1]
                = rightDoor.getDoors()[0];
        myTiles[theRoomY][theRoomX + 1][myDoorIndex][0]
                = rightDoor.getDoors()[1];
    }

    /**
     * Makes the top border for a room.
     *
     * @param theRoomX X-coordinate of the room.
     * @param theRoomY Y-coordinate of the room.
     */
    private void makeRoomTopBorder(final int theRoomX, final int theRoomY) {
        for (int x = 0; x < myRoomSize; x++) {
            if (x != myDoorIndex) {
                myTiles[theRoomY][theRoomX][myRoomSize - 1][x]
                        = new WallTile();
            }
        }

        // Top door
        final DoorController topDoor = new DoorController(myQuestion);
        myDoors.add(topDoor);
        myTiles[theRoomY][theRoomX][myRoomSize - 1][myDoorIndex]
                = topDoor.getDoors()[0];
        myTiles[theRoomY + 1][theRoomX][0][myDoorIndex]
                = topDoor.getDoors()[1];
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