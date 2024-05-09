package com.example.tradingsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tradingsystem.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    int save(Order order);
}