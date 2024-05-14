package com.example.tradingsystem.entity;

import lombok.Data;

@Data
public class Market {
    private int productId;
    private Integer levelBuy;
    private Integer buyVol;
    private Long price;
    private Integer sellVol;
    private Integer levelSell;
}
