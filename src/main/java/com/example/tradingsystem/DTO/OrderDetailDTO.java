package com.example.tradingsystem.DTO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDetailDTO {

    private String orderId;

    private int orderType;
    private String orderSide;

    private int productId;
    private String productName;
    private String brokerId;
    private String brokerName;
    private int quantity;

    private Long price;
    private Long stopPrice;

    private int remainQuantity;

    private String traderId;
    private String traderName;
    private String traderCompany;
    private Date createTime;


    private Date updateTime;
}
