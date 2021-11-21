package com.example.demo.vo.Entity;

import lombok.Data;

import java.util.HashMap;

/**
 * @Author: Owen
 * @Date: 2021/6/6 23:16
 * @Description:
 */
@Data
public class EData {

    String name;

    String id;

    String type;

    HashMap<String, Object> property;

    public EData(){}

    public EData(String name, String id, String type, HashMap<String, Object> property){
        this.name = name;
        this.id = id;
        this.type = type;
        this.property = (HashMap<String, Object>) property.clone();
    }

}
