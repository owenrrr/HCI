package com.example.demo.dao;

import com.example.demo.po.Relation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Owen
 * @Date: 2021/6/2 14:11
 * @Description:
 */
@Mapper
@Repository
public interface RelationMapper {

    Relation getRelationById(@Param("rid") Integer rid);

    List<Relation> getRelationsByRelation(@Param("relation") String relation);

    List<Relation> getRelationsBySourceAndRelation(@Param("source") String source, @Param("relation") String relation);

    List<Relation> getRelationsByTargetAndRelation(@Param("target") String target, @Param("relation") String relation);

    List<Relation> getRelationsByTwoEntities(@Param("source") String source, @Param("target") String target);

    List<Relation> getRelationsBySource(@Param("source") String source);

    List<Relation> getRelationsByTarget(@Param("target") String target);

    // addition-0607
    int insertRelations(@Param("list") List<Relation> list);

    int updateRelations(@Param("list") List<Relation> list);

    int deleteRelations(@Param("list") List<Relation> list);

    int truncateAllRelations(@Param("pid") Integer pid);

    List<Relation> getAllRelations();

    int getConnections(@Param("content") String content);

}
