package com.example.demo.vo.Relation;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/6/6 23:21
 * @Description:
 */
@Data
public class RData {

    String id;

    String source;

    String target;

    String relation;

    String type;

    public RData(){}

    public RData(String id, String source, String target, String relation, String type){
        this.id = id;
        this.source = source;
        this.target = target;
        this.relation  =relation;
        this.type = type;
    }
}
