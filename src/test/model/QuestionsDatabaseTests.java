package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for the QuestionsDatabase class.
 *
 * @author Shane Menzies
 * @version 11/2/24
 */
class QuestionsDatabaseTests {

    /**
     * Tests QuestionsDatabase's getInstance() method.
     */
    @Test
    void getInstance() {
        final QuestionsDatabase firstReference = QuestionsDatabase.getInstance();
        assertNotNull(firstReference,
                "QuestionsDatabase.getInstance() returned a null QuestionsDatabase!");

        assertSame(firstReference, QuestionsDatabase.getInstance(),
                "QuestionsDatabase.getInstance() returned 2 different instances " +
                        "of QuestionsDatabase!");
    }

    /**
     * Tests QuestionDatabase's getQuestion() method.
     */
    @Test
    void getQuestion() {
        final TriviaQuestion first = QuestionsDatabase.getInstance().getQuestion();
        assertNotNull(first,
                "QuestionsDatabase.getQuestion() returned a null question!");

        final TriviaQuestion second = QuestionsDatabase.getInstance().getQuestion();

        assertNotEquals(first, second,
                "QuestionsDatabase.getQuestion() returned a duplicate question!");
    }
}