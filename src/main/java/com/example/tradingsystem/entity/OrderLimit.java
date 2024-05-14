package com.example.tradingsystem.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;


@Data
@TableName("`order_limit`")
public class OrderLimit implements Comparable<OrderLimit> {

    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;

    private int orderType;

    private int productId;

    private String brokerId;

    private int quantity;

    private Long price;

    private int remainQuantity;

    private String traderId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    @Override
    public int compareTo(OrderLimit o) {
        return (int) (this.price - o.price);
    }
}