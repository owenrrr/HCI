package com.example.demo.blImpl;

import com.example.demo.vo.GrammarToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: lxyeah
 * @Date: 2021/6/2 20:25
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GrammarAnalyseImplTest {

    @Autowired
    GrammarAnalyseImpl grammarAnalyseImpl;

    @Test
    public void test() {

        GrammarToken grammarToken = grammarAnalyseImpl.grammarAnalyse("哈利波特的爸爸的血统？", 1);

//        assertTrue(list.size() > 0);
//
//        assertTrue(list.get(0).length() > 0);
    }
}
