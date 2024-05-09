package com.example.tradingsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("transaction")
public class Transaction {
    @TableId(type = IdType.ASSIGN_ID)
    private String transactionId;

    private String brokerId;
    private int productId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    private Long price;
    private Integer quantity;

    private String trader1Id;
    private int trader1Side;

    private String trader2Id;
    private int trader2Side;

    private String order1Id;
    private String order2Id;

}
