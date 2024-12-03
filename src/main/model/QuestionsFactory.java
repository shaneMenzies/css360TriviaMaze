package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.interfaces.QuestionSource;
import org.sqlite.SQLiteDataSource;

/**
 * Manages interactions with the questions SQLite Database.
 *
 * @author Shane Menzies
 * @version 11/2/24
 */
public final class QuestionsFactory implements QuestionSource {

    /**
     * Location and name of SQL database file.
     */
    private static final String DATABASE_FILE = "resources/questions.db";

    /**
     * Query to get all the questions from the database.
     */
    private static final String LOAD_QUESTIONS_QUERY = "SELECT * FROM questions";

    /**
     * Name of the field in the database for the question portion of a question.
     */
    private static final String DB_QUESTION_FIELD = "question_text";

    /**
     * Name of the field in the database for the type portion of a question.
     */
    private static final String DB_TYPE_FIELD = "type";

    /**
     * Name of the field in the database for the answer portion of a question.
     */
    private static final String DB_ANSWER_FIELD = "answer_text";

    /**
     * Error message unable to retrieve questions from the SQL Database.
     */
    private static final String RETRIEVAL_FAILED_MESSAGE
            = "ERROR: Unable to retrieve questions from the SQL Database!";

    /**
     * Unique instance of QuestionsFactory to limit
     * this class to a single instance.
     */
    private static final QuestionsFactory UNIQUE_INSTANCE = new QuestionsFactory();

    /**
     * List of trivia questions from database.
     */
    private final List<TriviaQuestion> myQuestions;

    /**
     * Index for tracking position in questions array.
     */
    private int myIndex;

    /**
     * Private constructor to prevent external instantiation.
     */
    private QuestionsFactory() {
        final SQLiteDataSource dataSource = establishDataSource();
        myQuestions = loadQuestionsFromDatabase(dataSource);
        shuffleQuestions();
        myIndex = 0;
    }

    /**
     * Gets the instance of QuestionsFactory. Will always
     * return a reference to the same instance.
     *
     * @return Reference to the sole instance of QuestionsFactory.
     */
    public static QuestionsFactory getInstance() {
        return UNIQUE_INSTANCE;
    }

    /**
     * Gets a random question from the database.
     *
     * @return Random trivia question.
     */
    @Override
    public TriviaQuestion getQuestion() {
        final TriviaQuestion question = myQuestions.get(myIndex);
        myIndex++;

        // Reshuffle questions if end reached
        if (myIndex >= myQuestions.size()) {
            shuffleQuestions();
            myIndex = 0;
        }

        return question;
    }

    /**
     * Establishes a data source with the questions database file.
     * @return SQLiteDataSource with URL set to the questions' database.
     */
    private static SQLiteDataSource establishDataSource() {
        final SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + DATABASE_FILE);

        return dataSource;
    }

    /**
     * Loads a TriviaQuestion from a row of SQL query results.
     *
     * @param theResults Results to load from.
     * @return TriviaQuestion made from the row's contents.
     * @throws SQLException If an error occurred in loading the question data.
     */
    private static TriviaQuestion loadQuestion(final ResultSet theResults)
            throws SQLException {
        final String question = theResults.getString(DB_QUESTION_FIELD);
        final String typeString = theResults.getString(DB_TYPE_FIELD);
        final String answer = theResults.getString(DB_ANSWER_FIELD);

        final TriviaQuestion.QuestionType type
                = TriviaQuestion.QuestionType.valueOf(typeString);

        return new TriviaQuestion(question, answer, type);
    }

    /**
     * Loads all the questions from the provided data source
     * into a list.
     *
     * @param theDataSource Data source to load the question data from.
     * @return List of TriviaQuestions, one for each row in the database.
     */
    private static List<TriviaQuestion> loadQuestionsFromDatabase(
            final SQLiteDataSource theDataSource) {
        final ArrayList<TriviaQuestion> tempQuestions = new ArrayList<>();

        try (Connection dbConnection = theDataSource.getConnection();
             Statement dbStatement = dbConnection.createStatement()) {

            // Perform query
            final ResultSet result = dbStatement.executeQuery(LOAD_QUESTIONS_QUERY);

            // Parse results into TriviaQuestion objects
            while (result.next()) {
                tempQuestions.add(loadQuestion(result));
            }

        } catch (final SQLException caughtException) {
            System.out.println(RETRIEVAL_FAILED_MESSAGE);
            System.out.println("Cause: " + caughtException.getCause().toString());
            System.out.println(caughtException.getMessage());
            System.exit(1);
        }

        return tempQuestions;
    }

    /**
     * Shuffles the list of questions.
     */
    private void shuffleQuestions() {
        Collections.shuffle(myQuestions);
    }
}