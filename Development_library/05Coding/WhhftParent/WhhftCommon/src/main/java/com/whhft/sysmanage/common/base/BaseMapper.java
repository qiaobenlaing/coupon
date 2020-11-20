package com.whhft.sysmanage.common.base;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper<E, PK extends Serializable> {
    long countByExample(Object example);
    int deleteByExample(Object example);
    int deleteByPrimaryKey(PK authId);
    int insert(E record);
    int insertSelective(E record);
    List<E> selectByExample(Object example);
    E selectByPrimaryKey(PK id);
    int updateByExampleSelective(@Param("record") E record, @Param("example") Object example);
    int updateByExample(@Param("record") E record, @Param("example") Object example);
    int updateByPrimaryKeySelective(E record);
    int updateByPrimaryKey(E record);
}
