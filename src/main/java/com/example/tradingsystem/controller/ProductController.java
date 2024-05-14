package com.example.tradingsystem.controller;

import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/all")
    public ApiResponse getAllProduct(){
        return productService.getProductList();
    }
}
