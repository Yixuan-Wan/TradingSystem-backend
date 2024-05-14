package com.example.tradingsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.Trader;
import com.example.tradingsystem.mapper.TraderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraderService {
    @Autowired
    TraderMapper traderMapper;

    public ApiResponse getAllTraders(){

        return ApiResponse.success(traderMapper.selectList(null));
    }

    public ApiResponse login(String mail, String password) {
        QueryWrapper<Trader> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("mail",mail).eq("password",password);
        Trader trader = traderMapper.selectOne(wrapper1);
        if (trader != null) {
            return ApiResponse.success(trader.getTraderId());
        }



        return ApiResponse.fail(-1,"用户名或密码错误");
    }

    public ApiResponse register(Trader trader){
        int result = traderMapper.insert(trader);
        return ApiResponse.success(result);
    }


}
