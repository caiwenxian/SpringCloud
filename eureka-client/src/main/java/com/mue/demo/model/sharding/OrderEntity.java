package com.mue.demo.model.sharding;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OrderEntity
 * @Description TODO
 * @Author cwx
 * @Date 2021/8/25 17:42
 * @Version 1.0
 **/
@Data
@TableName("t_order")
public class OrderEntity extends Model<OrderEntity>{

    private Long orderId;

    private Integer userId;

    private Integer status = 1;
}
