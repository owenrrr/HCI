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
        int id = userMapper.getLastKey();
        return id;
    }

    @Override
    public int createProject(Integer uid, String name){
        int result = userMapper.createProject(uid, name);
        if (result <= 0) {
            return -1;
        }
        int id = userMapper.getLastKey();
        return id;
    }

}