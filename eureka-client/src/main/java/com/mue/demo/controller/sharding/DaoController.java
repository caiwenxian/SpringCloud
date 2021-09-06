package com.mue.demo.controller.sharding;

import com.mue.demo.model.sharding.OrderEntity;
import com.mue.demo.service.sharding.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName DaoController
 * @Description TODO
 * @Author cwx
 * @Date 2021/8/25 17:49
 * @Version 1.0
 **/
@RestController
@RequestMapping("/order")
@Slf4j
public class DaoController {

    @Autowired
    IOrderService orderService;

    @PostMapping("/save")
    public String save(Integer userId) {
        OrderEntity entity = new OrderEntity();
        entity.setUserId(userId);
        orderService.saveOne(entity);
        return "success";
    }

    @GetMapping("/list")
    public List<OrderEntity> list() {
        return orderService.listAll();
    }

    @GetMapping("/listByOrderId")
    public List<OrderEntity> listByOrderId(Long orderId) {
        return orderService.listByOrderId(orderId);
    }
}
