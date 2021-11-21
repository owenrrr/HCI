package com.example.demo.blImpl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.bl.Common;
import com.example.demo.dao.EntityMapper;
import com.example.demo.dao.PositionMapper;
import com.example.demo.dao.RelationMapper;
import com.example.demo.po.Entity;
import com.example.demo.po.Position;
import com.example.demo.po.Relation;
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

    @Override
    public IOKG getKG() {
//
//        if (num > 0){
//            return randomKG(num);
//        }

        List<Entity> entities = entityMapper.getAllEntities();
        List<Relation> relations = relationMapper.getAllRelations();
        List<Position> positions = positionMapper.getAllPositions();

        HashMap<String, PositionVO> mapToPos = new HashMap<>();

        for (Position p: positions){
            String id = p.getId();
            mapToPos.put(id, new PositionVO(p.getX(), p.getY()));
        }

        EntityVO[] arr1 = new EntityVO[entities.size()];
        RelationVO[] arr2 = new RelationVO[relations.size()];

        for (int i = 0; i < entities.size(); ++i){
            Entity tmp = entities.get(i);
            mapToPos.get(tmp.getEid());
            HashMap<String,Object> prop = stringToProp(tmp.getProperty());
            if (mapToPos.get(tmp.getEid()) != null){
                arr1[i] = new EntityVO(new EData(tmp.getName(), String.valueOf(tmp.getEid()), tmp.getType(), prop),
                        mapToPos.get(tmp.getEid()));
            }else{
                arr1[i] = new EntityVO();
            }
        }

        // 确立outputKG 的 Relation
        for (int i = 0; i < relations.size(); ++i){
            Relation tmp = relations.get(i);
            arr2[i] = new RelationVO(new RData(tmp.getHash_id(), String.valueOf(tmp.getSource_id()),
                    String.valueOf(tmp.getTarget_id()), tmp.getRelation(), tmp.getType()));
        }


        IOKG ioKG = new IOKG();
        ioKG.setEdges(arr2);
        ioKG.setNodes(arr1);

        return ioKG;
    }

    private HashMap<String, Object> stringToProp(String content){
        HashMap<String, Object> params = null;
        try{
            params = JSONObject.parseObject(content, HashMap.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return params;
    }

//    /**
//     * @param num 随机挑选num个实体返回
//     */
//    private IOKG randomKG(Integer num){
//
//        List<Relation> relationList = relationMapper.getAllRelations();
//        List<Entity> entityList = entityMapper.getAllEntities();
//
//        List<Entity> mid1 = new ArrayList<>();
//        List<Entity> mid2 = new ArrayList<>();
//        List<Relation> mid3 = new ArrayList<>();
//
//        for (Entity e: entityList){
//            String prop = e.getProperty();
//            if (prop != null && !prop.equals("{}") && prop.length() > 20){
//                mid1.add(e);
//            }
//        }
//
//        for (Entity e: mid1){
//            int connections = relationMapper.getConnections(e.getName());
//            if (connections >= num - 1){
//                mid2.add(e);
//            }
//        }
//
//        Random r = new Random();
//        int random = r.nextInt(mid2.size());
//
//        // point: 随机选择的中心节点
//        Entity point = mid2.get(random);
//        List<Relation> list1 = relationMapper.getRelationsBySource(point.getName());
//        List<Relation> list2 = relationMapper.getRelationsByTarget(point.getName());
//        mid3.addAll(list1);
//        mid3.addAll(list2);
//
//        // 获取随机n个下标
//        HashSet<Integer> set = new HashSet<>();
//        randomSet(0, mid3.size() - 1, num, set);
//
//        // 获取位置Map
//        List<Position> positions = positionMapper.getAllPositions();
//        HashMap<Integer, PositionVO> mapToPos = new HashMap<>();
//        for (Position p: positions){
//            int id = p.getId();
//            mapToPos.put(id, new PositionVO(p.getX(), p.getY()));
//        }
//
//        List<EntityVO> resEnt = new ArrayList<>();
//        List<RelationVO> resRel = new ArrayList<>();
//
//        for (Integer i: set){
//            Relation relation = mid3.get(i);
//            int id = relation.getSource_id() == point.getEid() ? relation.getTarget_id() : relation.getSource_id();
//            Entity entity = entityMapper.getEntityById(id);
//
//            resEnt.add(new EntityVO(new EData(entity.getName(), String.valueOf(entity.getEid()), entity.getType(), stringToProp(entity.getProperty())),
//                    mapToPos.get(entity.getEid())));
//            resRel.add(new RelationVO(new RData(relation.getHash_id(), String.valueOf(relation.getSource_id()),
//                    String.valueOf(relation.getTarget_id()), relation.getRelation(), relation.getType())));
//        }
//
//        IOKG iokg = new IOKG();
//        EntityVO[] nodes = resEnt.toArray(new EntityVO[resEnt.size()]);
//        RelationVO[] edges = resRel.toArray(new RelationVO[resRel.size()]);
//        iokg.setNodes(nodes);
//        iokg.setEdges(edges);
//
//        return iokg;
//    }
//
//    private void randomSet(int min, int max, int n, HashSet<Integer> set) {
//        if (n > (max - min + 1) || max < min) {
//            return;
//        }
//        for (int i = 0; i < n; i++) {
//            int num = (int) (Math.random() * (max - min)) + min;
//            set.add(num);
//        }
//        int setSize = set.size();
//        if (setSize < n) {
//            randomSet(min, max, n - setSize, set);// 递归
//        }
//    }
}
