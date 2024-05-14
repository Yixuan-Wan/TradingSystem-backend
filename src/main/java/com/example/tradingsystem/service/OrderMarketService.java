package com.example.tradingsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tradingsystem.DTO.OrderResultDTO;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.OrderLimit;
import com.example.tradingsystem.entity.OrderMarket;
import com.example.tradingsystem.mapper.OrderLimitMapper;
import com.example.tradingsystem.mapper.OrderMarketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderMarketService {
    @Autowired
    OrderMarketMapper orderMarketMapper;
    @Autowired
    OrderLimitMapper orderLimitMapper;
    @Autowired
    MarketBuyService marketBuyService;
    @Autowired
    MarketSellService marketSellService;
    @Autowired
    TransactionService transactionService;

    public ApiResponse createMarketOrder(OrderMarket order){
        orderMarketMapper.insert(order);
        ExecuteMarket(order);
        return ApiResponse.success(order);
    }

    public void ExecuteMarket(OrderMarket order){
        if(order.getOrderType()==0){
            ExecuteMarketBuy(order);
        }else if(order.getOrderType()==1){
            ExecuteMarketSell(order);
        }
    }

    public void ExecuteMarketBuy(OrderMarket order){
        QueryWrapper<OrderLimit> wrapper = new QueryWrapper<>();

        wrapper.eq("product_id",order.getProductId())
                .eq("order_type",1)
                .eq("broker_id",order.getBrokerId())
                .gt("remain_quantity",0);

        executeBuyOrder(order, wrapper);
    }
    public void ExecuteMarketSell(OrderMarket order){
        QueryWrapper<OrderLimit> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",order.getProductId())
                .eq("order_type",0)
                .eq("broker_id",order.getBrokerId())
                .gt("remain_quantity",0);

        executeSellOrder(order, wrapper);
    }

    private void executeBuyOrder(OrderMarket order, QueryWrapper<OrderLimit> wrapper) {
        List<OrderLimit> list = orderLimitMapper.selectList(wrapper);
        if(list.isEmpty()){
            return;
        }
        Collections.sort(list);
        int remaining = order.getQuantity();
        for(int i=0; i<list.size() && remaining>0; i++){
            OrderLimit sellOrder = list.get(i);
            int delete_vol = 0;
            if(remaining >= sellOrder.getRemainQuantity()){
                delete_vol = sellOrder.getRemainQuantity();
                remaining = remaining - sellOrder.getRemainQuantity();
                sellOrder.setRemainQuantity(0);
            }else {
                delete_vol = remaining;
                sellOrder.setRemainQuantity(sellOrder.getQuantity() - remaining);
                remaining = 0;
            }
            transactionService.addTransaction(order.getBrokerId(),order.getProductId(), sellOrder.getPrice(), order.getTraderId(),sellOrder.getTraderId(),order.getOrderType(),sellOrder.getOrderType(),order.getOrderId(),sellOrder.getOrderId(),delete_vol);
            orderLimitMapper.updateById(sellOrder);
            marketSellService.deleteFromSellMarket(sellOrder.getProductId(), sellOrder.getPrice(), delete_vol);
        }
        order.setRemainQuantity(remaining);
        orderMarketMapper.updateById(order);
    }

    private void executeSellOrder(OrderMarket order, QueryWrapper<OrderLimit> wrapper) {
        List<OrderLimit> orderBuys = orderLimitMapper.selectList(wrapper);
        if(orderBuys.isEmpty()){
            return;
        }
        Collections.sort(orderBuys);
        Collections.reverse(orderBuys);
        int remaining = order.getQuantity();
        for(int i=0; i<orderBuys.size() && remaining>0 ; i++){
            OrderLimit buyOrder = orderBuys.get(i);
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
            transactionService.addTransaction(order.getBrokerId(),order.getProductId(), buyOrder.getPrice(), order.getTraderId(),buyOrder.getTraderId(),order.getOrderType(),buyOrder.getOrderType(),order.getOrderId(),buyOrder.getOrderId(),delete_vol);
            orderLimitMapper.updateById(buyOrder);
            marketBuyService.deleteFromBuyMarket(buyOrder.getProductId(), buyOrder.getPrice(), delete_vol);
        }
        order.setRemainQuantity(remaining);
        orderMarketMapper.updateById(order);
    }

    public ApiResponse getMarketOrderResult(String orderId){
        OrderMarket orderMarket = orderMarketMapper.selectById(orderId);
        if(orderMarket==null){
            return ApiResponse.fail(-1,"No such order");
        }
        OrderResultDTO orderResultDTO = new OrderResultDTO();
        int quantityOrigin = orderMarket.getQuantity();
        int quantityRemain = orderMarket.getRemainQuantity();

        if(quantityRemain==0){
            orderResultDTO.setRes("Success");
            orderResultDTO.setDetail("Your market order has been fully executed.");
        }else if(quantityRemain==quantityOrigin){
            orderResultDTO.setRes("Failed");
            orderResultDTO.setDetail("There is no order in the corresponding market. Please come back later");
        }else{
            orderResultDTO.setRes("Partly Success!");
            orderResultDTO.setDetail("Your market order has been partly executed.");
        }

        orderResultDTO.setList(transactionService.getTransactions(orderId));

        return ApiResponse.success(orderResultDTO);


    }

}
