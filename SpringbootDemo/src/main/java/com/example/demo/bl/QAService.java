package com.example.demo.bl;

import com.example.demo.vo.Characters;

/**
 * @Author: Owen
 * @Date: 2021/6/2 10:41
 * @Description:
 */
public interface QAService {
    String execute(String question);

    float JaccardDegree(String src, String dst);

    Characters jsonToObject(String str);
}
