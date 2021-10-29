import com.wenxianm.BlogMainServiceApplication;
import com.wenxianm.model.enums.MQTagEnum;
import com.wenxianm.model.mq.TopSongMessage;
import com.wenxianm.mq.MqProducer;
import com.wenxianm.mq.MqProducerWithoutStream;
import com.wenxianm.utils.IDUtil;
import com.wenxianm.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName Test
 * @Author cwx
 * @Date 2021/10/18 16:29
 **/
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BlogMainServiceApplication.class)
public class MQTest {

    @Autowired
    private MqProducer mqProducer;

    @Autowired
    private MqProducerWithoutStream mqProducerWithoutStream;

    @org.junit.Test
    public void test1() {
        TopSongMessage topSongMessage = new TopSongMessage();
        topSongMessage.setId(IDUtil.random());
        mqProducer.sendNoSerialize(topSongMessage, MQTagEnum.BLOG_MAIN_REPTILE_TOP_SONG);
    }

    @Test
    public void test2() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("group1");
        defaultMQProducer.setNamesrvAddr("192.168.115.128:9876");
        defaultMQProducer.setSendMsgTimeout(6000);
        defaultMQProducer.start();
        Message message = new Message("topic_blog_main_song", "blog_test", "hello".getBytes());
        SendResult send = defaultMQProducer.send(message);
        log.info("send: {}", JsonUtil.objToStr(send));
        defaultMQProducer.shutdown();
    }

    @Test
    public void test3() {
        SendResult hello = mqProducerWithoutStream.output1().send("hello", MQTagEnum.BLOG_MAIN_REPTILE_TOP_SONG);
        log.info("hello: {}", JsonUtil.objToStr(hello));
    }

}
