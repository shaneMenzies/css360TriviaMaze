package controller;

import model.GameModel;
import model.QuestionsFactory;
import model.RectangleMazeGenerator;
import view.TitleScreen;

public final class TriviaMazeMain {
    /**
     * Private constructor to prevent instantiation.
     */
    private TriviaMazeMain() { }

    public static void main(final String[] theArgs) {
        final RectangleMazeGenerator rmg = new RectangleMazeGenerator(6, 6,
                5, 5, QuestionsFactory.getInstance());
        final GameModel gameModel = new GameModel(rmg, QuestionsFactory.getInstance());
        new TitleScreen(gameModel);
    }
}
