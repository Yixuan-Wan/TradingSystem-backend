package com.example.tradingsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tradingsystem.DTO.UserDTO;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.Broker;
import com.example.tradingsystem.entity.Trader;
import com.example.tradingsystem.mapper.BrokerMapper;
import com.example.tradingsystem.mapper.TraderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraderService {
    @Autowired
    TraderMapper traderMapper;
    @Autowired
    BrokerMapper brokerMapper;

    public ApiResponse getAllTraders(){

        return ApiResponse.success(traderMapper.selectList(null));
    }

    public ApiResponse login(String mail, String password) {
        QueryWrapper<Trader> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("mail",mail).eq("password",password);
        Trader trader = traderMapper.selectOne(wrapper1);

        UserDTO userDTO = new UserDTO();

        if (trader != null) {
            userDTO.setUserId(trader.getTraderId());
            userDTO.setUserName(trader.getName());
            userDTO.setUserType("0");
            userDTO.setMail(trader.getMail());
            userDTO.setCompany(trader.getCompany());
            return ApiResponse.success(userDTO);
        }
        QueryWrapper<Broker> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("mail",mail).eq("password",password);
        Broker broker = brokerMapper.selectOne(wrapper2);
        if(broker!=null){
            userDTO.setUserId(broker.getBrokerId());
            userDTO.setUserName(broker.getName());
            userDTO.setUserType("1");
            userDTO.setMail(broker.getMail());
            userDTO.setCompany(broker.getCompany());
            return ApiResponse.success(userDTO);
        }


        return ApiResponse.fail(-1,"用户名或密码错误");
    }

    public ApiResponse register(Trader trader){
        int result = traderMapper.insert(trader);
        return ApiResponse.success(result);
    }


}
