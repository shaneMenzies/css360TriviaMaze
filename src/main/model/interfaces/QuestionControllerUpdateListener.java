package model.interfaces;

import model.QuestionController;

/**
 * Basic listener interface to be notified when
 * a QuestionController changes state.
 *
 * @author Shane Menzies
 * @version 11/10/24
 */
public interface QuestionControllerUpdateListener {

    /**
     * Handles an update in the specified Question Controller.
     *
     * @param theUpdateType Type of change in the QuestionController.
     * @param theQuestionController QuestionController which updated.
     */
    void doUpdate(UpdateType theUpdateType, QuestionController theQuestionController);

    enum UpdateType {
        /**
         * Indicates a new question was added.
         */
        NEW_QUESTION,

        /**
         * Indicates the question was answered correctly.
         */
        ANSWERED_CORRECTLY,

        /**
         * Indicates the question was answered incorrectly.
         */
        ANSWERED_INCORRECTLY,

        /**
         * Indicates the question was cancelled.
         */
        CANCELLED
    }
}