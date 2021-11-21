package com.example.demo.bl;

import java.util.ArrayList;

public interface SynonymChecking {

    void readCiLin();

    double getSimilarity(String word1, String word2);

    ArrayList<String> getEncode(String word);

    String getCommonStr(String encode1, String encode2);

    int getK(String encode1, String encode2);

    int getN(String encodeHead);

    int getCount(String encodeHead, int end);
}
