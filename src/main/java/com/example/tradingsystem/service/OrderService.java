package com.example.tradingsystem.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.Order;
import com.example.tradingsystem.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    MarketBuyService marketBuyService;
    @Autowired
    MarketSellService marketSellService;
    @Autowired
    TransactionService transactionService;
    public ApiResponse createLimitOrder(Order order){
        orderMapper.insert(order);
        ExecuteLimit(order);
        if(order.getOrderType()==0){
            marketBuyService.addOrderToMarket(order);
        } else if (order.getOrderType()==1) {
            marketSellService.addOrderToMarket(order);
        }
        return ApiResponse.success(order);
    }

    public void ExecuteLimit(Order order){
        if(order.getOrderType()==0){
            ExecuteLimitBuy(order);
        }else if(order.getOrderType()==1){
            ExecuteLimitSell(order);
        }
    }

    public void ExecuteLimitBuy(Order order){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",order.getProductId())
                .eq("order_type",1)
                .eq("broker_id",order.getBrokerId())
                .eq("order_algorithm",1)
                .gt("remain_quantity",0)
                .le("price",order.getPrice());

        List<Order> orderSells = orderMapper.selectList(wrapper);
        if(orderSells.isEmpty()){
            return;
        }
        Collections.sort(orderSells);
        int remaining = order.getQuantity();
        for(int i=0; i<orderSells.size() && remaining>0 ; i++){
            Order sellOrder = orderSells.get(i);
            int delete_vol = 0;
            if(remaining >= sellOrder.getRemainQuantity()){
                delete_vol = sellOrder.getRemainQuantity();
                remaining = remaining - sellOrder.getRemainQuantity();
                sellOrder.setRemainQuantity(0);
            }else{
                delete_vol = remaining;
                sellOrder.setRemainQuantity(sellOrder.getQuantity() - remaining);
                remaining = 0;
            }
            transactionService.addTransaction(order,sellOrder,delete_vol);
            orderMapper.updateById(sellOrder);
            marketSellService.deleteFromSellMarket(sellOrder.getProductId(), sellOrder.getPrice(), delete_vol);
        }
        order.setRemainQuantity(remaining);
        orderMapper.updateById(order);



    }

    public void ExecuteLimitSell(Order order){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",order.getProductId())
                .eq("order_type",0)
                .eq("broker_id",order.getBrokerId())
                .eq("order_algorithm",1)
                .gt("remain_quantity",0)
                .ge("price",order.getPrice());

        List<Order> orderBuys = orderMapper.selectList(wrapper);
        if(orderBuys.isEmpty()){
            return;
        }
        Collections.sort(orderBuys);
        int remaining = order.getQuantity();
        for(int i=0; i<orderBuys.size() && remaining>0 ; i++){
            Order buyOrder = orderBuys.get(i);
            int delete_vol = 0;
            if(remaining >= buyOrder.getRemainQuantity()){
                delete_vol = buyOrder.getRemainQuantity();
                remaining = remaining - buyOrder.getRemainQuantity();
                buyOrder.setRemainQuantity(0);
            }else{
                delete_vol = remaining;
                buyOrder.setRemainQuantity(buyOrder.getQuantity() - remaining);
                remaining = 0;
            }
            transactionService.addTransaction(order,buyOrder,delete_vol);
            orderMapper.updateById(buyOrder);
            marketBuyService.deleteFromBuyMarket(buyOrder.getProductId(), buyOrder.getPrice(), delete_vol);
        }
        order.setRemainQuantity(remaining);
        orderMapper.updateById(order);



    }
}

