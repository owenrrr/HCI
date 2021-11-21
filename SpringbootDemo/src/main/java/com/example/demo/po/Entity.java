package com.example.demo.po;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/3/7 14:20
 * @Description:
 */
@Data
public class Entity {

    private String eid;

    private Integer pid;

    private String name;

    private String type;

    private String property;

    public Entity(){}

    public Entity(String eid, String name, String type, String property){
        this.eid = eid;
        this.name = name;
        this.type = type;
        this.property = property;
    }

    public Entity(Integer pid, String eid, String name, String type, String property){
        this.eid = eid;
        this.pid = pid;
        this.name = name;
        this.type = type;
        this.property = property;
    }

}
