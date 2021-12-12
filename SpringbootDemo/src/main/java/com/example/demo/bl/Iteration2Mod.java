package com.example.demo.bl;

import com.example.demo.vo.UserVO;

import java.util.HashMap;

/**
 * @Author: Owen
 * @Date: 2021/12/10 21:11
 * @Description:
 */
public interface Iteration2Mod {

    HashMap<Integer, String> getUserProjectList(Integer uid);

    UserVO getUserInfo(Integer uid);
}
