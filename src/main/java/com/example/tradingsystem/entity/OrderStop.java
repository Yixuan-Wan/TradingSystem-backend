package com.example.tradingsystem.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("`order_stop`")
public class OrderStop implements Comparable<OrderStop> {

    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;

    private int orderType;

    private int productId;

    private String brokerId;

    private int quantity;

    private Long stopPrice;

    private int remainQuantity;

    private String traderId;

    private int isFinished;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    @Override
    public int compareTo(OrderStop o) {
        return (int) (this.stopPrice - this.stopPrice);
    }
}