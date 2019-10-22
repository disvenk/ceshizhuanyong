package com.ceshizhuanyong.mapper;


import com.ceshizhuanyong.model.ElectronicTicketConfig;

public interface ElectronicTicketConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ElectronicTicketConfig record);

    int insertSelective(ElectronicTicketConfig record);

    ElectronicTicketConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ElectronicTicketConfig record);

    int updateByPrimaryKey(ElectronicTicketConfig record);
}