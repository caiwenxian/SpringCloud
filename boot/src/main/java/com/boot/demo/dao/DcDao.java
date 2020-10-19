package com.boot.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @description：
 * @author: caiwx
 * @createDate ： 2020年10月15日 16:42:00
 */
@Mapper
public interface DcDao {

    /**
     * 功能描述：测试
     *
     * @author caiwx
     * @return int
     * @date 2020/10/15
     */
    int selectDual();
}
