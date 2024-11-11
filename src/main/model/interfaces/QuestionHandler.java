package model.interfaces;

import model.TriviaQuestion;

/**
 * Interface for a question handler to handle asking a question
 * and handling the user's input as an answer.
 *
 * @author Shane Menzies
 * @version 11/10/24
 */
public interface QuestionHandler {

    /**
     * Starts the process of asking a question to the user.
     * Also takes a callback to return the result of the question
     * when it has been answered.
     *
     * @param theQuestion Question to be asked.
     * @param theCallback Callback method to receive the result of the question.
     * @throws QuestionRejectedException If this handler was already
     *  busy with another question.
     */
    void askQuestion(TriviaQuestion theQuestion,
                     AnswerCallback theCallback)
                    throws QuestionRejectedException;

    /**
     * Simple callback interface for handing the correctness of the user's answer back
     * to the original asking party once it has been answered.
     */
    interface AnswerCallback {
        /**
         * Returns the correctness of the user's answer.
         *
         * @param theResult Result of the question from the user's answer.
         */
        void call(QuestionResult theResult);
    }

    class QuestionRejectedException extends Exception {
        public QuestionRejectedException(final String theMessage) {
            super(theMessage);
        }
    }

    /**
     * Possible results for when a question is answered.
     */
    enum QuestionResult {
        /**
         * Answered correctly.
         */
        CORRECT,

        /**
         * Answered incorrectly.
         */
        INCORRECT,

        /**
         * Cancelled the question.
         */
        CANCELLED,
    }
}