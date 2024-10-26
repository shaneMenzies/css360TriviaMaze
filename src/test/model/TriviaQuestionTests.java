package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the TriviaQuestion class.
 *
 * @author Shane Menzies
 * @version 10/25/24
 */
public class TriviaQuestionTests {

    /**
     * Question string for testing.
     */
    private static final String TEST_QUESTION = "Test Question";

    /**
     * Answer string for testing.
     */
    private static final String TEST_ANSWER = "Correct Answer for Test";

    /**
     * Default question type for testing.
     */
    private static final TriviaQuestion.QuestionType TEST_DEFAULT_TYPE
            = TriviaQuestion.QuestionType.ShortAnswer;

    /**
     * Instance to be used for testing.
     */
    private TriviaQuestion myTestQuestion;

    /**
     * Prepares the TriviaQuestion instance before each test.
     */
    @BeforeEach
    void setUp() {
        myTestQuestion = new TriviaQuestion(TEST_QUESTION, TEST_ANSWER, TEST_DEFAULT_TYPE);
    }

    /**
     * Tests TriviaQuestion's getQuestion() method.
     */
    @Test
    void getQuestion() {
        assertEquals(TEST_QUESTION, myTestQuestion.getQuestion());
    }

    /**
     * Tests TriviaQuestion's getAnswer() method.
     */
    @Test
    void getAnswer() {
        assertEquals(TEST_ANSWER, myTestQuestion.getAnswer());
    }

    /**
     * Tests TriviaQuestion's getType() method.
     */
    @Test
    void getType() {
        for (TriviaQuestion.QuestionType questionType : TriviaQuestion.QuestionType.values()) {
            myTestQuestion = new TriviaQuestion(TEST_QUESTION, TEST_ANSWER, questionType);
            assertEquals(questionType, myTestQuestion.getType());
        }
    }
}