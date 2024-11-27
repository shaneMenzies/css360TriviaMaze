package view;

import model.GameState;
import model.TriviaQuestion;
import javax.swing.*;
import java.awt.*;

/**
 * Represents the display for the questions in the main game frame.
 * Displays multiple choice, true/false, and short answer questions.
 *
 * @author Cynthia Lopez
 * @version 11/20/24
 */
public class QuestionsPanel extends JPanel {

    /** The current game state */
    private final GameState myGameState;

    /**
     * Constructors question panel with the current game state.
     *
     * @param theGameState current game state to provide question and answers.
     */
    public QuestionsPanel(GameState theGameState) {
        myGameState = theGameState;
        setBounds(10, 500, 300, 180);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

    }

    /**
     * Sets up panel for questions, adds buttons, text fields and displays message, if
     * no question is available.
     */
    public void displayQuestion() {
        removeAll();

        TriviaQuestion question = myGameState.getQuestion();

        if (question == null) {
            JLabel noQuestionLabel = new JLabel("No question available"); // debug message
            add(noQuestionLabel);
            revalidate();
            repaint();
            return;
        }

        JLabel questionLabel = new JLabel(question.getQuestion());
        add(questionLabel);

        switch (question.getType()) {
            case MULTIPLE_CHOICE -> {
                final JButton answer1Button = new JButton("A");
                add(answer1Button);

                final JButton answer2Button = new JButton("B");
                add(answer2Button);

                final JButton answer3Button = new JButton("C");
                add(answer3Button);

                final JButton answer4Button = new JButton("D");
                add(answer4Button);

                answer1Button.addActionListener(e -> myGameState.answerQuestion("A"));
                answer2Button.addActionListener(e -> myGameState.answerQuestion("B"));
                answer3Button.addActionListener(e -> myGameState.answerQuestion("C"));
                answer4Button.addActionListener(e -> myGameState.answerQuestion("D"));
            } case TRUE_FALSE -> {
                JButton trueButton = new JButton("True");
                JButton falseButton = new JButton("False");
                trueButton.addActionListener(e -> myGameState.answerQuestion("True"));
                falseButton.addActionListener(e -> myGameState.answerQuestion("False"));

                add(trueButton);
                add(falseButton);
            } case SHORT_ANSWER -> {
                JTextField answerField = new JTextField(15);
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(e -> myGameState.answerQuestion(answerField.getText()));

                add(answerField);
                add(submitButton);
            }
        }
        revalidate();
        repaint();
    }
}