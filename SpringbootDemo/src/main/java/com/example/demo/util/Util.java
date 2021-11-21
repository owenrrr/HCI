package com.example.demo.util;

import com.example.demo.bl.SynonymChecking;
import com.example.demo.blImpl.QAServiceImpl;
import com.example.demo.blImpl.SynonymCheckingImpl;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.seg.common.Term;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.*;


public class Util {

    static SynonymChecking synonymChecking = new SynonymCheckingImpl();

//    static final String PREFIX = "/var/lib/jenkins/workspace/seiii/SpringbootDemo/src/main/resources";
    static final String PREFIX = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "resources"));

    public static float jaccardDegree(String a, String b) {

        Set<Character> s1 = new HashSet<>();//set元素不可重复
        Set<Character> s2 = new HashSet<>();

        for (int i = 0; i < a.length(); i++) {
            s1.add(a.charAt(i));//将string里面的元素一个一个按索引放进set集合
        }
        for (int j = 0; j < b.length(); j++) {
            s2.add(b.charAt(j));
        }

        float mergeNum = 0;//并集元素个数
        float commonNum = 0;//相同元素个数（交集）

        for (Character ch1 : s1) {
            for (Character ch2 : s2) {
                if (ch1.equals(ch2)) {
                    commonNum++;
                }
            }
        }

        mergeNum = s1.size() + s2.size() - commonNum;

        return (float) (commonNum / mergeNum);

    }

    public static List<String> extractKeyword(String str, int size) {
        return HanLP.extractKeyword(str, size);
    }

    public static Match subClassifier(String word) {

        double JACCARD_THRESHOLD = 0.5;
        double SIMILARITY_THRESHOLD = 0.7;

        try {
            // step 4
            File file = new File(PREFIX + "/voc/characters.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String result = br.readLine();
            result = result != null ? result : "";
            String[] character_array = result.split(",");

            file = new File(PREFIX + "/voc/relations.txt");
            br = new BufferedReader(new FileReader(file));
            result = br.readLine();
            result = result != null ? result : "";
            String[] relations_array = result.split(",");

            file = new File(PREFIX + "/voc/property.txt");
            br = new BufferedReader(new FileReader(file));
            result = br.readLine();
            result = result != null ? result : "";
            String[] property_array = result.split(",");

            file = new File(PREFIX + "/voc/groupRelations.txt");
            br = new BufferedReader(new FileReader(file));
            result = br.readLine();
            result = result != null ? result : "";
            String[] group_array = result.split(",");

            //step 5 (includes step 2: using on relation)
            float degree = 0;
            double similarity = 0.0;

            Match ent_candidate = new Match(0, "", "", "");
            Match rel_candidate = new Match(0, "", "", "");
            Match pro_candidate = new Match(0, "", "", "");
            Match group_candidate = new Match(0, "", "", "");

            for (String s : character_array) {
                degree = jaccardDegree(s, word);

                if (degree > JACCARD_THRESHOLD && degree > ent_candidate.degree) {
                    ent_candidate.degree = degree;
                    ent_candidate.content = word;
                    ent_candidate.match_content = s;
                    ent_candidate.type = "character";
                }
            }

            if (ent_candidate.degree != 0) {
                return ent_candidate;
            }

            for (String s : relations_array) {
                similarity = synonymChecking.getSimilarity(s, word);
                if (similarity > SIMILARITY_THRESHOLD && similarity > rel_candidate.degree) {
                    rel_candidate.degree = (float) similarity;
                    rel_candidate.content = word;
                    rel_candidate.match_content = s;
                    rel_candidate.type = "relation";
                }
            }
            if (rel_candidate.degree != 0) {

                return rel_candidate;
            }

            for (String s : group_array) {
                similarity = synonymChecking.getSimilarity(s, word);
                if (similarity > SIMILARITY_THRESHOLD && similarity > group_candidate.degree) {
                    group_candidate.degree = (float) similarity;
                    group_candidate.content = word;
                    group_candidate.match_content = s;
                    group_candidate.type = "group";
                }
            }

            if (group_candidate.degree != 0) {

                return group_candidate;
            }

            for (String s : property_array) {
                similarity = synonymChecking.getSimilarity(s, word);
                if (similarity > SIMILARITY_THRESHOLD && similarity > pro_candidate.degree) {
                    pro_candidate.degree = (float) similarity;
                    pro_candidate.content = word;
                    pro_candidate.match_content = s;
                    pro_candidate.type = "property";
                }
            }

            if (pro_candidate.degree != 0) {
                return pro_candidate;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Match(0, word, word, "none");
    }

    public static HashMap<String, List<Match>> classifier(List<String> keywords) {

        HashMap<String, List<Match>> classify_res = new HashMap<>();

        List<Match> entities = new LinkedList<>();
        List<Match> relations = new LinkedList<>();
        List<Match> properties = new LinkedList<>();
        List<Match> groups = new LinkedList<>();

        for (String word : keywords) {
            Match temp = subClassifier(word);

            switch (temp.type) {
                case "character":
                    entities.add(temp);
                    break;
                case "relation":
                    relations.add(temp);
                    break;
                case "property":
                    properties.add(temp);
                    break;
                case "group":
                    groups.add(temp);
                    break;
            }
        }


        classify_res.put("entity", entities);
        classify_res.put("relation", relations);
        classify_res.put("property", properties);
        classify_res.put("group", groups);

        return classify_res;
    }


    public static void filter(List<CoNLLWord> words, List<CoNLLWord> resArray) {
        if (resArray.size() == 0) {
            for (CoNLLWord word : words) {
                if (word.HEAD.POSTAG.equals("root")) {
                    resArray.add(word);
                    words.remove(word);
                    break;
                }
            }
            filter(words, resArray);
        } else {
            List<CoNLLWord> temp = new LinkedList<>();
            for (CoNLLWord res : resArray) {
                for (CoNLLWord word : words) {
                    if (word.HEAD == res && (
                            word.POSTAG.charAt(0) == 'n'
                                    //  || word.POSTAG.charAt(0) == 'r'
                                    || word.POSTAG.charAt(0) == 'l'
                    )) {
                        temp.add(word);
                    }
                }
            }
            if (temp.size() == 0) {
                return;
            } else {
                for (CoNLLWord t : temp) {
                    resArray.add(t);
                    words.remove(t);
                }
                filter(words, resArray);
            }
        }
    }

    public static List<String> filter2(List<Term> words) {
        List<String> res = new LinkedList<>();
        SynonymChecking synonymChecking = new SynonymCheckingImpl();
        for (int i = 0; i < words.size() - 1; i++) {
            if (words.get(i).nature.toString().startsWith("n")){
                if (words.get(i).nature.toString().startsWith("n") && words.get(i+1).nature.toString().startsWith("n")) {
                    String temp = words.get(i).word +
                            words.get(i + 1).word;
                    res.add(temp);
                    i++;
                }
                else {
                    res.add(words.get(i).word);
                }

            }
            else if (synonymChecking.getSimilarity(words.get(i).word, "从属") > 0.7) {
                res.add(words.get(i).word);
            }
        }

        if (words.get(words.size() - 1).nature.toString().startsWith("n")) {
            if (words.size() == 1 ) {
                res.add(words.get(0).word);
            }
            else if (!words.get(words.size() - 2).nature.toString().startsWith("n")){
                res.add(words.get(words.size() - 1).word);
            }
        }


        return res;
    }
}
