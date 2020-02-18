package sber.edu.analyzer;

import java.util.Properties;

public class CoreNLPConf {

    private static final Properties props = new Properties();

    public static Properties getProperties() {
        props.setProperty("annotators", "tokenize, ssplit, pos");
        props.setProperty("pos.model", "edu/stanford/nlp/models/russian-ud-pos.tagger");
        props.setProperty("customAnnotatorClass.custom.lemma", "edu.stanford.nlp.international.russian.process.RussianLemmatizationAnnotator");
        props.setProperty("custom.lemma.dictionaryPath", "edu/stanford/nlp/models/dict.tsv");
        props.setProperty("customAnnotatorClass.custom.morpho ", "edu.stanford.nlp.international.russian.process.RussianMorphoAnnotator");
        props.setProperty("custom.morpho.model", "edu/stanford/nlp/models/pos-tagger/russian-ud-mf.tagger");
        props.setProperty("depparse.model", "edu/stanford/nlp/models/parser/nndep/nndep.rus.model.wiki.txt.gz");
        props.setProperty("depparse.language", "russian");
        props.setProperty("coref.algorithm", "neural");
        return props;
    }
}
