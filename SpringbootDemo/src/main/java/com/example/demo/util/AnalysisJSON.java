package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.EntityMapper;
import com.example.demo.dao.PositionMapper;
import com.example.demo.dao.RelationMapper;
import com.example.demo.po.Entity;
import com.example.demo.po.Position;
import com.example.demo.po.Relation;
import com.example.demo.util.aliyunOSS.PropertyReader;
import com.example.demo.vo.Entity.EntityVO;
import com.example.demo.vo.IOKG;
import com.example.demo.vo.Relation.RelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Author: Owen
 * @Date: 2021/6/6 23:10
 * @Description:
 */
@Service
public class AnalysisJSON {

//    private static final String CONFIG_PATH ="/var/lib/jenkins/workspace/seiii/SpringbootDemo/src/main/resources/voc/";
    private static final String CONFIG_PATH = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "voc")) + "\\";

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    RelationMapper relationMapper;
    @Autowired
    PositionMapper positionMapper;

    /**
     * 切换新的知识图谱
     */
    public void createKG(IOKG IOKG){

        refreshAllData(IOKG.getPid());

        try{
            insertKG(IOKG, new HashMap<String, String>());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 添加新的知识
     */
    public void addKG(IOKG IOKG){
        HashMap<String, String> map = new HashMap<>();
        // 需要包含已存在的实体和添加后的实体
        List<Entity> tmp = entityMapper.getAllEntities();

        for (com.example.demo.po.Entity e : tmp) {
            map.put(e.getEid(), e.getName());
        }
        try{
            insertKG(IOKG, map);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void insertKG(IOKG IOKG, HashMap<String, String> idToName) throws Exception {


        Integer pid = IOKG.getPid();

        EntityVO[] entities = IOKG.getNodes();

        RelationVO[] relationVOS = IOKG.getEdges();

        List<Position> list0 = new ArrayList<>();
        List<com.example.demo.po.Entity> list1 = new ArrayList<>();
        List<com.example.demo.po.Relation> list2 = new ArrayList<>();


        String eid = "", name = "", type = "";
        double x = 0.0, y = 0.0;

        for (EntityVO e : entities) {
            eid = e.getData().getId();
            name = e.getData().getName();
            type = e.getData().getType();
            x = e.getPosition().getX();
            y = e.getPosition().getY();

            idToName.put(eid, name);

            list0.add(new Position(pid, eid, x, y));

            HashMap<String, Object> property = e.getData().getProperty();

            StringBuilder builder = new StringBuilder();
            builder.append(Symbol.OPEN_BRACE.getSymbol());
            for (Map.Entry entry : property.entrySet()) {
                // 如果property是数组
                if (entry.getValue() instanceof ArrayList &&
                        ((ArrayList) entry.getValue()).get(0) instanceof String) {
                    ArrayList<String> list = (ArrayList<String>) entry.getValue();
                    builder.append(Symbol.DOUBLE_QUOTATION.getSymbol() + entry.getKey() + Symbol.DOUBLE_QUOTATION.getSymbol()
                            + Symbol.COLON.getSymbol() + Symbol.LEFT_BRACKET.getSymbol());
                    for (int i = 0; i < list.size(); ++i) {
                        builder.append(Symbol.DOUBLE_QUOTATION.getSymbol() + list.get(i) +
                                Symbol.DOUBLE_QUOTATION.getSymbol());
                        if (i != list.size() - 1) {
                            builder.append(Symbol.COMMA.getSymbol());
                        }
                    }
                    builder.append(Symbol.RIGHT_BRACKET.getSymbol() + Symbol.COMMA.getSymbol());
                } else {
                    builder.append(Symbol.DOUBLE_QUOTATION.getSymbol() + entry.getKey() + Symbol.DOUBLE_QUOTATION.getSymbol()
                            + Symbol.COLON.getSymbol() + Symbol.DOUBLE_QUOTATION.getSymbol() + entry.getValue() +
                            Symbol.DOUBLE_QUOTATION.getSymbol() + Symbol.COMMA.getSymbol());
                }
            }
            if (builder.length() > 1){
                builder.deleteCharAt(builder.length() - 1);
            }
            builder.append(Symbol.CLOSE_BRACE.getSymbol());
            list1.add(new com.example.demo.po.Entity(pid, eid, name, type, String.valueOf(builder)));
        }

        String source = "", target = "";
        String source_id = "", target_id = "";

        for (RelationVO r : relationVOS) {
            source_id = r.getData().getSource();
            target_id = r.getData().getTarget();
            source = idToName.get(source_id);
            target = idToName.get(target_id);
            list2.add(new Relation(pid, source_id, target_id, source, target,
                    r.getData().getRelation(), r.getData().getId()));
        }

        if (entities.length > 0){
            positionMapper.insertPositions(list0);
            entityMapper.insertEntities(list1);
        }
        if (relationVOS.length > 0){
            relationMapper.insertRelations(list2);
        }

        addConfigurationFiles();

    }

    private void refreshAllData(Integer pid) {

        entityMapper.truncateAllEntities(pid);

        relationMapper.truncateAllRelations(pid);

        positionMapper.truncateAllPositions(pid);
    }

    private void addConfigurationFiles() {
        List<Entity> entities = entityMapper.getAllEntities();
        List<Relation> relations = relationMapper.getAllRelations();

        HashSet<String> entityHashSet = new HashSet<>();
        HashSet<String> relationHashSet = new HashSet<>();
        HashSet<String> properties = new HashSet<>();

        for (Entity e : entities) {

            JSONObject jsonObject = JSONObject.parseObject(e.getProperty());

            for (Map.Entry me : jsonObject.entrySet()) {
                properties.add((String) me.getKey());
            }
            entityHashSet.add(e.getName());
        }
        for (Relation r : relations) {
            relationHashSet.add(r.getRelation());
        }

        try {
            writeConfigurations(entityHashSet, "characters.txt");
            writeConfigurations(relationHashSet, "relations.txt");
            writeConfigurations(properties, "property.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeConfigurations(HashSet<String> set, String filename) throws Exception {
        BufferedWriter out = new BufferedWriter(new FileWriter(
                String.format("%s%s", CONFIG_PATH, filename)));
        int count = 0;
        for (String s : set) {
            ++count;
            if (count == set.size()) {
                out.write(s);
            } else {
                out.write(s + ",");
            }
        }
        out.close();
    }
}
