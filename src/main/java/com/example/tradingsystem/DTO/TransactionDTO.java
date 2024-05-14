package com.example.tradingsystem.DTO;

import com.example.tradingsystem.entity.Trader;
import com.example.tradingsystem.entity.Transaction;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionDTO {
    private String transactionId;
    private String brokerId;
    private String brokerName;

    private int productId;
    private String productName;

    private Date createTime;
    private Date updateTime;
    private Long price;
    private Integer quantity;

    private String trader1Id;
    private String trader1Name;
    private String trader1Company;
    private int trader1Side;
    private String side1;

    private String trader2Id;
    private String trader2Name;
    private String trader2Company;
    private int trader2Side;
    private String side2;
}
