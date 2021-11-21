package com.example.demo.bl;

import com.example.demo.vo.IOKG;

/**
 * @Author: Owen
 * @Date: 2021/6/9 11:05
 * @Description:
 */
public interface Common {


    IOKG getKG();

    void createProject(Integer uid, String name);

    void createUser(String mail, String password);
}
