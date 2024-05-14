package com.example.tradingsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tradingsystem.entity.OrderCancel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderCancelMapper extends BaseMapper<OrderCancel> {
}
