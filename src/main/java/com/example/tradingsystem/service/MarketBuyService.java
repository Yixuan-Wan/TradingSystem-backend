package com.example.tradingsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tradingsystem.entity.MarketBuy;

import com.example.tradingsystem.entity.Order;
import com.example.tradingsystem.mapper.MarketBuyMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketBuyService {
    @Autowired
    MarketBuyMapper marketBuyMapper;


    public void addOrderToMarket(Order order){
        if(order.getRemainQuantity()==0){
            return;
        }
        MarketBuy marketBuy = new MarketBuy();
        marketBuy.setProductId(order.getProductId());
        marketBuy.setPrice(order.getPrice());
        marketBuy.setBuyVol(order.getRemainQuantity());

        QueryWrapper<MarketBuy> wrapper = new QueryWrapper<>();
        wrapper.select("buy_vol")
                .eq("product_id",order.getProductId())
                .eq("price",order.getPrice());
        MarketBuy mb = marketBuyMapper.selectOne(wrapper);
        if(mb!=null){
            marketBuy.setBuyVol(order.getRemainQuantity() + mb.getBuyVol());
            marketBuyMapper.delete(wrapper);
        }
        marketBuyMapper.insert(marketBuy);

    }



    public void deleteFromBuyMarket(int product_id, Long price, int delete_vol){
        QueryWrapper<MarketBuy> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",product_id)
                .eq("price",price);
        MarketBuy mb = marketBuyMapper.selectOne(wrapper);

        MarketBuy marketBuy = new MarketBuy();
        marketBuy.setProductId(product_id);
        marketBuy.setPrice(price);

        if(mb!=null){
            int remain_vol = mb.getBuyVol() - delete_vol;
            marketBuy.setBuyVol(remain_vol);
            marketBuyMapper.delete(wrapper);
            if(remain_vol!=0){
                marketBuyMapper.insert(marketBuy);
            }
        }
    }

}