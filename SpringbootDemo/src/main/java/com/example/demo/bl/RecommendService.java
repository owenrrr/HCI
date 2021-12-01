package com.example.demo.bl;

import java.util.HashMap;
import java.util.List;

public interface RecommendService {
    HashMap<String, Integer> recommend(String question, Integer pid);
}
