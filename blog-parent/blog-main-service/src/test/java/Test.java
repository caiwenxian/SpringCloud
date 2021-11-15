import com.wenxianm.model.Constants;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @ClassName Test
 * @Author cwx
 * @Date 2021/11/6 9:51
 **/
@Slf4j
public class Test {

    @org.junit.Test
    public void test() {
        String PROPERTY = "http://music.163.com/song/media/outer/url?id=%s.mp3";
        String ceshi = String.format(PROPERTY, "ceshi");
        log.info("ceshi:{}", ceshi);
    }



    @org.junit.Test
    public void test2() throws IOException {
        String url = "http://music.163.com/song/media/outer/url?id=1401740479.mp3";
        Document document = Jsoup.connect( url).get();
        log.info(document.toString());
    }
}
