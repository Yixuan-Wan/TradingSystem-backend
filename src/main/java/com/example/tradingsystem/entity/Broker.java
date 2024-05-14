package com.example.tradingsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("broker")
public class Broker {
    @TableId(type = IdType.ASSIGN_ID)
    private String brokerId;
    private String name;
    private String password;
    private String mail;
    private String company;
}
