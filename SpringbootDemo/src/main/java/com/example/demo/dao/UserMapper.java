package com.example.demo.dao;

import com.example.demo.po.Project;
import com.example.demo.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Owen
 * @Date: 2021/11/21 22:20
 * @Description:
 */
@Mapper
@Repository
public interface UserMapper {

    int createUser(@Param("mail") String mail, @Param("password") String password);

    int createProject(@Param("uid") Integer uid, @Param("name") String name);

    int getLastKey();

    List<Project> getProjectsByUid(@Param("uid") Integer uid);

    User getUserByUid(@Param("uid") Integer uid);

}
