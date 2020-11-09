package com;

import com.service.JTAService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={TransactionAtomitosApplication.class})
public class TransactionAtomitosApplicationTests {

    public TransactionAtomitosApplicationTests() {}

    @Autowired
    JTAService jtaService;

    @Test
    public void testAtomikos() {
        jtaService.insert();
    }

}
