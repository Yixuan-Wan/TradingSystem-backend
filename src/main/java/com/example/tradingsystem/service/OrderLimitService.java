package com.example.tradingsystem.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.tradingsystem.DTO.OrderResultDTO;
import com.example.tradingsystem.DTO.TransactionDTO;
import com.example.tradingsystem.common.PagedResponse;
import com.example.tradingsystem.entity.OrderCancel;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.OrderLimit;
import com.example.tradingsystem.mapper.OrderLimitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderLimitService {
    @Autowired
    OrderLimitMapper orderLimitMapper;
    @Autowired
    MarketBuyService marketBuyService;
    @Autowired
    MarketSellService marketSellService;
    @Autowired
    TransactionService transactionService;
    public ApiResponse createLimitOrder(OrderLimit order){
        orderLimitMapper.insert(order);
        ExecuteLimit(order);
        if(order.getOrderType()==0){
            marketBuyService.addOrderToMarket(order);
        } else if (order.getOrderType()==1) {
            marketSellService.addOrderToMarket(order);
        }
        return ApiResponse.success(order);
    }


    public int executeCancelOrder(OrderCancel order){
        String orderId = order.getDeleteOrderId();
        int cancelQuantity = order.getQuantity();
        OrderLimit originOrder = orderLimitMapper.selectById(orderId);
        if(originOrder==null){
            return -2;
        }
        int quantity = originOrder.getRemainQuantity();
        if(cancelQuantity > quantity){
            return  -1;
        }else {
            originOrder.setRemainQuantity(quantity - cancelQuantity);
            orderLimitMapper.updateById(originOrder);
            if(originOrder.getOrderType()==0){
                marketBuyService.deleteFromBuyMarket(originOrder.getProductId(),originOrder.getPrice(),cancelQuantity);
            }else {
                marketSellService.deleteFromSellMarket(originOrder.getProductId(),originOrder.getPrice(),cancelQuantity);
            }
        }
        return 1;
    }

    public void ExecuteLimit(OrderLimit order){
        if(order.getOrderType()==0){
            ExecuteLimitBuy(order);
        }else if(order.getOrderType()==1){
            ExecuteLimitSell(order);
        }
    }



    public void ExecuteMarketBuy(OrderLimit order){
        QueryWrapper<OrderLimit> wrapper = new QueryWrapper<>();

        wrapper.eq("product_id",order.getProductId())
                .eq("order_type",1)
                .eq("broker_id",order.getBrokerId())
                .gt("remain_quantity",0);

        executeBuyOrder(order, wrapper);
    }

    public void ExecuteLimitBuy(OrderLimit order){
        QueryWrapper<OrderLimit> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",order.getProductId())
                .eq("order_type",1)
                .eq("broker_id",order.getBrokerId())
                .gt("remain_quantity",0)
                .le("price",order.getPrice());

        executeBuyOrder(order, wrapper);

    }

    private void executeBuyOrder(OrderLimit order, QueryWrapper<OrderLimit> wrapper) {
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
            transactionService.addTransaction(order.getBrokerId(),order.getProductId(), order.getPrice(), order.getTraderId(),sellOrder.getTraderId(),order.getOrderType(),sellOrder.getOrderType(),order.getOrderId(),sellOrder.getOrderId(),delete_vol);
            orderLimitMapper.updateById(sellOrder);
            marketSellService.deleteFromSellMarket(sellOrder.getProductId(), sellOrder.getPrice(), delete_vol);
        }
        order.setRemainQuantity(remaining);
        orderLimitMapper.updateById(order);
    }

    public void ExecuteMarketSell(OrderLimit order){
        QueryWrapper<OrderLimit> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",order.getProductId())
                .eq("order_type",0)
                .eq("broker_id",order.getBrokerId())
                .gt("remain_quantity",0);


        executeSellOrder(order, wrapper);
    }

    public void ExecuteLimitSell(OrderLimit order){
        QueryWrapper<OrderLimit> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",order.getProductId())
                .eq("order_type",0)
                .eq("broker_id",order.getBrokerId())
                .gt("remain_quantity",0)
                .ge("price",order.getPrice());

        executeSellOrder(order, wrapper);


    }

    private void executeSellOrder(OrderLimit order, QueryWrapper<OrderLimit> wrapper) {
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
            transactionService.addTransaction(order.getBrokerId(),order.getProductId(), order.getPrice(), order.getTraderId(),buyOrder.getTraderId(),order.getOrderType(),buyOrder.getOrderType(),order.getOrderId(),buyOrder.getOrderId(),delete_vol);
            orderLimitMapper.updateById(buyOrder);
            marketBuyService.deleteFromBuyMarket(buyOrder.getProductId(), buyOrder.getPrice(), delete_vol);
        }
        order.setRemainQuantity(remaining);
        orderLimitMapper.updateById(order);
    }

    public ApiResponse getLimitOrderResult(String orderId){
        OrderLimit orderLimit = orderLimitMapper.selectById(orderId);
        if(orderLimit==null){
            return ApiResponse.fail(-1,"No such order");
        }
        OrderResultDTO orderResultDTO = new OrderResultDTO();
        int quantityOrigin =  orderLimit.getQuantity();
        int quantityRemain =  orderLimit.getRemainQuantity();

        if(quantityRemain==0){
            orderResultDTO.setRes("Success");
            orderResultDTO.setDetail("Your limit order has been fully executed.");
        }else if(quantityRemain==quantityOrigin){
            orderResultDTO.setRes("Failed");
            orderResultDTO.setDetail("There is no order that meets your requirements in the corresponding market. Please come back later");
        }else{
            orderResultDTO.setRes("Partly Success!");
            orderResultDTO.setDetail("Your limit order has been partly executed.");
        }

        orderResultDTO.setList(transactionService.getTransactions(orderId));

        return ApiResponse.success(orderResultDTO);
    }

    public ApiResponse getOrderIdList(String traderId, String brokerId, Integer pageNo, Integer pageSize) {
        QueryWrapper<OrderLimit> wrapper = new QueryWrapper<>();
        if (traderId != null) {
            wrapper.eq("trader_id", traderId);
        }
        if (brokerId != null) {
            wrapper.eq("broker_id", brokerId);
        }
        Page<OrderLimit> page = new Page<>(pageNo, pageSize);
        page = orderLimitMapper.selectPage(page, wrapper);
        List<OrderLimit> list = page.getRecords();

        PagedResponse<OrderLimit> response = new PagedResponse<>(
                (int) page.getPages(),
                (int) page.getCurrent(),
                list
        );
        return ApiResponse.success(response);
    }
}

