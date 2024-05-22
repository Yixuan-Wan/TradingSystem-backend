package com.example.tradingsystem.service;

import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.Broker;
import com.example.tradingsystem.mapper.BrokerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrokerService {
    @Autowired
    BrokerMapper brokerMapper;

    public ApiResponse<List> getBrokerList(){
        List<Broker> list = brokerMapper.selectList(null);
        return ApiResponse.success(list);
    }


}
