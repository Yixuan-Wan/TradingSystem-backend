package com.example.tradingsystem.service;

import com.example.tradingsystem.DTO.MarketNum;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.Market;
import com.example.tradingsystem.entity.MarketBuy;
import com.example.tradingsystem.entity.MarketSell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketService {
    @Autowired
    MarketBuyService marketBuyService;

    @Autowired
    MarketSellService marketSellService;

    public ApiResponse getMarketDepth(int product_id,int startLevel){
        List<MarketBuy> marketBuys = marketBuyService.getMarketBuy(product_id,startLevel);
        List<MarketSell> marketSells = marketSellService.getMarketSell(product_id,startLevel);
        List<Market> markets = new ArrayList<>();

        for(int i=0; i<marketBuys.size(); i++){
            Market market = marketBuys.get(i);
            market.setLevelBuy(i+1+startLevel);
            markets.add(market);
        }

        for(int i=0; i<marketSells.size(); i++){
            Market market = marketSells.get(i);
            market.setLevelSell(i+1+startLevel);
            markets.add(market);
        }

        return ApiResponse.success(markets);
    }

    public ApiResponse getMarketNum(int product_id){
        MarketNum marketNum = new MarketNum();
        marketNum.setBuyNum(marketBuyService.getBuyMarketNum(product_id));
        marketNum.setSellNum(marketSellService.getSellMarketNum(product_id));
        return ApiResponse.success(marketNum);
    }
}
