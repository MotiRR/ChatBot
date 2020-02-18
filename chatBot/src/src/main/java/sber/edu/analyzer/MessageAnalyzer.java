package sber.edu.analyzer;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import sber.edu.kafka.messageReceiver.Message;
import sber.edu.kafka.messageReceiver.MessageListener;
import sber.edu.kafka.messageReceiver.MessageReceiver;
import sber.edu.kafka.restrictionSender.restrictions.RestrictionsIds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;

public class MessageAnalyzer implements MessageListener {

    public static final Logger logger = LogManager.getLogger(MessageReceiver.class);

    private final Properties props = CoreNLPConf.getProperties();
    private final BiFunction<List<String>, String, List<Integer>> indexOfPOS = SentenceAnalyzer.indexOfPOS();
    private final BiFunction<List<CoreLabel>, List<Integer>, List<String>> wordOfPOS = SentenceAnalyzer.wordOfPOS();
    private final BiFunction<List<String>, Map<String, String>, List<String>> wordBadAndKey = SentenceAnalyzer.wordBadAndKey();

    private final Map<String, String> badWord = WordsConf.getWords(RestrictionsIds.SWEAR_WORD);
    private final Map<String, String> xxxWord = WordsConf.getWords(RestrictionsIds.XXX_CONTENT);
    private final Map<String, String> spamWord = WordsConf.getWords(RestrictionsIds.SPAM);
    private final Map<String, String> secretWord = WordsConf.getWords(RestrictionsIds.SECRET_INFO_USAGE);

    private LocksManager locksManager = new LocksManager();

    private StanfordCoreNLP pipeline;
    private ArrayList<List<String>> allList;

    public MessageAnalyzer() {
        pipeline = new StanfordCoreNLP(props);
    }


    private ArrayList<List<String>> checkedSentence(CoreSentence sentence) {
        allList = new ArrayList<>();
        List<String> posTags = sentence.posTags();

        List<Integer> nounIndexPOC = indexOfPOS.apply(posTags, "NOUN");
        List<Integer> verbIndexPOC = indexOfPOS.apply(posTags, "VERB");
        List<Integer> adjIndexPOC = indexOfPOS.apply(posTags, "ADJ");
        List<Integer> propnIndexPOC = indexOfPOS.apply(posTags, "PROPN");


        List<String> nounPOC = wordOfPOS.apply(sentence.tokens(), nounIndexPOC);
        List<String> verbPOC = wordOfPOS.apply(sentence.tokens(), verbIndexPOC);
        List<String> adjPOC = wordOfPOS.apply(sentence.tokens(), adjIndexPOC);
        List<String> propnPOC = wordOfPOS.apply(sentence.tokens(), propnIndexPOC);

        List<String> allWords = new ArrayList<>();
        allWords.addAll(nounPOC);
        allWords.addAll(verbPOC);
        allWords.addAll(adjPOC);
        allWords.addAll(propnPOC);

        List<String> badWords = wordBadAndKey.apply(allWords, badWord);
        List<String> xxxWords = wordBadAndKey.apply(allWords, xxxWord);
        List<String> spamWords = wordBadAndKey.apply(allWords, spamWord);
        List<String> secretWords = wordBadAndKey.apply(allWords, secretWord);

        allList.add(nounPOC);
        allList.add(verbPOC);
        allList.add(badWords);
        allList.add(xxxWords);
        allList.add(spamWords);
        allList.add(secretWords);

        return allList;
    }

    private void checkedStatus(EntityCreate entityCreate) {

        if (allList == null) return;

        for (int i = 2; i < allList.size(); i++) {
            List<String> words = allList.get(i);
            for (int j = 0; j < words.size(); j++) {
                String word = words.get(j).toLowerCase();
                if (badWord.containsKey(word)) {
                    entityCreate.entityCreateStatusOfSentence(RestrictionsIds.SWEAR_WORD);
                    entityCreate.addStatusOfSentence();
                }
                if (xxxWord.containsKey(word)) {
                    entityCreate.entityCreateStatusOfSentence(RestrictionsIds.XXX_CONTENT);
                    entityCreate.addStatusOfSentence();
                }
                if (spamWord.containsKey(word)) {
                    entityCreate.entityCreateStatusOfSentence(RestrictionsIds.SPAM);
                    entityCreate.addStatusOfSentence();
                }
                if (secretWord.containsKey(word)) {
                    entityCreate.entityCreateStatusOfSentence(RestrictionsIds.SECRET_INFO_USAGE);
                    entityCreate.addStatusOfSentence();
                }
            }
        }
    }

    @Override
    public synchronized void messageReceived(Message message) {

        CoreDocument document = new CoreDocument(message.getMessage());
        pipeline.annotate(document);

        EntityCreate entityCreate = new EntityCreate(message);
        entityCreate.entityCreateMessage();

        for (int i = 0; i < document.sentences().size(); i++) {
            CoreSentence sentence = document.sentences().get(i);
            entityCreate.entityCreateSentence(sentence.text());
            entityCreate.addSentenceToMessage();
            entityCreate.entityCreateResultAnalysis(checkedSentence(sentence));
            entityCreate.addResultAnalysisToSentence();
            checkedStatus(entityCreate);
        }

        try {
            entityCreate.saveEntity();
        } catch (SQLException ex) {
            logger.error("exception save entity: " + ex.getMessage());
        }

        try {
            locksManager.locker(message.getFromUser());
        } catch (Exception ex) {
            logger.error("exception lock users: " + ex.getMessage());
        }
    }
}
