package com.example.demo.blImpl;

import com.example.demo.bl.QAService;
import com.example.demo.vo.Characters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author: Owen
 * @Date: 2021/6/2 22:11
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QAServiceImplTest {

    @Autowired
    QAService qaService;

    @Test
    public void execute() {
        String question1 = "卡多根的孩子是谁?";
        String question2 = "阿不思邓布利多是谁?";
        String question3 = "阿不思·邓布利多是谁?";
        String result = qaService.execute(question1, 1);
        System.out.println(result);
        assertTrue(true);
    }

    @Test
    public void jaccardDegree() {
        String str1 = "哈利波特";
        String str2 = "哈利·波特";
        String str3 = "哈德温·波特";

        float result1 = qaService.JaccardDegree(str1, str2);
        float result2 = qaService.JaccardDegree(str1, str3);
        System.out.println("result1 Jaccard degree: " + result1);
        System.out.println("result2 Jaccard degree: " + result1);

        assertTrue(result1 > result2);
    }

    @Test
    public void testJSON(){
        String content = "{'出生': '英格兰', '血统': '麻瓜', '婚姻状况': '已婚', '物种': '人类', '性别': '男', '职业': ['石匠', '伊尔弗莫尼魔法学校校长及创办者']}";
        Characters character = qaService.jsonToObject(content);
        if (character != null){
            System.out.println("birth: " + character.getBirth());
        }else{
            System.out.println("character is null!");
        }
    }

}