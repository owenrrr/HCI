package com.example.demo.vo.Entity;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/6/6 23:15
 * @Description:
 */
@Data
public class PositionVO {

    double x;

    double y;

    public PositionVO(){}

    public PositionVO(double x, double y){
        this.x = x;
        this.y = y;
    }
}
