package com.example.demo.vo.Entity;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/6/6 23:14
 * @Description:
 */
@Data
public class EntityVO {

    EData data;

    PositionVO position;

    public EntityVO(){}

    public EntityVO(EData eData, PositionVO position){
        this.data = eData;
        this.position = position;
    }
}
