package com.example.tradingsystem.service;

import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    ProductMapper productMapper;
    public ApiResponse getProductList(){
        return ApiResponse.success(productMapper.selectList(null));
    }
}
