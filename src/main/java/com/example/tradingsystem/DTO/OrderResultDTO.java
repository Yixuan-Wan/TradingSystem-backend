package com.example.tradingsystem.DTO;

import com.example.tradingsystem.entity.OrderLimit;
import lombok.Data;

import java.util.List;

@Data
public class OrderResultDTO {
    private String res;
    private String detail;
    List<TransactionDTO> list;
    List<OrderLimit> orderList;
}
