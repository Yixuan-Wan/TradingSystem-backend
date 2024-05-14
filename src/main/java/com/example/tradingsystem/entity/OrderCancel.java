package com.example.tradingsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("order_cancel")
public class OrderCancel {
    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;
    private String deleteOrderId;
    private String brokerId;
    private int quantity;
    private Integer isSuccess;
    private String traderId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
