package com.example.demo.vo.Relation;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/6/6 23:20
 * @Description:
 */
@Data
public class RelationVO {

    RData data;

    public RelationVO(){}

    public RelationVO(RData data){
        this.data = data;
    }
}
