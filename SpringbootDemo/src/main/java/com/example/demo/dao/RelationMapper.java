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

    Relation getRelationById(@Param("rid") Integer rid, @Param("pid") Integer pid);

    List<Relation> getRelationsByRelation(@Param("relation") String relation, @Param("pid") Integer pid);

    List<Relation> getRelationsBySourceAndRelation(@Param("source") String source, @Param("relation") String relation, @Param("pid") Integer pid);

    List<Relation> getRelationsByTargetAndRelation(@Param("target") String target, @Param("relation") String relation, @Param("pid") Integer pid);

    List<Relation> getRelationsByTwoEntities(@Param("source") String source, @Param("target") String target, @Param("pid") Integer pid);

    List<Relation> getRelationsBySource(@Param("source") String source, @Param("pid") Integer pid);

    List<Relation> getRelationsByTarget(@Param("target") String target, @Param("pid") Integer pid);

    // addition-0607
    int insertRelations(@Param("list") List<Relation> list);

    int updateRelations(@Param("list") List<Relation> list);

    int deleteRelations(@Param("list") List<Relation> list);

    int truncateAllRelations(@Param("pid") Integer pid);

    List<Relation> getAllRelations();

    List<Relation> getRelationsByPid(@Param("pid") Integer pid);

    int getConnections(@Param("content") String content, @Param("pid") Integer pid);

}
