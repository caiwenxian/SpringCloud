package com.mue.demo.service.sharding.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mue.demo.dao.sharding.OrderDao;
import com.mue.demo.model.sharding.OrderEntity;
import com.mue.demo.service.sharding.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName OrderServiceImpl
 * @Description TODO
 * @Author cwx
 * @Date 2021/8/25 17:46
 * @Version 1.0
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements IOrderService {

    @Autowired
    OrderDao orderDao;

    @Override
    public void saveOne(OrderEntity orderEntity) {
//        orderEntity.setOrderId(System.currentTimeMillis());
        this.save(orderEntity);
    }

    @Override
    public List<OrderEntity> listAll() {
        return this.list();
    }

    @Override
    public List<OrderEntity> listByOrderId(Long orderId) {
        return this.list(Wrappers.<OrderEntity>lambdaQuery().eq(OrderEntity::getOrderId, orderId));
    }
}
