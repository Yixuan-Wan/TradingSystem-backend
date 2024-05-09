package com.example.tradingsystem.DTO;

import com.example.tradingsystem.entity.Trader;
import com.example.tradingsystem.entity.Transaction;
import lombok.Data;

@Data
public class TransactionDTO {
    private Transaction transaction;
    private Trader trader1;
    private Trader trader2;
}
