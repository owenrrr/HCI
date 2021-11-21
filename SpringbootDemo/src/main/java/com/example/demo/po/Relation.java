package com.example.demo.po;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/3/7 14:40
 * @Description:
 */
@Data
public class Relation {

    private Integer rid;

    private String source_id;

    private String target_id;

    private String source;

    private String target;

    private String relation;

    private String type;

    private String hash_id;

    public Relation(String source_id, String target_id, String source, String target,
                    String relation, String hash_id){
        this.source_id = source_id;
        this.target_id = target_id;
        this.source = source;
        this.target = target;
        this.relation = relation;
        this.hash_id = hash_id;
    }


    public Relation(){
    }
}
