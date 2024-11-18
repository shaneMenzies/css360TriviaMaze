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
        final Room[][] myRooms = new Room[myMazeSize][myMazeSize];
        myTiles = new Tile[myMazeSize][myMazeSize][myRoomSize][myRoomSize];
        myDoors = new ArrayList<>();

        // Internal Room borders
        makeAllRoomBorders();

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
     * Makes the necessary borders for all the rooms in the maze.
     */
    private void makeAllRoomBorders() {
        for (int roomY = 0; roomY < myMazeSize; roomY++) {
            for (int roomX = 0; roomX < myMazeSize; roomX++) {
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
        for (int y = 1; y < (myRoomSize - 1); y++) {
            for (int x = 1; x < (myRoomSize - 1); x++) {
                myTiles[theRoomY][theRoomX][y][x]
                        = new EmptyTile();
            }
        }
    }

    private void makeRoomBorders(final int theRoomX, final int theRoomY) {
        // Right and Left
        for (int y = 0; y < myRoomSize; y++) {
            if (y != myDoorIndex) {
                myTiles[theRoomY][theRoomX][y][0]
                        = new WallTile();
                myTiles[theRoomY][theRoomX][y][myRoomSize - 1]
                        = new WallTile();
            }
        }

        // Top and Bottom
        for (int x = 0; x < myRoomSize; x++) {
            if (x != myDoorIndex) {
                myTiles[theRoomY][theRoomX][0][x]
                        = new WallTile();
                myTiles[theRoomY][theRoomX][myRoomSize - 1][x]
                        = new WallTile();
            }
        }

        // Need to fill bottom and left edges if this room has no neighbors
        // in those directions.
        if (theRoomY == 0) {
            // No bottom door
            myTiles[theRoomY][theRoomX][0][myDoorIndex]
                    = new WallTile();
        }
        if (theRoomX == 0) {
            // No left door
            myTiles[theRoomY][theRoomX][myDoorIndex][0]
                    = new WallTile();
        }

        // Top door
        if (theRoomY == (myMazeSize - 1)) {
           // No top door
           myTiles[theRoomY][theRoomX][myRoomSize - 1][myDoorIndex]
                   = new WallTile();
        } else {
            // Make a top door
            final DoorController topDoor = new DoorController(myQuestion);
            myDoors.add(topDoor);
            myTiles[theRoomY][theRoomX][myRoomSize - 1][myDoorIndex]
                    = topDoor.getDoors()[0];
            myTiles[theRoomY + 1][theRoomX][0][myDoorIndex]
                    = topDoor.getDoors()[1];
        }

        // Right door
        if (theRoomX == (myMazeSize - 1)) {
            // No right door
            myTiles[theRoomY][theRoomX][myDoorIndex][myRoomSize - 1]
                    = new WallTile();
        } else {
            // Make a right door
            final DoorController rightDoor = new DoorController(myQuestion);
            myDoors.add(rightDoor);
            myTiles[theRoomY][theRoomX][myDoorIndex][myRoomSize - 1]
                    = rightDoor.getDoors()[0];
            myTiles[theRoomY][theRoomX + 1][myDoorIndex][0]
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