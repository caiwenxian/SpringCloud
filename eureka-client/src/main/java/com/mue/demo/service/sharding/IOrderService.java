package com.mue.demo.service.sharding;


import com.mue.demo.model.sharding.OrderEntity;

import java.util.List;

/**
 * @ClassName IOrderService
 * @Description TODO
 * @Author cwx
 * @Date 2021/8/25 17:46
 * @Version 1.0
 **/
public interface IOrderService extends IBaseService<OrderEntity>{

    /**
     * save
     * @param orderEntity
     */
    void saveOne(OrderEntity orderEntity);

    /**
     * listAll
     * @return
     */
    List<OrderEntity> listAll();

    /**
     * listAll
     * @return
     */
    List<OrderEntity> listByOrderId(Long orderId);
}
