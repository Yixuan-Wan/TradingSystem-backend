package com.example.tradingsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("trader")
public class Trader {
    @TableId(type = IdType.ASSIGN_ID)
    private String traderId;
    private String name;
    private String password;
    private String mail;
    private String company;
}

