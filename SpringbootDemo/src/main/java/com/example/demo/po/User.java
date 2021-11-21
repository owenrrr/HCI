package com.example.demo.po;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/11/21 22:14
 * @Description:
 */
@Data
public class User {

    private Integer id;

    private String password;

    private String mail;

    public User(String password, String mail){
        this.password = password;
        this.mail = mail;
    }

    public User(Integer id, String password, String mail){
        this.id = id;
        this.password = password;
        this.mail = mail;
    }

    public User(){}

}
