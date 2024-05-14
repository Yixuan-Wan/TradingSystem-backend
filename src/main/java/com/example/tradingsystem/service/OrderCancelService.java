package com.example.tradingsystem.service;

import com.example.tradingsystem.DTO.OrderCancelDTO;
import com.example.tradingsystem.DTO.OrderResultDTO;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.OrderCancel;
import com.example.tradingsystem.entity.OrderLimit;
import com.example.tradingsystem.entity.OrderMarket;
import com.example.tradingsystem.mapper.OrderCancelMapper;
import com.example.tradingsystem.mapper.OrderLimitMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderCancelService {
    @Autowired
    OrderCancelMapper orderCancelMapper;
    @Autowired
    OrderLimitService orderLimitService;
    @Autowired
    OrderLimitMapper orderLimitMapper;
    public ApiResponse createCancelOrder(OrderCancelDTO order){
        OrderCancel orderCancel = new OrderCancel();
        BeanUtils.copyProperties(order,orderCancel);
        int res =  orderLimitService.executeCancelOrder(orderCancel);
        orderCancel.setIsSuccess(res);
        orderCancelMapper.insert(orderCancel);
        return ApiResponse.success(orderCancel);
    }

    public ApiResponse getCancelOrderResult(String orderId){
        OrderCancel orderCancel = orderCancelMapper.selectById(orderId);
        if(orderCancel==null){
            return ApiResponse.fail(-1,"No such order");
        }
        OrderResultDTO orderResultDTO = new OrderResultDTO();
        int res = orderCancel.getIsSuccess();
        String orderOrigin = orderCancel.getDeleteOrderId();

        if(res==1){
            orderResultDTO.setRes("Success");
            orderResultDTO.setDetail("Your cancel order has been executed.");
            List<OrderLimit> list = new ArrayList<>();
            list.add(orderLimitMapper.selectById(orderOrigin));
            orderResultDTO.setOrderList(list);
        }else if(res==-1){
            orderResultDTO.setRes("Failed");
            orderResultDTO.setDetail("The number of quantity in the cancel order is greater than the remaining quantity");
        }else if(res==-2){
            orderResultDTO.setRes("Failed");
            orderResultDTO.setDetail("Unknown Order Id");
        }

        return ApiResponse.success(orderResultDTO);
    }

}
