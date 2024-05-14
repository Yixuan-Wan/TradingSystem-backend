package com.example.tradingsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.tradingsystem.DTO.TransactionDTO;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.common.PagedResponse;
import com.example.tradingsystem.entity.*;
import com.example.tradingsystem.mapper.BrokerMapper;
import com.example.tradingsystem.mapper.ProductMapper;
import com.example.tradingsystem.mapper.TraderMapper;
import com.example.tradingsystem.mapper.TransactionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    TraderMapper traderMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    BrokerMapper brokerMapper;

    public void addTransaction(String brokerId, int productId, Long price, String trader1Id, String trader2Id, int trader1Type, int trader2Type,String order1Id,String order2Id, int quantity){
        Transaction transaction = new Transaction();
        transaction.setBrokerId(brokerId);
        transaction.setProductId(productId);
        transaction.setPrice(price);
        transaction.setQuantity(quantity);
        transaction.setTrader1Id(trader1Id);
        transaction.setTrader1Side(trader1Type);
        transaction.setTrader2Id(trader2Id);
        transaction.setTrader2Side(trader2Type);
        transaction.setOrder1Id(order1Id);
        transaction.setOrder2Id(order2Id);

        transactionMapper.insert(transaction);

    }

    public ApiResponse getAllTransactions(String orderId, Integer pageNo, Integer pageSize) {
        QueryWrapper<Transaction> wrapper = new QueryWrapper<>();
        if (orderId == null) {
            wrapper = null;
        } else {
            wrapper.eq("order1_id", orderId).or().eq("order2_id", orderId);
        }

        Page<Transaction> page = new Page<>(pageNo, pageSize);
        transactionMapper.selectPage(page,wrapper);

        List<Transaction> transactions = page.getRecords();


        List<TransactionDTO> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDTO dto = new TransactionDTO();
            BeanUtils.copyProperties(transaction, dto);
            Trader trader1 = traderMapper.selectById(transaction.getTrader1Id());
            Trader trader2 = traderMapper.selectById(transaction.getTrader2Id());
            Product product = productMapper.selectById(transaction.getProductId());
            Broker broker = brokerMapper.selectById(transaction.getBrokerId());

            if(dto.getTrader1Side()==0){
                dto.setSide1("buy");
            }else{
                dto.setSide1("sell");
            }

            if(dto.getTrader2Side()==0){
                dto.setSide2("buy");
            }else{
                dto.setSide2("sell");
            }
            dto.setTrader1Name(trader1.getName());
            dto.setTrader1Company(trader1.getCompany());
            dto.setTrader2Name(trader2.getName());
            dto.setTrader2Company(trader2.getCompany());
            dto.setProductName(product.getProductName());
            dto.setBrokerName(broker.getName());

            result.add(dto);
        }

        // 创建分页响应对象
        PagedResponse<TransactionDTO> response = new PagedResponse<>(
                (int) page.getPages(),
                (int) page.getCurrent(),
                result
        );

        return ApiResponse.success(response);
    }

    public List<TransactionDTO> getTransactions(String orderId){
        QueryWrapper<Transaction> wrapper = new QueryWrapper<>();
        if (orderId == null) {
            wrapper = null;
        } else {
            wrapper.eq("order1_id", orderId).or().eq("order2_id", orderId);
        }


        List<Transaction> transactions = transactionMapper.selectList(wrapper);

        List<TransactionDTO> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDTO dto = new TransactionDTO();
            BeanUtils.copyProperties(transaction, dto);
            Trader trader1 = traderMapper.selectById(transaction.getTrader1Id());
            Trader trader2 = traderMapper.selectById(transaction.getTrader2Id());
            Product product = productMapper.selectById(transaction.getProductId());
            Broker broker = brokerMapper.selectById(transaction.getBrokerId());

            if(dto.getTrader1Side()==0){
                dto.setSide1("buy");
            }else{
                dto.setSide1("sell");
            }

            if(dto.getTrader2Side()==0){
                dto.setSide2("buy");
            }else{
                dto.setSide2("sell");
            }
            dto.setTrader1Name(trader1.getName());
            dto.setTrader1Company(trader1.getCompany());
            dto.setTrader2Name(trader2.getName());
            dto.setTrader2Company(trader2.getCompany());
            dto.setProductName(product.getProductName());
            dto.setBrokerName(broker.getName());

            result.add(dto);
        }

        return result;

    }
}
