package com.example.demo.bl;

import com.example.demo.vo.GrammarToken;

public interface GrammarAnalyseService {
    GrammarToken grammarAnalyse(String question, Integer pid);
}
