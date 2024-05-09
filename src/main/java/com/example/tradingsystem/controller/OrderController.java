package com.example.tradingsystem.controller;


import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.Order;
import com.example.tradingsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping ("/addLimitOrder")
    public ApiResponse createLimitOrder(@RequestBody Order order){
        return orderService.createLimitOrder(order);
    }

}
