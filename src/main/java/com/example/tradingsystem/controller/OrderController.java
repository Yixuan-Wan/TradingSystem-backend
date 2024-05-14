package com.example.tradingsystem.controller;


import com.example.tradingsystem.DTO.OrderCancelDTO;
import com.example.tradingsystem.entity.OrderCancel;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.OrderLimit;
import com.example.tradingsystem.entity.OrderMarket;
import com.example.tradingsystem.service.OrderCancelService;
import com.example.tradingsystem.service.OrderLimitService;
import com.example.tradingsystem.service.OrderMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    @Autowired
    OrderLimitService orderLimitService;
    @Autowired
    OrderMarketService orderMarketService;
    @Autowired
    OrderCancelService orderCancelService;
    @PostMapping ("/add/limit")
    public ApiResponse createLimitOrder(@RequestBody OrderLimit order){
        return orderLimitService.createLimitOrder(order);
    }

    @PostMapping("/add/market")
    public ApiResponse createMarketOrder(@RequestBody OrderMarket order){
        return orderMarketService.createMarketOrder(order);
    }

    @PostMapping("/add/cancel")
    public ApiResponse createCancelOrder(@RequestBody OrderCancelDTO orderCancel){
        return orderCancelService.createCancelOrder(orderCancel);
    }

    @GetMapping("/result/market")
    public ApiResponse getMarketOrderResult(@RequestParam String orderId){
        return orderMarketService.getMarketOrderResult(orderId);
    }

    @GetMapping("/result/limit")
    public ApiResponse getLimitOrderResult(@RequestParam String orderId){
        return orderLimitService.getLimitOrderResult(orderId);
    }
    @GetMapping("/list")
    public ApiResponse getOrderIdList(
            @RequestParam(required = false) String traderId,
            @RequestParam(required = false) String brokerId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return orderLimitService.getOrderIdList(traderId, brokerId, pageNo, pageSize);
    }

    @GetMapping("/result/cancel")
    public ApiResponse getCancelOrderResult(@RequestParam String orderId){
        return orderCancelService.getCancelOrderResult(orderId);
    }



}
