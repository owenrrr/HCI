package com.example.demo.vo;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/12/1 19:25
 * @Description:
 */
@Data
public class UserVO {

    String mail;

    String password;

    public UserVO(){}

    public UserVO(String mail, String password){
        this.mail = mail;
        this.password = password;
    }
}
