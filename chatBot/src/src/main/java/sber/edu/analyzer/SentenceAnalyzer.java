package sber.edu.analyzer;

import edu.stanford.nlp.ling.CoreLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class SentenceAnalyzer {

    public static BiFunction<List<String>, String, List<Integer>> indexOfPOS() {
        return (sentenceStr, pos) -> {
            int index = 0;
            ArrayList<Integer> retIndex = new ArrayList<>();
            for (String sen : sentenceStr) {
                if (sen.equalsIgnoreCase(pos)) {
                    retIndex.add(index);
                }
                index = index + 1;
            }
            return retIndex;
        };
    }

    public static BiFunction<List<CoreLabel>, List<Integer>, List<String>> wordOfPOS() {
        return (sentenceStr, pos) -> {
            ArrayList<String> retIndex = new ArrayList<>();
            for (int i = 0; i < pos.size(); i++)
                retIndex.add(sentenceStr.get(pos.get(i)).originalText());
            return retIndex;
        };
    }

    public static BiFunction<List<String>, Map<String, String>, List<String>> wordBadAndKey() {

        return (words, badAndKeyWords) -> {
            String value;
            ArrayList<String> foundWords = new ArrayList<>();
            for (int i = 0; i < words.size(); i++) {
                String currentWord = words.get(i);
                value = badAndKeyWords.get(currentWord.toLowerCase());
                if (value!=null)
                    foundWords.add(currentWord);
            }
            return foundWords;
        };
    }

    public static BiPredicate<Map<String, String>, List<String>> dd() {
        return (badKeyWords, words) -> {
            boolean isWord = false;
            for (String word : words) {
                if (badKeyWords.containsKey(word)) {
                    isWord = true;
                    break;
                }
            }
            return isWord;
        };
    }

}
