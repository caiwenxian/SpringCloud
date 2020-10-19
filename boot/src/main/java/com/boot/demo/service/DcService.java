package com.boot.demo.service;

import com.boot.demo.dao.DcDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description：
 * @author: caiwx
 * @createDate ： 2020年10月15日 16:46:00
 */
@Service
public class DcService {

    @Autowired
    DcDao dcDao;

    public int dcService() {
        return dcDao.selectDual();
    }
}
