package com.example.tradingsystem.controller;

import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("/get")
    public ApiResponse getAllTransactions(@RequestParam(required = false) String orderId, Integer pageNo, Integer pageSize){
        return transactionService.getAllTransactions(orderId,pageNo,pageSize);
    }
}
