package com.example.demo.blImpl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.bl.GrammarAnalyseService;
import com.example.demo.dao.EntityMapper;
import com.example.demo.dao.PositionMapper;
import com.example.demo.dao.RelationMapper;
import com.example.demo.po.Entity;
import com.example.demo.po.Position;
import com.example.demo.po.Relation;
import com.example.demo.vo.Entity.EData;
import com.example.demo.vo.Entity.EntityVO;
import com.example.demo.vo.Entity.PositionVO;
import com.example.demo.vo.GrammarToken;
import com.example.demo.util.Match;
import com.example.demo.util.Util;
import com.example.demo.vo.Characters;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class GrammarAnalyseImpl implements GrammarAnalyseService {

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    RelationMapper relationMapper;

    @Autowired
    PositionMapper positionMapper;

    public GrammarToken grammarAnalyse(String question, Integer pid) {

        Segment segment = HanLP.newSegment().enableTranslatedNameRecognize(true);
        List<Term> termList = segment.seg(question);
        System.out.println(termList);

        List<String> resArray = Util.filter2(termList);

        // resArray: 符合规则的（名词，根节点）

        List<Match> formalWords = new LinkedList<>();
        for (String word : resArray) {
            Match temp = Util.subClassifier(word);
            formalWords.add(temp);
        }
        // formalWords: List<Match> 赋予类别

        System.out.println("---------------------------------------------");
        List<String> res = new LinkedList<>();
        List<EntityVO> nodes = new LinkedList<>();
        List<String> nodeIds = new LinkedList<>();

        if (formalWords.size() != 0 && formalWords.get(0).type.equals("character")) {
            subAnalyse(formalWords, res, nodeIds, pid);

            for (String s : res) {
                System.out.println(s);
            }

            System.out.println("---------------------------------------------");

            for (String node : nodeIds) {
                Entity e = entityMapper.getEntityById(node, pid);
                System.out.println(e.getName());
                HashMap property = JSONObject.parseObject(e.getProperty(), HashMap.class);
                EData data = new EData(e.getName(), e.getEid(), e.getType(), property);
                Position position = positionMapper.getPositionById(e.getEid(), pid);
                PositionVO positionVO = new PositionVO(position.getX(), position.getY());
                EntityVO entityVO = new EntityVO(data, positionVO);

                nodes.add(entityVO);
            }
        }

        return new GrammarToken(res, nodes);
    }

    public void subAnalyse(List<Match> words, List<String> res, List<String> nodeIds, int pid) {
        if (words.size() < 2) {

            if (words.get(0).type.equals("character")) {
                Entity e = entityMapper.getEntityByName(words.get(0).match_content);
                nodeIds.add(e.getEid());
            }

            res.add(words.get(0).match_content);

            return;
        }

        if (words.get(1).type.equals("none")) {

            if (words.get(0).type.equals("character")) {
                Entity e = entityMapper.getEntityByName(words.get(0).match_content);
                nodeIds.add(e.getEid());
            }


            res.add(words.get(0).match_content);
            return;
        }
        else {
            Match entity1 = words.get(0);
            Match entity2 = words.get(1);

            if (entity1.type.equals("character") && entity2.type.equals("relation")) {
                List<Relation> r = relationMapper.getRelationsBySourceAndRelation(entity1.match_content, entity2.match_content, pid);

                if (r.size() == 0) {
                    return;
                }
                else if (r.size() == 1) {
                    Match target = new Match(0, r.get(0).getTarget(), r.get(0).getTarget(), "character");
                    words.remove(0);
                    words.remove(0);
                    words.add(0, target);
                    subAnalyse(words, res, nodeIds, pid);
                }
                else {
                    words.remove(0);
                    words.remove(0);
                    for (Relation temp : r) {
                        List<Match> tmp = new LinkedList<>(words);
                        Match target = new Match(0, temp.getTarget(), temp.getTarget(), "character");
                        tmp.add(0, target);
                        subAnalyse(tmp, res, nodeIds, pid);
                    }
                }
            }
            else if (entity1.type.equals("character") && entity2.type.equals("group")) {
                if (entity2.match_content.equals("成员")) {
                    List<Relation> r = relationMapper.getRelationsByTargetAndRelation(entity1.match_content, "从属", pid);
                    if (r.size() == 0) {
                        return;
                    }
                    else if (r.size() == 1) {
                        Match target = new Match(0, r.get(0).getSource(), r.get(0).getSource(), "character");
                        words.remove(0);
                        words.remove(0);
                        words.add(0, target);
                        subAnalyse(words, res, nodeIds, pid);
                    }
                    else {
                        words.remove(0);
                        words.remove(0);
                        for (Relation temp : r) {
                            List<Match> tmp = new LinkedList<>(words);
                            Match target = new Match(0, temp.getSource(), temp.getSource(), "character");
                            tmp.add(0, target);
                            subAnalyse(tmp, res, nodeIds, pid);
                        }
                    }
                }
                else {
                    List<Relation> r = relationMapper.getRelationsBySourceAndRelation(entity1.match_content, "从属", pid);
                    if (r.size() == 0) {
                        return;
                    }
                    else if (r.size() == 1) {
                        Match target = new Match(0, r.get(0).getTarget(), r.get(0).getTarget(), "character");
                        words.remove(0);
                        words.remove(0);
                        words.add(0, target);
                        subAnalyse(words, res, nodeIds, pid);
                    }
                    else {
                        words.remove(0);
                        words.remove(0);
                        for (Relation temp : r) {
                            List<Match> tmp = new LinkedList<>(words);
                            Match target = new Match(0, temp.getTarget(), temp.getTarget(), "character");
                            tmp.add(0, target);
                            subAnalyse(tmp, res, nodeIds, pid);
                        }
                    }
                }
            }
            else if (entity1.type.equals("character") && entity2.type.equals("property")) {
                Entity ent = entityMapper.getEntityByName(entity1.match_content);
                nodeIds.add(ent.getEid());
                if (ent != null){
                    Characters tmp = jsonToObject(ent.getProperty());
                    if (tmp != null){
                        res.add(propertyPattern(tmp, entity1.match_content, entity2.match_content));
                        return;
                    }
                    else {
                        return;
                    }
                }
                else {
                    return;
                }
            }
            else if (entity1.type.equals("character") && entity2.type.equals("character")) {
                List<Relation> r1 = relationMapper.getRelationsByTwoEntities(entity1.match_content, entity2.match_content, pid);
                nodeIds.add(entityMapper.getEntityByName(entity1.match_content).getEid());
                nodeIds.add(entityMapper.getEntityByName(entity2.match_content).getEid());
                if (r1.size() == 0) {
                    return;
                }
                else {
                    for (Relation r : r1) {
                        res.add(r.getRelation());
                    }
                    return;
                }
            }
        }

    }

    public Characters jsonToObject(String jsonString){
        Characters character = JSONObject.parseObject(jsonString, Characters.class);
        return character;
    }

    private String propertyPattern(Characters character, String match, String property){
        StringBuilder builder = new StringBuilder();

        // 0: 它； 1：他； 2：她
        String it = "它";
        if (character.getGender() != null && property.equals("性别")){
            if (character.getGender().equals("男")){
                it = "他";
            }else{
                it = "她";
            }
            builder.append(it);
            return String.valueOf(builder);
        }

        if (character.getBirth() != null && property.equals("出生")){
            builder.append(character.getBirth());
            return String.valueOf(builder);
        }
        if (character.getBlood() != null && property.equals("血统")){
            builder.append(character.getBlood());
            return String.valueOf(builder);
        }
        if (character.getMarry() != null && property.equals("婚姻状况")){
            builder.append(character.getMarry());
            return String.valueOf(builder);
        }
        if (character.getSpecies() != null && property.equals("物种")){
            builder.append(character.getSpecies());
            return String.valueOf(builder);
        }
        if (character.getDead() != null && property.equals("逝世")) {
            builder.append(character.getDead());
            return String.valueOf(builder);
        }
        if (character.getCollege() != null && property.equals("学院")) {
            builder.append(character.getCollege());
            return String.valueOf(builder);
        }
        if (character.getJobs() != null && character.getJobs().length > 0 && property.equals("职业")){
            String[] jobs = character.getJobs();
            for (int i = 0; i < jobs.length; ++i){
                if (i != jobs.length - 1){
                    builder.append(String.format("%s、", jobs[i]));
                }else{
                    builder.append(String.format("%s", jobs[i]));
                }
            }
            return String.valueOf(builder);
        }

        return String.valueOf(builder);
    }
}
