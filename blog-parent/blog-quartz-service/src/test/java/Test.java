import com.google.common.collect.Lists;
import com.wenxianm.BlogQuartzServiceApplication;
import com.wenxianm.api.SongApi;
import lombok.extern.slf4j.Slf4j;
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
@SpringBootTest(classes = BlogQuartzServiceApplication.class)
public class Test {

    @Autowired
    private SongApi songApi;

    @org.junit.Test
    public void test1() {
        songApi.reptileMp3Url(Lists.newArrayList());
    }

}
