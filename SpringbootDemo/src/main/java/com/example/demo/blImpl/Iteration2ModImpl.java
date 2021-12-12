package com.example.demo.blImpl;

import com.example.demo.bl.Iteration2Mod;
import com.example.demo.dao.UserMapper;
import com.example.demo.po.Project;
import com.example.demo.po.User;
import com.example.demo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Owen
 * @Date: 2021/12/10 21:12
 * @Description:
 */
@Service
public class Iteration2ModImpl implements Iteration2Mod {

    @Autowired
    UserMapper userMapper;

    @Override
    public HashMap<Integer, String> getUserProjectList(Integer uid){

        HashMap<Integer, String> resultMap = new HashMap<>();
        List<Project> projects = userMapper.getProjectsByUid(uid);
        if (projects.size() == 0) {
            return null;
        }
        for (Project p: projects) {
            resultMap.put(p.getId(), p.getName());
        }
        return resultMap;
    }

    @Override
    public UserVO getUserInfo(Integer uid){
        User user = userMapper.getUserByUid(uid);
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO(user.getMail(), user.getPassword());
        return userVO;
    }

}
