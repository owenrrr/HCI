package com.example.demo.dao;

import com.example.demo.po.Entity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Owen
 * @Date: 2021/6/2 10:46
 * @Description:
 */
@Mapper
@Repository
public interface EntityMapper {

    Entity getEntityByName(@Param("name") String name);

    Entity getEntityById(@Param("eid") String eid, @Param("pid") Integer pid);

    // addition-0607
    int insertEntities(@Param("list") List<Entity> list);

    int updateEntities(@Param("list") List<Entity> list);

    int deleteEntities(@Param("list") List<Entity> list);

    int truncateAllEntities(@Param("pid") Integer pid);

    List<Entity> getEntityByPid(@Param("pid") Integer pid);

    List<Entity> getAllEntities();
}
