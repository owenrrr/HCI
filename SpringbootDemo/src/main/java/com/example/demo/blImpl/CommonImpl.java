package com.example.demo.blImpl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.bl.Common;
import com.example.demo.dao.EntityMapper;
import com.example.demo.dao.PositionMapper;
import com.example.demo.dao.RelationMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.po.Entity;
import com.example.demo.po.Position;
import com.example.demo.po.Relation;
import com.example.demo.po.User;
import com.example.demo.vo.Entity.EData;
import com.example.demo.vo.Entity.EntityVO;
import com.example.demo.vo.Entity.PositionVO;
import com.example.demo.vo.IOKG;
import com.example.demo.vo.Relation.RData;
import com.example.demo.vo.Relation.RelationVO;
import com.example.demo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: Owen
 * @Date: 2021/6/9 11:05
 * @Description:
 */
@Service
public class CommonImpl implements Common {

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    RelationMapper relationMapper;
    @Autowired
    PositionMapper positionMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public IOKG getKG(Integer pid) {

//        List<Entity> entities = entityMapper.getAllEntities();
//        List<Relation> relations = relationMapper.getAllRelations();
//        List<Position> positions = positionMapper.getAllPositions();

        List<Entity> entities = entityMapper.getEntityByPid(pid);
        List<Relation> relations = relationMapper.getRelationsByPid(pid);
        List<Position> positions = positionMapper.getPositionByPid(pid);

        HashMap<String, PositionVO> mapToPos = new HashMap<>();

        for (Position p : positions) {
            String id = p.getId();
            mapToPos.put(id, new PositionVO(p.getX(), p.getY()));
        }

        EntityVO[] arr1 = new EntityVO[entities.size()];
        RelationVO[] arr2 = new RelationVO[relations.size()];

        for (int i = 0; i < entities.size(); ++i) {
            Entity tmp = entities.get(i);
            mapToPos.get(tmp.getEid());
            HashMap<String, Object> prop = stringToProp(tmp.getProperty());
            if (mapToPos.get(tmp.getEid()) != null) {
                arr1[i] = new EntityVO(new EData(tmp.getName(), String.valueOf(tmp.getEid()), tmp.getType(), prop),
                        mapToPos.get(tmp.getEid()));
            } else {
                arr1[i] = new EntityVO();
            }
        }

        // 确立outputKG 的 Relation
        for (int i = 0; i < relations.size(); ++i) {
            Relation tmp = relations.get(i);
            arr2[i] = new RelationVO(new RData(tmp.getHash_id(), String.valueOf(tmp.getSource_id()),
                    String.valueOf(tmp.getTarget_id()), tmp.getRelation(), tmp.getType()));
        }


        IOKG ioKG = new IOKG();
        ioKG.setEdges(arr2);
        ioKG.setNodes(arr1);

        return ioKG;
    }

    private HashMap<String, Object> stringToProp(String content) {
        HashMap<String, Object> params = null;
        try {
            params = JSONObject.parseObject(content, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }


    @Override
    public int createUser(String mail, String password){
        int result = userMapper.createUser(mail, password);
        if (result <= 0) {
            return -1;
        }
        int uid = userMapper.getLastKey();
        // 插入范例项目
        userMapper.createProject(uid, "模板项目");
        int pid = userMapper.getLastKey();
        // 插入范例节点
        List<Entity> list1 = new ArrayList<>();
        Entity dad_ent = new Entity(pid, "5b1f8384-b731-44e7-a947-31fc080029fc", "父亲", "individual",
                "{'性别':'男','工作':'教职','年龄':'40岁'}");
        Entity mom_ent = new Entity(pid, "b27d3d45-585b-4a1d-a74a-97f7fbab9be5", "母亲", "individual",
                "{'性别':'女','工作':'教职','年龄':'35岁'}");
        Entity son_ent = new Entity(pid, "415702db-0638-4abe-a956-f0e23b76c510", "儿子", "individual",
                "{'性别':'男','年龄':'15岁','兴趣':'看书、游泳、打篮球'}");
        list1.add(dad_ent);
        list1.add(mom_ent);
        list1.add(son_ent);
        List<Position> list2 = new ArrayList<>();
        Position dad_pos = new Position(pid, "5b1f8384-b731-44e7-a947-31fc080029fc", 1.9820265823878318, 54.805905124095844);
        Position mom_pos = new Position(pid, "b27d3d45-585b-4a1d-a74a-97f7fbab9be5", 163.50194582924112, 56.09795031055394);
        Position son_pos = new Position(pid, "415702db-0638-4abe-a956-f0e23b76c510", 88.48410433249299, 181.13977057543852);
        list2.add(dad_pos);
        list2.add(mom_pos);
        list2.add(son_pos);
        entityMapper.insertEntities(list1);
        positionMapper.insertPositions(list2);
        List<Relation> list3 = new ArrayList<>();
        Relation dad_mom = new Relation(pid, "5b1f8384-b731-44e7-a947-31fc080029fc", "b27d3d45-585b-4a1d-a74a-97f7fbab9be5",
                "父亲", "母亲", "夫妻", "6403b01d-c925-4a12-a060-e69c04b22fac");
        Relation dad_son = new Relation(pid, "5b1f8384-b731-44e7-a947-31fc080029fc", "415702db-0638-4abe-a956-f0e23b76c510",
                "父亲", "儿子", "父子", "c17e5bbf-e430-409d-a20c-fe15b4b0e71e");
        Relation mom_son = new Relation(pid, "b27d3d45-585b-4a1d-a74a-97f7fbab9be5", "415702db-0638-4abe-a956-f0e23b76c510",
                "母亲", "儿子", "母子", "0efb956f-6d39-4129-bd98-02bebff19e10");
        list3.add(dad_mom);
        list3.add(dad_son);
        list3.add(mom_son);
        relationMapper.insertRelations(list3);

        return uid;
    }

    @Override
    public User getUser(String mail){
        User user = userMapper.getUser(mail);
        if(user == null){
            return null;
        }
        return user;
    }

    @Override
    public int createProject(Integer uid, String name){
        int result = userMapper.createProject(uid, name);
        if (result <= 0) {
            return -1;
        }
        int pid = userMapper.getLastKey();

        // 插入范例节点
        List<Entity> list1 = new ArrayList<>();
        Entity samp_ent = new Entity(pid, "1", "节点", "individual", "{'节点属性':'节点属性值'}");
        list1.add(samp_ent);
        List<Position> list2 = new ArrayList<>();
        Position samp_pos = new Position(pid, "1", 100.0, 100.0);
        list2.add(samp_pos);
        int res1 = entityMapper.insertEntities(list1);
        positionMapper.insertPositions(list2);

        if (res1 <= 0) {
            return -1;
        }

        return pid;
    }

}