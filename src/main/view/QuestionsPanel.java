package view;

import model.GameModel;
import model.TriviaQuestion;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Represents the display for the questions in the main game frame.
 * Displays multiple choice, true/false, and short answer questions.
 *
 * @author Cynthia Lopez
 * @version 11/20/24
 */
public class QuestionsPanel extends JPanel {

    /** The current game model. */
    private final GameModel myGameModel;

    /** Game model object for logic. */
    private Font myFont;

    /**
     * Constructors question panel with the current game state.
     *
     * @param theGameModel current game state to provide question and answers.
     */
    public QuestionsPanel(final GameModel theGameModel) {
        myGameModel = theGameModel;
        setBounds(10, 500, 500, 180);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    }

    /**
     * Sets up panel for questions, add labels and displays message, if
     * no question is available.
     */
    public void displayQuestion() {
        removeAll();
        customText();

        final TriviaQuestion question = myGameModel.getState().getQuestion();

        if (question == null) {
            final JLabel noQuestionLabel = new JLabel("No question available");
            noQuestionLabel.setFont(myFont);
            noQuestionLabel.setForeground(Color.WHITE);
            add(noQuestionLabel);
            revalidate();
            repaint();
            return;
        }

        final JLabel questionLabel = new JLabel(question.getQuestion());
        questionLabel.setFont(myFont);
        questionLabel.setForeground(Color.WHITE);
        add(questionLabel);

        switch (question.getType()) {
            case MULTIPLE_CHOICE -> multipleChoice();
            case TRUE_FALSE -> trueFalse();
            case SHORT_ANSWER -> shortAnswer();
        }

        revalidate();
        repaint();
    }

    /** handles rendering/display for multiple choice questions with buttons. */
    private void multipleChoice() {
        final JButton answer1Button = new JButton("A");
        answer1Button.setFont(myFont);
        add(answer1Button);

        final JButton answer2Button = new JButton("B");
        answer2Button.setFont(myFont);
        add(answer2Button);

        final JButton answer3Button = new JButton("C");
        answer3Button.setFont(myFont);
        add(answer3Button);

        final JButton answer4Button = new JButton("D");
        answer4Button.setFont(myFont);
        add(answer4Button);

        answer1Button.addActionListener(e -> myGameModel.getState().answerQuestion("A"));
        answer2Button.addActionListener(e -> myGameModel.getState().answerQuestion("B"));
        answer3Button.addActionListener(e -> myGameModel.getState().answerQuestion("C"));
        answer4Button.addActionListener(e -> myGameModel.getState().answerQuestion("D"));
    }

    /** handles rendering/display for true/false questions with buttons. */
    private void trueFalse() {
        final JButton trueButton = new JButton("True");
        final JButton falseButton = new JButton("False");

        trueButton.addActionListener(e -> myGameModel.getState().answerQuestion("True"));
        falseButton.addActionListener(e -> myGameModel.getState().answerQuestion("False"));

        trueButton.setFont(myFont);
        falseButton.setFont(myFont);

        add(trueButton);
        add(falseButton);
    }

    /** handles rendering/display for short answer questions with text field and submit button. */
    private void shortAnswer() {
        final JTextField answerField = new JTextField(15);
        final JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> myGameModel.getState().answerQuestion(answerField.getText()));

        answerField.setFont(myFont);
        submitButton.setFont(myFont);

        add(answerField);
        add(submitButton);
    }

    /** custom text for questions, answers and buttons. */
    private void customText() {
        try {
            myFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("resources/fonts/pixel_font_reg.ttf")).deriveFont(20f);

            final GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();

            g.registerFont(Font.createFont(Font.TRUETYPE_FONT,
                    new File("resources/fonts/pixel_font_reg.ttf")));

        } catch (final IOException | FontFormatException exception) {
            myFont = new Font("Times New Roman", Font.BOLD, 20);
        }
    }
}