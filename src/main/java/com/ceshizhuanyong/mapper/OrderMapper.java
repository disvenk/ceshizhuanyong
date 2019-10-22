package com.ceshizhuanyong.mapper;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int deleteById(@Param("id") String id);
}
