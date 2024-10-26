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
    private final String myQuestion;

    /**
     * Answer part of this question.
     */
    private final String myAnswer;

    /**
     * Type of this question.
     */
    private final QuestionType myType;

    /**
     * Creates a new TriviaQuestion with the provided
     *  values for the question, answer, and type.
     *
     * @param theQuestion Question text itself
     * @param theAnswer Answer to the question
     * @param theType Type of the question.
     */
    public TriviaQuestion(final String theQuestion,
                          final String theAnswer, final QuestionType theType) {
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

        /**
         * A multiple choice question.
         */
        MULTIPLE_CHOICE,

        /**
         * A true or false question.
         */
        TRUE_FALSE,

        /**
         * A short answer question.
         */
        SHORT_ANSWER,
    }

}