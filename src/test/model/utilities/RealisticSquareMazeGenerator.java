package model.utilities;

import model.Maze;
import model.RectangleMazeGenerator;
import model.TriviaQuestion;
import model.interfaces.MazeGenerator;

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

        myStartX = theStartX;
        myStartY = theStartY;

        myExitX = theExitX;
        myExitY = theExitY;

        myQuestion = theQuestion;
    }

    @Override
    public Maze generate() {
        RectangleMazeGenerator rectangleMazeGenerator = new RectangleMazeGenerator();

        rectangleMazeGenerator.setMazeDimensions(myMazeSize, myMazeSize);
        rectangleMazeGenerator.setRoomDimensions(myRoomSize, myRoomSize);
        rectangleMazeGenerator.setStartCoordinates(myStartX, myStartY);
        rectangleMazeGenerator.setExitCoordinates(myExitX, myExitY);
        rectangleMazeGenerator.setQuestionsSource(() -> myQuestion);

        return rectangleMazeGenerator.generate();
    }
}