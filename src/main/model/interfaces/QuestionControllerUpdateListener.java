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
     * @param theQuestionController QuestionController which updated.
     */
    void doUpdate(QuestionController theQuestionController);
}