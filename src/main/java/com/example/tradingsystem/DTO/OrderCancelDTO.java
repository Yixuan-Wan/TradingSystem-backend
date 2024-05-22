package com.example.tradingsystem.DTO;

import lombok.Data;

@Data
public class OrderCancelDTO {
    private String deleteOrderId;
//    private String brokerId;
    private int quantity;
}
