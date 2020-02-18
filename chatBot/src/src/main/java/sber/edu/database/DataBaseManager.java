package sber.edu.database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DataBaseManager {
    private static final String dbName = System.getProperty("sberAdmin.database.name");
    private static final String dbFolder = "target/";

    public final List<String> tables = Arrays.asList(
            "words",
            "messages",
            "result_analysis",
            "sentences",
            "status_of_sentence",
            "user_lock"
    );

    public static final Logger logger = LogManager.getLogger(DataBaseManager.class);

    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", "jdbc:sqlite:" + dbFolder + dbName);

            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static DataBaseManager getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DataBaseManager.class) {
                if (instance == null) {
                    instance = new DataBaseManager();
                }
            }
        }
        return instance;
    }

    public Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    private static DataBaseManager instance;

    private DataBaseManager() throws SQLException {
        intializeDataBase();
    }

    public void clearDataBase() {
        try (Session session = getSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM WordsEntity").executeUpdate();
            session.createQuery("DELETE FROM MessagesEntity").executeUpdate();
            session.createQuery("DELETE FROM ResultAnalysisEntity").executeUpdate();
            session.createQuery("DELETE FROM SentencesEntity").executeUpdate();
            session.createQuery("DELETE FROM StatusOfSentenceEntity").executeUpdate();
            session.createQuery("DELETE FROM UserLockEntity").executeUpdate();
            session.getTransaction().commit();
        }
    }

    private void intializeDataBase() throws SQLException {
        String url = "jdbc:sqlite:" + dbFolder + dbName;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            for (String tableName : tables) {
                createTable(stmt, tableName);
            }
            logger.info("Database initialized successfully");
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        }
    }

    private void createTable(Statement stmt, String tableName) throws SQLException {
        try {
            String script = new String(Files.readAllBytes(Paths.get("db/" + tableName + ".sql")), StandardCharsets.UTF_8);
            stmt.execute(script);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }
}
