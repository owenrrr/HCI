package com.example.demo.util;

public class Match {
    public float degree;

    public String type;

    public String content;

    public String match_content;

    public Match(float degree, String content, String match_content, String type) {
        this.degree = degree;
        this.content = content;
        this.match_content = match_content;
        this.type = type;
    }
}
