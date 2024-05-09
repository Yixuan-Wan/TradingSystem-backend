package com.example.tradingsystem.controller;

import com.example.tradingsystem.DTO.LoginDTO;
import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.entity.Trader;
import com.example.tradingsystem.service.TraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trader")
public class TraderController {
    @Autowired
    TraderService traderService;
    @GetMapping("/")
    ApiResponse getAllTraders(){
        return traderService.getAllTraders();
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginDTO loginDTO) {
        return traderService.login(loginDTO.getMail(),loginDTO.getPassword());
    }
}
