package com.example.demo.bl;

import com.example.demo.vo.IOKG;

/**
 * @Author: Owen
 * @Date: 2021/6/9 11:05
 * @Description:
 */
public interface Common {


    IOKG getKG(Integer pid);

    int createProject(Integer uid, String name);

    int createUser(String mail, String password);
}
