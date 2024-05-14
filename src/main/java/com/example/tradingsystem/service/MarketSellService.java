package com.example.tradingsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.MarketBuy;
import com.example.tradingsystem.entity.MarketSell;
import com.example.tradingsystem.entity.OrderLimit;
import com.example.tradingsystem.mapper.MarketSellMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketSellService {
    @Autowired
    MarketSellMapper marketSellMapper;
    public void addOrderToMarket(OrderLimit order){
        if(order.getRemainQuantity()==0){
            return;
        }
        MarketSell marketSell = new MarketSell();
        marketSell.setProductId(order.getProductId());
        marketSell.setPrice(order.getPrice());
        marketSell.setSellVol(order.getRemainQuantity());

        QueryWrapper<MarketSell> wrapper = new QueryWrapper<>();
        wrapper.select("sell_vol")
                .eq("product_id",order.getProductId())
                .eq("price",order.getPrice());
        MarketSell mb = marketSellMapper.selectOne(wrapper);
        if(mb!=null){
            marketSell.setSellVol(order.getRemainQuantity() + mb.getSellVol());
            marketSellMapper.delete(wrapper);
        }
        marketSellMapper.insert(marketSell);
    }

    public void deleteFromSellMarket(int product_id, Long price, int delete_vol){
        QueryWrapper<MarketSell> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id",product_id)
                .eq("price",price);
        MarketSell mb = marketSellMapper.selectOne(wrapper);

        MarketSell marketSell = new MarketSell();
        marketSell.setProductId(product_id);
        marketSell.setPrice(price);

        if(mb!=null){
            int remain_vol = mb.getSellVol() - delete_vol;
            marketSell.setSellVol(remain_vol);
            marketSellMapper.delete(wrapper);
            if(remain_vol!=0){
                marketSellMapper.insert(marketSell);
            }
        }

    }

    public List<MarketSell> getMarketSell(int productId,int startLevel) {
        QueryWrapper<MarketSell> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId).orderByAsc("price").last("LIMIT " + startLevel + ", 3");
        return marketSellMapper.selectList(queryWrapper);
    }

    public Long getSellMarketNum(int productId){
        QueryWrapper<MarketSell> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        return marketSellMapper.selectCount(queryWrapper);
    }
}
