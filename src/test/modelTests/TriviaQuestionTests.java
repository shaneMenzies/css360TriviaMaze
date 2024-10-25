package modelTests;

import static org.junit.jupiter.api.Assertions.*;

import model.TriviaQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TriviaQuestionTests {

    /**
     * Question string for testing.
     */
    private final static String TEST_QUESTION = "Test Question";

    /**
     * Answer string for testing.
     */
    private final static String TEST_ANSWER = "Correct Answer for Test";

    /**
     * Default question type for testing.
     */
    private final static TriviaQuestion.QuestionType TEST_DEFAULT_TYPE = TriviaQuestion.QuestionType.ShortAnswer;

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
        assertEquals(myTestQuestion.getQuestion(), TEST_QUESTION);
    }

    /**
     * Tests TriviaQuestion's getAnswer() method.
     */
    @Test
    void getAnswer() {
        assertEquals(myTestQuestion.getAnswer(), TEST_ANSWER);
    }

    /**
     * Tests TriviaQuestion's getType() method.
     */
    @Test
    void getType() {
        for (TriviaQuestion.QuestionType questionType : TriviaQuestion.QuestionType.values()) {
            myTestQuestion = new TriviaQuestion(TEST_QUESTION, TEST_ANSWER, questionType);
            assertEquals(myTestQuestion.getType(), questionType);
        }
    }
}