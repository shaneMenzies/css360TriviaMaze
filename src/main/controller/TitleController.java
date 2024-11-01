package controller;

import view.TitleScreen;
import view.GameplayScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The controller for the title screen. It listens to the action on the "Play" and
 * "exit" buttons and handles them by either starting the game or exiting the application.
 *
 * @author Cynthia Lopez
 * @version 10/28/24
 */
public class TitleController implements ActionListener {

    /**
     * Sets up action listeners for the "Play" and "Exit" buttons.
     *
     * @param theView title screen view object where buttons are displayed.
     */
    public TitleController(TitleScreen theView) {
        theView.getStartButton().addActionListener(new StartButtonListener(theView));
        theView.getExitButton().addActionListener(new ExitButtonListener());
    }

    /**
     * To allow TitleController to not be declared as abstract.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // no actions needed here.
    }

    /**
     * Handles the event where the start button is pressed. It initializes
     * the gameplay screen and closes the title screen.
     *
     * @param titleScreen TitleScreen reference in order to perform events.
     */
    private record StartButtonListener(TitleScreen titleScreen) implements ActionListener {

        /**
         * Launches the gameplay screen and closes the title screen.
         *
         * @param theEvent the event to be processed.
         */
        @Override
            public void actionPerformed(ActionEvent theEvent) {
                new GameplayScreen();
                titleScreen.disposeTitleWindow();
            }
        }

    /**
     * Handles the event when the exit button is pressed. It closes the application.
     */
    private static class ExitButtonListener implements ActionListener {

        /**
         * Closes the application.
         *
         * @param theEvent the event to be processed.
         */
        @Override
        public void actionPerformed(ActionEvent theEvent) {
            System.exit(0);
        }
    }
}
