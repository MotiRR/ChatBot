package sber.edu.analyzer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import sber.edu.database.dao.WordsDAO;
import sber.edu.database.dto.WordsEntity;
import sber.edu.kafka.messageReceiver.MessageReceiver;
import sber.edu.kafka.restrictionSender.restrictions.Restrictions;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsIds;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordsConf {

    public static final Logger logger = LogManager.getLogger(MessageReceiver.class);

    private static final Map<String, String> swearWord = new HashMap<>();
    private static final Map<String, String> xxxWord = new HashMap<>();
    private static final Map<String, String> spamWord = new HashMap<>();
    private static final Map<String, String> secretWord = new HashMap<>();
    private static WordsDAO wordsDAO = new WordsDAO();
    private static WordsEntity wordsEntity;

    public static void setWords(String word, RestrictionsIds restrictionsIds) {
        wordsEntity = new WordsEntity();
        word = word.toLowerCase();
        wordsEntity.setWord(word);
        wordsEntity.setStatus(restrictionsIds.toString());
        try {
            wordsDAO.createWord(wordsEntity);
            if (restrictionsIds == RestrictionsIds.SWEAR_WORD)
                swearWord.put(word, word);
            if (restrictionsIds == RestrictionsIds.XXX_CONTENT)
                xxxWord.put(word, word);
            if (restrictionsIds == RestrictionsIds.SPAM)
                spamWord.put(word, word);
            if (restrictionsIds == RestrictionsIds.SECRET_INFO_USAGE)
                secretWord.put(word, word);
        } catch (SQLException se) {
            logger.info(Restrictions.getTranslation(restrictionsIds) + " words:" + se.getMessage());
        }
    }

    public static void setWordsAll() {
        try {
            List<String> swearWordsList = wordsDAO.selectWordWithRestrictionsIds(RestrictionsIds.SWEAR_WORD);
            swearWordsList.stream().forEach(swear -> { swear = swear.toLowerCase(); swearWord.put(swear, swear); });

            List<String> XXXWordsList = wordsDAO.selectWordWithRestrictionsIds(RestrictionsIds.XXX_CONTENT);
            XXXWordsList.stream().forEach(xxx -> { xxx = xxx.toLowerCase(); xxxWord.put(xxx, xxx); });

            List<String> spamWordsList = wordsDAO.selectWordWithRestrictionsIds(RestrictionsIds.SPAM);
            spamWordsList.stream().forEach(spam -> {spam = spam.toLowerCase(); spamWord.put(spam, spam);});

            List<String> secretWordsList = wordsDAO.selectWordWithRestrictionsIds(RestrictionsIds.SECRET_INFO_USAGE);
            secretWordsList.stream().forEach(secret -> {secret = secret.toLowerCase(); secretWord.put(secret, secret);});

        } catch (SQLException se) {
            logger.info("Words all:" + se.getMessage());
        }
    }

    public static Map<String, String> getWords(RestrictionsIds restrictionsIds) {

        if (restrictionsIds == RestrictionsIds.SWEAR_WORD)
            return swearWord;
        if (restrictionsIds == RestrictionsIds.XXX_CONTENT)
            return xxxWord;
        if (restrictionsIds == RestrictionsIds.SPAM)
            return spamWord;
        if (restrictionsIds == RestrictionsIds.SECRET_INFO_USAGE)
            return secretWord;

        return null;
    }


    public static void createWordsForStarts() throws SQLException {

        if(WordsDAO.selectWord().isEmpty()) {
            setWords("идиот", RestrictionsIds.SWEAR_WORD);
            setWords("дурак", RestrictionsIds.SWEAR_WORD);
            setWords("дура", RestrictionsIds.SWEAR_WORD);
            setWords("придурок", RestrictionsIds.SWEAR_WORD);
            setWords("тупой", RestrictionsIds.SWEAR_WORD);
            setWords("Паршивец", RestrictionsIds.SWEAR_WORD);
            setWords("Сволочь", RestrictionsIds.SWEAR_WORD);
            setWords("Стерва", RestrictionsIds.SWEAR_WORD);
            setWords("Ублюдок", RestrictionsIds.SWEAR_WORD);
            setWords("Шваль", RestrictionsIds.SWEAR_WORD);
            setWords("Тварь", RestrictionsIds.SWEAR_WORD);
            setWords("хуй", RestrictionsIds.SWEAR_WORD);
            setWords("хуи", RestrictionsIds.SWEAR_WORD);

            setWords("эротика", RestrictionsIds.XXX_CONTENT);
            setWords("эротики", RestrictionsIds.XXX_CONTENT);
            setWords("порно", RestrictionsIds.XXX_CONTENT);
            setWords("порнография", RestrictionsIds.XXX_CONTENT);
            setWords("порнуха", RestrictionsIds.XXX_CONTENT);
            setWords("https://pornoxxx.com", RestrictionsIds.XXX_CONTENT);

            setWords("реклама", RestrictionsIds.SPAM);
            setWords("спам", RestrictionsIds.SPAM);

            setWords("пароль", RestrictionsIds.SECRET_INFO_USAGE);
            setWords("логин", RestrictionsIds.SECRET_INFO_USAGE);
        }
        else setWordsAll();
    }

}
