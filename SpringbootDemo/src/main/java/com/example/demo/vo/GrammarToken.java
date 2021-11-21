package com.example.demo.vo;

import com.example.demo.vo.Entity.EntityVO;
import lombok.Data;

import java.util.List;

@Data
public class GrammarToken {

    List<String> ans;

    List<EntityVO> nodes;

    public GrammarToken(List<String> ans, List<EntityVO> nodes) {
        this.ans = ans;
        this.nodes = nodes;
    }

}

