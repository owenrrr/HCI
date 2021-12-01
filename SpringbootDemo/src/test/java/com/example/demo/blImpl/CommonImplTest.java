package com.example.demo.blImpl;

import com.example.demo.bl.Common;
import com.example.demo.vo.IOKG;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Owen
 * @Date: 2021/6/9 14:48
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonImplTest {

    @Autowired
    Common common;

    @Test
    public void getKG() {

        IOKG ioKG = common.getKG(1);

        assertTrue(ioKG != null);

        assertTrue(ioKG.getEdges().length > 0);

        assertTrue(ioKG.getNodes().length > 0);

        System.out.println(ioKG.getNodes().length + " " + ioKG.getEdges().length);

    }
}