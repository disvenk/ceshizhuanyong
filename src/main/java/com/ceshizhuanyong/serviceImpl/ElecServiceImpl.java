package com.ceshizhuanyong.serviceImpl;

import com.ceshizhuanyong.mapper.ElectronicTicketConfigMapper;
import com.ceshizhuanyong.mapper.OrderMapper;
import com.ceshizhuanyong.mapper.WechatConfigMapper;
import com.ceshizhuanyong.model.ElectronicTicketConfig;
import com.ceshizhuanyong.model.WechatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElecServiceImpl {

    @Autowired
    ElectronicTicketConfigMapper electronicTicketConfigMapper;

    @Autowired
    WechatConfigMapper wechatConfigMapper;

    @Autowired
    OrderMapper orderMapper;

    public void update(ElectronicTicketConfig electronicTicket){
        electronicTicketConfigMapper.updateByPrimaryKeySelective(electronicTicket);
    }

    public void update(WechatConfig wechatConfig){
        wechatConfigMapper.updateByPrimaryKeySelective(wechatConfig);
    }

    public int deleteById(String id){
        return orderMapper.deleteById(id);
    }
}
