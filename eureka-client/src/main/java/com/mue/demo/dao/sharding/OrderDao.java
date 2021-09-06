package com.mue.demo.dao.sharding;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mue.demo.model.sharding.OrderEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @ClassName OrderDao
 * @Description TODO
 * @Author cwx
 * @Date 2021/8/25 17:44
 * @Version 1.0
 **/
@Component
public interface OrderDao extends BaseMapper<OrderEntity> {

}
