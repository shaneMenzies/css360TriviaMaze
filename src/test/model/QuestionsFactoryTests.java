package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for the QuestionsFactory class.
 *
 * @author Shane Menzies
 * @version 11/2/24
 */
class QuestionsFactoryTests {

    /**
     * Tests QuestionsFactory's getInstance() method.
     */
    @Test
    void getInstance() {
        final QuestionsFactory firstReference = QuestionsFactory.getInstance();
        assertNotNull(firstReference,
                "QuestionsFactory.getInstance() returned a null QuestionsFactory!");

        assertSame(firstReference, QuestionsFactory.getInstance(),
                "QuestionsFactory.getInstance() returned 2 different instances " +
                        "of QuestionsFactory!");
    }

    /**
     * Tests QuestionDatabase's getQuestion() method.
     */
    @Test
    void getQuestion() {
        final TriviaQuestion first = QuestionsFactory.getInstance().getQuestion();
        assertNotNull(first,
                "QuestionsFactory.getQuestion() returned a null question!");

        final TriviaQuestion second = QuestionsFactory.getInstance().getQuestion();

        assertNotEquals(first, second,
                "QuestionsFactory.getQuestion() returned a duplicate question!");
    }
}