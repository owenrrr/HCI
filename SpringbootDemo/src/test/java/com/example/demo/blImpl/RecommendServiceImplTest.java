package com.example.demo.blImpl;


import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: lxyeah
 * @Date: 2021/6/2 20:25
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest

public class RecommendServiceImplTest {

    @Autowired
    RecommendServiceImpl recommendService;

    @Test
    public void execute(){
        recommendService.recommend("哈利波特的爸爸是谁？", 1);
    }

    @Test
    public void execute1() {
        String[] testCase = new String[]{
                "哈利波特的爸爸是阿不思邓布利多？"
        };
        Segment segment = HanLP.newSegment().enableTranslatedNameRecognize(true);
        for (String sentence : testCase)
        {
            List<Term> termList = segment.seg(sentence);
            System.out.println(termList);
        }
    }

    @Test
    public void execute2() {
        CoNLLSentence sentence = HanLP.parseDependency("哈利波特的妈妈？");
        System.out.println(sentence);

        CoNLLWord[] wordArray = sentence.getWordArray();
        List<CoNLLWord> words = new LinkedList<>(Arrays.asList(wordArray));
        List<CoNLLWord> resArray = new LinkedList<>();

        filter(words, resArray);


        for (CoNLLWord word : resArray) {
//            if (!word.HEAD.POSTAG.equals("root") && word.HEAD.DEPREL.equals("核心关系") && !word.DEPREL.equals("标点符号"))
//            {
//                for (CoNLLWord w: sentence) {
//                    if (w.DEPREL.equals("并列关系") && w.HEAD.ID == word.ID) {
//                        System.out.printf("%s --(%s)--> %s\n", w.LEMMA, w.DEPREL, w.HEAD.LEMMA);
//                    }
//                }
                System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);

//            }
        }

    }

    public void filter(List<CoNLLWord> words, List<CoNLLWord> resArray) {
        if (resArray.size() == 0) {
            for (CoNLLWord word : words) {
                if (word.HEAD.POSTAG.equals("root")) {
                    resArray.add(word);
                    words.remove(word);
                    break;
                }
            }
            filter(words, resArray);
        }
        else {
            List<CoNLLWord> temp = new LinkedList<>();
            for (CoNLLWord res : resArray) {
                for (CoNLLWord word : words) {
                    if (word.HEAD == res && (
                            word.POSTAG.charAt(0) == 'n'
                                    || word.POSTAG.charAt(0) == 'r'
                                    || word.POSTAG.charAt(0) == 'l'
                    )) {
                        temp.add(word);
                    }
                }
            }
            if (temp.size() == 0) {
                return;
            }
            else {
                for (CoNLLWord t : temp) {
                    resArray.add(t);
                    words.remove(t);
                }
                filter(words, resArray);
            }
        }
    }
}
