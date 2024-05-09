package com.example.tradingsystem.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("`order`")
public class Order implements Comparable<Order> {

    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;

    private int orderType;

    private int productId;

    private String brokerId;

    private int quantity;

    private Long price;

    private int remainQuantity;

    private String traderId;

    private Integer orderAlgorithm;

    @Override
    public int compareTo(Order o) {
        return (int) (this.price - o.price);
    }
}