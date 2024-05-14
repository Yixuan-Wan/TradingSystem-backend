package com.example.tradingsystem.controller;

import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.MarketBuy;
import com.example.tradingsystem.service.MarketBuyService;
import com.example.tradingsystem.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market")
@CrossOrigin
public class MarketController {
    @Autowired
    MarketService marketService;
    @GetMapping("/{productId}")
    public ApiResponse getMarket(@PathVariable int productId, @RequestParam(required = false) int startLevel) {

        return marketService.getMarketDepth(productId,startLevel);
    }

    @GetMapping("/num")
    public ApiResponse getMarketNum(@RequestParam int productId){
        return marketService.getMarketNum(productId);
    }
}
