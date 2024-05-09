package com.example.tradingsystem.service;

import com.example.tradingsystem.entity.Order;
import com.example.tradingsystem.entity.Transaction;
import com.example.tradingsystem.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionMapper transactionMapper;

    public void addTransaction(Order order1, Order order2, int quantity){
        Transaction transaction = new Transaction();
        transaction.setBrokerId(order1.getBrokerId());
        transaction.setProductId(order1.getProductId());
        transaction.setPrice(order1.getPrice());
        transaction.setQuantity(quantity);
        transaction.setTrader1Id(order1.getTraderId());
        transaction.setTrader1Side(order1.getOrderType());
        transaction.setTrader2Id(order2.getTraderId());
        transaction.setTrader2Side(order2.getOrderType());
        transaction.setOrder1Id(order1.getOrderId());
        transaction.setOrder2Id(order2.getOrderId());

        transactionMapper.insert(transaction);

    }
}
