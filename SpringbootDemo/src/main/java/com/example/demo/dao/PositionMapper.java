package com.example.demo.dao;

import com.example.demo.po.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Owen
 * @Date: 2021/6/7 0:28
 * @Description:
 */
@Mapper
@Repository
public interface PositionMapper {

    Position getPositionById(@Param("id") String id, @Param("pid") Integer pid);

    List<Position> getPositions();

    void insertPositions(@Param("list") List<Position> list);

    void updatePositions(@Param("list") List<Position> list);

    void deletePositions(@Param("list") List<Position> list);

    int truncateAllPositions(@Param("pid") Integer pid);

    List<Position> getPositionByPid(@Param("pid") Integer pid);

    List<Position> getAllPositions();

}
