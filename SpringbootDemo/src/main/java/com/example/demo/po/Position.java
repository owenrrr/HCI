package com.example.demo.po;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/6/7 0:27
 * @Description:
 */
@Data
public class Position {

    private String id;

    private double x;

    private double y;

    public Position(String id, double x, double y){
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
