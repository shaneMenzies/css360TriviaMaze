package model.interfaces;

import model.TriviaQuestion;

/**
 * Interface representing a source of trivia questions.
 *
 * @author Shane Menzies
 * @version 11/17/24
 */
public interface QuestionSource {

    /**
     * Gets a new question from this source.
     *
     * @return New Trivia Question.
     */
    TriviaQuestion getQuestion();
}