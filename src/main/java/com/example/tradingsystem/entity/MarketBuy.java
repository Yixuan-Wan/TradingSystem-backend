package com.example.tradingsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("market_buy")
public class MarketBuy {

    private int productId;
    private Integer levelBuy;
    private Integer buyVol;
    private Long price;
    private Integer sellVol;
    private Integer levelSell;
}
