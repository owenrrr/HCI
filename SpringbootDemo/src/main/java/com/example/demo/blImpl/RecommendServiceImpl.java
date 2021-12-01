package com.example.demo.blImpl;

import com.example.demo.bl.RecommendService;
import com.example.demo.bl.SynonymChecking;
import com.example.demo.dao.EntityMapper;
import com.example.demo.dao.RelationMapper;
import com.example.demo.po.Relation;
import com.example.demo.util.Match;
import com.example.demo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    RelationMapper relationMapper;

    @Autowired
    SynonymChecking synonymChecking;

    HashMap<String, Integer> entity_frequency = new HashMap<>();

    @Override
    public HashMap<String, Integer> recommend(String question, Integer pid) {
        List<String> keywords = Util.extractKeyword(question, 5);

        HashMap<String, List<Match>> result = Util.classifier(keywords);

        List<Match> entities = result.get("entity");

        for (Match entity : entities) {
            if (!entity_frequency.containsKey(entity.match_content)) {
                entity_frequency.put(entity.match_content, 1);
            }
            else {
                entity_frequency.put(entity.match_content,
                        entity_frequency.get(entity.match_content) + 1);
            }
        }


        if (entities.size() == 0) {
            return null;
        }

        List<String> relation = new LinkedList<>();


        for (Match entity : entities) {
            String ac_ent = entity.match_content;

            List<Relation> relations1 = relationMapper.getRelationsBySource(ac_ent, pid);
            List<Relation> relations2 = relationMapper.getRelationsByTarget(ac_ent, pid);


            for (Relation r : relations1) {
                relation.add(r.getTarget());
            }
            for (Relation r : relations2) {
                relation.add(r.getSource());
            }
        }

        int N = 10;
        //推荐家庭关系
        HashMap<String, Integer> fr = getN(relation, N, pid);

        List<Map.Entry<String, Integer>> frequency = new ArrayList<>(entity_frequency.entrySet());

        int total = 0;
        for (Map.Entry<String, Integer> i : frequency) {
            total += i.getValue();
        }

        for (String s : fr.keySet()) {
            if (entity_frequency.containsKey(s)) {
                fr.put(s, fr.get(s) * (1 + entity_frequency.get(s) / total));
            }
        }

        System.out.println("-------------------------------------------");

        List<Map.Entry<String, Integer>> frList = new ArrayList<>(fr.entrySet());



        frList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        List<Map.Entry<String, Integer>> frResult;
        if (frList.size() < N) {
            frResult = new LinkedList<>(frList);
        }
        else {
            frResult = frList.subList(0, N);
        }

        for (Map.Entry<String, Integer> f : frResult) {
            System.out.println(f.getKey() + ": " + f.getValue());
        }


        return fr;

    }

    public HashMap<String, Integer> getN(List<String> r, int N, int pid) {
        HashMap<String, Integer> f_model = new HashMap<>();
        for (String sr : r) {
            f_model.put(sr, relationMapper.getConnections(sr, pid));
        }

        return f_model;
    }

}
