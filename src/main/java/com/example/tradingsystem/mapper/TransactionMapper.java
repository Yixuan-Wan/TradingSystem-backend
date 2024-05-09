package com.example.tradingsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tradingsystem.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {
}
