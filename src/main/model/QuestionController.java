package model;

import java.util.ArrayList;
import java.util.List;
import model.interfaces.QuestionControllerUpdateListener;
import model.interfaces.QuestionHandler;

/**
 * Question Controller for handling trivia questions from the
 * individual doors, tracking the active question, and
 * grading a user's answer.
 *
 * @author Shane Menzies
 * @version 11/10/24
 */
public final class QuestionController implements QuestionHandler {

    /**
     * Exception message for a question being rejected because this handler
     * already has one.
     */
    private static final String QUESTION_REJECTED_MESSAGE =
            "Tried to give QuestionController"
                    + " a new question when it was already busy with one!";

    /**
     * This QuestionController's update listeners.
     */
    private final List<QuestionControllerUpdateListener> myListeners;

    /**
     * Tracks if this question controller has a question.
     */
    private boolean myHasQuestion;

    /**
     * Active TriviaQuestion being asked.
     */
    private TriviaQuestion myQuestion;

    /**
     * Callback to return the question result to when it's been answered.
     */
    private AnswerCallback myAnswerCallback;


    /**
     * Makes an empty QuestionController.
     */
    public QuestionController() {
        myHasQuestion = false;
        myListeners = new ArrayList<>();
    }

    /**
     * Gives a question to this QuestionController to be asked.
     *
     * @param theQuestion Question to be asked.
     * @param theCallback Callback method to receive the result of the question.
     * @throws QuestionRejectedException If this controller was already
     *  busy with another question.
     */
    @Override
    public void askQuestion(final TriviaQuestion theQuestion, final AnswerCallback theCallback)
        throws QuestionRejectedException {
        if (myHasQuestion) {
            // Trying to ask another question when we already have one active!
            throw new QuestionRejectedException(QUESTION_REJECTED_MESSAGE);
        } else {
            myHasQuestion = true;
            myQuestion = theQuestion;
            myAnswerCallback = theCallback;

            updateListeners();
        }
    }

    /**
     * Checks if this controller has an active question.
     *
     * @return True if this controller does have an active question, false otherwise.
     */
    public boolean hasQuestion() {
        return myHasQuestion;
    }

    /**
     * Gets the active question if there is one.
     *
     * @return Active question if this controller has one.
     */
    public TriviaQuestion getQuestion() {
        return myQuestion;
    }

    /**
     * Adds an update listener to this QuestionController.
     *
     * @param theListener New update listener to add.
     */
    public void addListener(final QuestionControllerUpdateListener theListener) {
        myListeners.add(theListener);
    }

    /**
     * Removes a previously added update listener from this
     * QuestionController's listeners.
     *
     * @param theListener Update listener to remove.
     * @return True if successful, otherwise false.
     */
    public boolean removeListener(final QuestionControllerUpdateListener theListener) {
        return myListeners.remove(theListener);
    }

    /**
     * Provides an answer to the active question.
     *
     * @param theInput Answer to provide and judge.
     */
    public void answerQuestion(final String theInput) {
        // TODO: More advanced question grading logic.
        if (theInput.equals(myQuestion.getAnswer())) {
            resolveQuestion(QuestionResult.CORRECT);
        } else {
            resolveQuestion(QuestionResult.INCORRECT);
        }
    }

    /**
     * Cancels the active question.
     */
    public void cancelQuestion() {
        resolveQuestion(QuestionResult.CANCELLED);
    }

    /**
     * Updates this QuestionController's listeners.
     */
    private void updateListeners() {
        for (final QuestionControllerUpdateListener listener : myListeners) {
            listener.doUpdate(this);
        }
    }

    /**
     * Resolves the active question with the provided result.
     *
     * @param theResult Result of the question.
     */
    private void resolveQuestion(final QuestionResult theResult) {
        myHasQuestion = false;
        myAnswerCallback.call(theResult);

        // Null out values which are now invalid.
        myQuestion = null;
        myAnswerCallback = null;

        updateListeners();
    }
}