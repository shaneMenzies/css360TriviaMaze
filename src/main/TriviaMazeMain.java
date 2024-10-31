import controller.TitleController;
import view.TitleScreen;

public final class TriviaMazeMain {
    /**
     * Private constructor to prevent instantiation.
     */
    private TriviaMazeMain() { }

    public static void main(final String[] theArgs) {
        System.out.println("TriviaMaze: TODO");
        TitleScreen view = new TitleScreen();
        new TitleController(view);
    }
}
