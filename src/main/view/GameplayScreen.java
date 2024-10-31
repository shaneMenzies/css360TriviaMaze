package view;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the main view for the game screen. Displays map, avatar, direction buttons
 * and menu bar.
 *
 * @author Cynthia Lopez
 * @version 10/28/24
 */
public class GameplayScreen extends JFrame {
    private final JFrame myGameWindow = new JFrame();
    private final Color backgroundColor = new Color(51,51,51);

    public GameplayScreen() {
        setUpGUI();
    }

    private void setUpGUI() {
        frame();
    }
    private void frame() {
        myGameWindow.setSize(800,700);
        myGameWindow.setLocation(600, 200);
        myGameWindow.setResizable(false);
        myGameWindow.setBackground(backgroundColor);
        myGameWindow.setTitle("Level Up");
        myGameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myGameWindow.setVisible(true);
    }
}