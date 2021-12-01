package com.example.demo.controller;

import com.example.demo.vo.ResponseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Owen
 * @Date: 2021/6/9 14:53
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class BasicControllerTest {

    @Autowired
    BasicController basicController;

    @Test
    @Transactional
    void createKG() {


    }

    @Test
    @Transactional
    void addKG() {
    }

    @Test
    @Transactional
    void getKG() {

        ResponseVO result = basicController.getKG(1);

        assertTrue(result.getSuccess());

    }
}