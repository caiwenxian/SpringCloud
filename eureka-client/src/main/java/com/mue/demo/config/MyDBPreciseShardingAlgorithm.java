package com.mue.demo.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @ClassName MyDBPreciseShardingAlgorithm
 * @Description 自定义标准分库算法实现
 * @Author cwx
 * @Date 2021/9/6 10:32
 * @Version 1.0
 **/
@Component
public class MyDBPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
        //collection就是数据库名称集合
        //preciseShardingValue为分片键的值
        for (String databaseName : collection) {
            String value = (preciseShardingValue.getValue() % collection.size()) + "";
            if (databaseName.endsWith(value)) {
                return databaseName;
            }
        }
        throw new IllegalArgumentException();
    }
}
