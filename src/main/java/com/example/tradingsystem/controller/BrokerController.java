package com.example.tradingsystem.controller;

import com.example.tradingsystem.common.ApiResponse;
import com.example.tradingsystem.service.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/broker")
@CrossOrigin
public class BrokerController {
    @Autowired
    BrokerService brokerService;
    @GetMapping("/all")
    ApiResponse getBrokerList(){
        return brokerService.getBrokerList();
    }
}
