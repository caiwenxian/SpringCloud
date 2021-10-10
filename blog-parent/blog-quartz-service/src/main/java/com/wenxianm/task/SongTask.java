package com.wenxianm.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName SongTask
 * @Author cwx
 * @Date 2021/9/26 11:16
 **/
@Component
@Slf4j
@JobHandler("SongTask")
public class SongTask extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("执行SongTask");
        XxlJobLogger.log("执行SongTask");
        return null;
    }
}
