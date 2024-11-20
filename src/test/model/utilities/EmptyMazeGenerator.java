package model.utilities;

import model.Maze;
import model.Room;
import model.interfaces.MazeGenerator;
import model.interfaces.Tile;
import model.tiles.EmptyTile;

public class EmptyMazeGenerator implements MazeGenerator {

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
     * Arrays of tiles for each room.
     */
    private Tile[][][][] myTiles;

    /**
     * Array for Rooms inside the maze.
     */
    private Room[][] myRooms;


    /**
     * Constructs an EmptyMazeGenerator for creating empty mazes.
     *
     * @param theMazeSize Length and width of generated Maze.
     * @param theRoomSize Length and width of rooms inside generated Maze.
     * @param theStartX X-coordinate of starting room.
     * @param theStartY Y-coordinate of starting room.
     * @param theExitX X-coordinate of exit room.
     * @param theExitY Y-coordinate of exit room.
     */
    public EmptyMazeGenerator(final int theMazeSize, final int theRoomSize,
                              final int theStartX, final int theStartY,
                              final int theExitX, final int theExitY) {
        myMazeSize = theMazeSize;
        myRoomSize = theRoomSize;

        myStartX = theStartX;
        myStartY = theStartY;

        myExitX = theExitX;
        myExitY = theExitY;
    }

    @Override
    public Maze generate() {
        myRooms = new Room[myMazeSize][myMazeSize];
        myTiles = new Tile[myMazeSize][myMazeSize][myRoomSize][myRoomSize];

        // Setup rooms
        for (int i = 0; i < myMazeSize; i++) {
            for (int j = 0; j < myMazeSize; j++) {
                myRooms[i][j] = prepareEmptyRoom(j, i);
            }
        }

        // Can put together maze.
         return new Maze(
                myRooms, null,
                myStartX, myStartY,
                myExitX, myExitY
        );

    }

    /**
     * Prepares a single room inside the maze.
     *
     * @param theRoomX X-coordinate of target room.
     * @param theRoomY Y-coordiante of target room.
     * @return Newly created (empty) room.
     */
    private Room prepareEmptyRoom(final int theRoomX, final int theRoomY) {
        // Clear tiles
        for (int y = 0; y < myRoomSize; y++) {
            for (int x = 0; x < myRoomSize; x++) {
                myTiles[theRoomY][theRoomX][y][x] = new EmptyTile();
            }
        }

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