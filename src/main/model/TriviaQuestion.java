package model;

/**
 * Represents a trivia question and its associated answer.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public class TriviaQuestion {

    /**
     * Question part of this question.
     */
    private String myQuestion;

    /**
     * Answer part of this question.
     */
    private String myAnswer;

    /**
     * Type of this question.
     */
    private QuestionType myType;

    /**
     * Creates a new TriviaQuestion with the provided values for the question, answer, and type.
     *
     * @param theQuestion Question text itself
     * @param theAnswer Answer to the question
     * @param theType Type of the question.
     */
    public TriviaQuestion(String theQuestion, String theAnswer, QuestionType theType) {
        myQuestion = theQuestion;
        myAnswer = theAnswer;
        myType = theType;
    }

    /**
     * Gets this question's text.
     *
     * @return This question's text.
     */
    public String getQuestion() {
        return myQuestion;
    }

    /**
     * Gets this question's answer.
     *
     * @return This question's answer.
     */
    public String getAnswer() {
        return myAnswer;
    }

    /**
     * Gets this question's type.
     *
     * @return This question's type.
     */
    public QuestionType getType() {
        return myType;
    }

    public enum QuestionType {
        MultipleChoice,
        TrueFalse,
        ShortAnswer,
    }

}