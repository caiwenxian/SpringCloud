package com.mue.demo.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @ClassName MyDBPreciseShardingAlgorithm
 * @Description 自定义标准分表算法实现
 * @Author cwx
 * @Date 2021/9/6 10:32
 * @Version 1.0
 **/
@Component
public class MyTablePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        //collection就是表名称集合
        //preciseShardingValue为分片键的值
        for (String tableName : collection) {
            String value = (preciseShardingValue.getValue() % collection.size()) + "";
            if (tableName.endsWith(value)) {
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }
}
