package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Handles the game over and winning panels.
 *
 * @author Cynthia Lopez
 * @version 12/05/2024
 */
public class OutcomePanel extends JPanel {

    /** Game model object for logic. */
    private Font myFont;

    /** Constructs the outcome panel to display the results to the player. */
    public OutcomePanel() {
        setBounds(290, 10, 900, 450);
        setLayout(new GridLayout(2,1));
        setBackground(Color.BLACK);

    }

    /** The game over screen. */
    public void endScreen() {
        removeAll();
        customText();

        final JLabel loseLabel = new JLabel("GAME OVER");
        loseLabel.setFont(myFont);
        loseLabel.setForeground(Color.WHITE);
        add(loseLabel);

        revalidate();
        repaint();

    }

    /** The win screen. */
    public void winScreen() {
        removeAll();
        customText();

        final JLabel winLabel = new JLabel("YOU WON!");
        winLabel.setFont(myFont);
        winLabel.setForeground(Color.WHITE);
        add(winLabel);

        revalidate();
        repaint();
    }

    /** Custom text for questions, answers and buttons. */
    private void customText() {
        try {
            myFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("resources/fonts/pixel_font_reg.ttf")).deriveFont(70f);

            final GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();

            g.registerFont(Font.createFont(Font.TRUETYPE_FONT,
                    new File("resources/fonts/pixel_font_reg.ttf")));

        } catch (final IOException | FontFormatException exception) {
            myFont = new Font("Times New Roman", Font.BOLD, 20);
        }
    }
}
