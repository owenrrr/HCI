package com.example.demo.blImpl;

import com.example.demo.bl.SynonymChecking;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author: Owen
 * @Date: 2021/6/2 20:25
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SynonymCheckingImplTest {

    @Autowired
    SynonymChecking synonymChecking;

    @Test
    public void getSimilarity() {
        double similarity = synonymChecking.getSimilarity("人民", "国民");
        System.out.println("人民--" + "国民:" + similarity);
        similarity = synonymChecking.getSimilarity("人民", "群众");
        System.out.println("人民--" + "群众:" + similarity);
        similarity = synonymChecking.getSimilarity("人民", "党群");
        System.out.println("人民--" + "党群:" + similarity);
        similarity = synonymChecking.getSimilarity("人民", "良民");
        System.out.println("人民--" + "良民:" + similarity);
        similarity = synonymChecking.getSimilarity("人民", "同志");
        System.out.println("人民--" + "同志:" + similarity);
        similarity = synonymChecking.getSimilarity("人民", "成年人");
        System.out.println("人民--" + "成年人:" + similarity);
        similarity = synonymChecking.getSimilarity("人民", "市民");
        System.out.println("人民--" + "市民:" + similarity);
        similarity = synonymChecking.getSimilarity("人民", "亲属");
        System.out.println("人民--" + "亲属:" + similarity);
        similarity = synonymChecking.getSimilarity("人民", "志愿者");
        System.out.println("人民--" + "志愿者:" + similarity);
        similarity = synonymChecking.getSimilarity("人民", "先锋");
        System.out.println("人民--" + "先锋:" + similarity);

        similarity = synonymChecking.getSimilarity("母亲", "老妈");
        System.out.println(similarity);
        similarity = synonymChecking.getSimilarity("爸爸", "老爹");
        System.out.println(similarity);
    }

    @Test
    public void getEncode() {
    }

    @Test
    public void getCommonStr() {
        String commonStr = synonymChecking.getCommonStr("Aa01A01=", "Aa01A03=");
        System.out.println(commonStr);
    }

    @Test
    public void getK() {
        int k = synonymChecking.getK("Aa01A01=", "Aa01A01=");
        System.out.println(k);
    }

    @Test
    public void getN() {
        int a = synonymChecking.getN("A");
        System.out.println(a);
    }

    @Test
    public void getCount() {
    }
}