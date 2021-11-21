package com.example.demo.vo;

import com.example.demo.vo.Entity.EntityVO;
import com.example.demo.vo.Relation.RelationVO;
import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/6/6 23:23
 * @Description: Input/Output Knowledge Graph
 */
@Data
public class IOKG {

    EntityVO[] nodes;

    RelationVO[] edges;

    Integer pid;
}
