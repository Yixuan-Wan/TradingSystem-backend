package com.example.tradingsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tradingsystem.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
