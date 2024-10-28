package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.Music;
import javax.swing.Timer;

public class TitleScreen extends JFrame {
    private final JFrame myTitleWindow = new JFrame();
    private final JButton myStart;
    private final JButton myExit;
    private JLabel myTitleLabel;
    private int myTitleScreenY = 0;
    private final JPanel myLayout = new JPanel();


    public TitleScreen() {
        myStart = new JButton("Play");
        myExit = new JButton("Exit");

        setUpTitle();
    }

    private void setUpTitle() {
        moveTitle();
        buttons();
        frame();
        backgroundMusic();
    }

    private void frame() {
        myTitleWindow.setLocation(700, 200);
        myTitleWindow.setSize(558, 732);
        myTitleWindow.setResizable(false);
        myTitleWindow.setTitle("Trivia Maze");
        myTitleWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTitleWindow.setVisible(true);


        ImageIcon imageTV = new ImageIcon("src/images/title_background.jpg");
        JLabel tvLabel = new JLabel(imageTV);

        myTitleWindow.add(tvLabel);
    }

    private void buttons() {
        myLayout.add(myStart);
        myLayout.add(myExit);
        myTitleWindow.add(myLayout, BorderLayout.SOUTH);
    }

    private void moveTitle() {
        ImageIcon imageTitle = new ImageIcon("src/images/title.png");
        myTitleLabel = new JLabel(imageTitle);

        Timer titleScreenTimer = new Timer(40, new ActionListener() {
            boolean movingUp = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if(movingUp) {
                    myTitleScreenY -= 1;
                    if(myTitleScreenY <= 0) {
                        movingUp = false;
                    }
                }
                else {
                    myTitleScreenY += 1;
                    if(myTitleScreenY >= 30) {
                        movingUp = true;
                    }
                }
                myTitleLabel.setLocation(getX(), myTitleScreenY);
            }
        });
        titleScreenTimer.start();
        myTitleWindow.add(myTitleLabel);
    }

    private void backgroundMusic() {
        Music music = new Music();
        music.getMusic("src/sounds/game-music-teste-1-204326.wav");
    }
}
