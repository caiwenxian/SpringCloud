package com.service;

import com.dao.account.AccountMapper;
import com.dao.user.UserMapper;
import com.model.Account;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description：
 * @author: caiwx
 * @createDate ： 2020年11月03日 14:30:00
 */

@Service
public class JTAService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Transactional
    public void insert() {
        User user = new User();
        user.setName("name2");
        userMapper.insert(user);

        Account account = new Account();
        account.setUserId(user.getId());
        account.setMoney(20);
        accountMapper.insert(account);
    }


    static public class Test {
        static public void main(String[] args) {
            atomikos();
        }

        static void atomikos() {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-atomikos.xml");
            JTAService jtaService = applicationContext.getBean("jtaService", JTAService.class);
            jtaService.insert();
        }

    }
}
