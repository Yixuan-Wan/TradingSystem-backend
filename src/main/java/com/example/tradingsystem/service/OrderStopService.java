package com.example.tradingsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.tradingsystem.DTO.OrderDetailDTO;
import com.example.tradingsystem.DTO.OrderResultDTO;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.common.PagedResponse;
import com.example.tradingsystem.entity.*;
import com.example.tradingsystem.mapper.BrokerMapper;
import com.example.tradingsystem.mapper.OrderStopMapper;
import com.example.tradingsystem.mapper.ProductMapper;
import com.example.tradingsystem.mapper.TraderMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderStopService {
    @Autowired
    OrderStopMapper orderStopMapper;
    @Autowired
    MarketBuyService marketBuyService;
    @Autowired
    MarketSellService marketSellService;
    @Autowired
    OrderMarketService orderMarketService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    TraderMapper traderMapper;
    @Autowired
    BrokerMapper brokerMapper;
    @Autowired
    ProductMapper productMapper;

    public ApiResponse createStopOrder(OrderStop orderStop){
        orderStopMapper.insert(orderStop);
        if(orderStop.getOrderType()==0){
            Long sellMarket = marketSellService.getLowestPrice();
            if(sellMarket >= orderStop.getStopPrice()){
                executeStopOrder(orderStop);
            }
        } else if (orderStop.getOrderType()==1) {
            Long buyMarket = marketBuyService.getHighestPrice();
            if(buyMarket <= orderStop.getStopPrice()){
                executeStopOrder(orderStop);
            }
        } else{
            return ApiResponse.fail(-1,"Error Order Type");
        }
        return ApiResponse.success(orderStop);
    }

    public void executeStopOrder(OrderStop orderStop){
        OrderMarket orderMarket = new OrderMarket();
        BeanUtils.copyProperties(orderStop,orderMarket);
        QueryWrapper<OrderLimit> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",orderStop.getProductId())
                .eq("broker_id",orderStop.getBrokerId())
                .gt("remain_quantity",0);
        int remaining = orderStop.getRemainQuantity();
        if(orderStop.getOrderType()==0){
            wrapper.eq("order_type",1);
            remaining = orderMarketService.executeBuyOrder(orderMarket, wrapper);
        }else{
            wrapper.eq("order_type",0);
            remaining = orderMarketService.executeSellOrder(orderMarket,wrapper);
        }

        orderStop.setRemainQuantity(remaining);
        orderStop.setIsFinished(1);
        orderStopMapper.updateById(orderStop);

    }

    public ApiResponse getStopOrderResult(String orderId){

        OrderStop orderStop = orderStopMapper.selectById(orderId);
        if(orderStop==null){
            return ApiResponse.fail(-1,"No such order");
        }

        OrderResultDTO orderResultDTO = new OrderResultDTO();

        int quantityOrigin = orderStop.getQuantity();
        int quantityRemain = orderStop.getRemainQuantity();

        if(quantityRemain<quantityOrigin){
            orderResultDTO.setRes("Executed");
            orderResultDTO.setDetail("Your Stop order has been executed.");
        }else{
            orderResultDTO.setRes("Pending");
            orderResultDTO.setDetail("Your Stop order is pending and has not been executed yet.");
        }

        orderResultDTO.setList(transactionService.getTransactions(orderId));
        return ApiResponse.success(orderResultDTO);

    }

    public ApiResponse cancelStopOrder(String orderId, int quantity){
        OrderStop orderStop = orderStopMapper.selectById(orderId);
        if(orderStop.getIsFinished()==1){
            return ApiResponse.fail(-1,"This order has already been executed");
        }
        int remain = orderStop.getRemainQuantity();
        if(quantity > remain){
            return  ApiResponse.fail(-2,"Insufficient quantity");
        }
        remain = remain - quantity;
        if(remain==0){
            orderStop.setIsFinished(1);
        }
        orderStop.setRemainQuantity(remain);
        orderStopMapper.updateById(orderStop);
        return ApiResponse.success();
    }

    public ApiResponse getOrderList(String traderId, String brokerId, Integer pageNo, Integer pageSize) {
        QueryWrapper<OrderStop> wrapper = new QueryWrapper<>();
        if (traderId != null) {
            wrapper.eq("trader_id", traderId);
        }
        if (brokerId != null) {
            wrapper.eq("broker_id", brokerId);
        }
        Page<OrderStop> page = new Page<>(pageNo, pageSize);
        page = orderStopMapper.selectPage(page, wrapper);
        List<OrderStop> list = page.getRecords();
        List<OrderDetailDTO> res = new ArrayList<>();
        for(OrderStop order : list){
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            Trader t = traderMapper.selectById(order.getTraderId());
            Broker b = brokerMapper.selectById(order.getBrokerId());
            Product p = productMapper.selectById(order.getProductId());
            BeanUtils.copyProperties(order,orderDetailDTO);
            orderDetailDTO.setBrokerName(b.getName());
            orderDetailDTO.setTraderCompany(t.getCompany());
            orderDetailDTO.setTraderName(t.getName());
            orderDetailDTO.setProductName(p.getProductName());
            if (order.getOrderType()==0){
                orderDetailDTO.setOrderSide("buy");
            }else {
                orderDetailDTO.setOrderSide("sell");
            }
            res.add(orderDetailDTO);
        }

        PagedResponse<OrderDetailDTO> response = new PagedResponse<>(
                (int) page.getPages(),
                (int) page.getCurrent(),
                res
        );
        return ApiResponse.success(response);
    }

}
