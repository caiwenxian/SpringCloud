package com.wenxianm.task;

import com.wenxianm.api.SongApi;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 根据名称reptile歌曲
 * @ClassName SongTask
 * @Author cwx
 * @Date 2021/9/26 11:16
 **/
@Component
@Slf4j
@JobHandler("reptileSongTask")
public class ReptileSongTask extends IJobHandler {

    @Autowired
    private SongApi songApi;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("执行reptileSongTask");
        XxlJobLogger.log("执行reptileSongTask");
        if (StringUtils.isEmpty(param)) {
            return SUCCESS;
        }
        songApi.reptileSong(param);
        log.info("结束执行reptileSongTask");
        XxlJobLogger.log("结束执行reptileSongTask");
        return SUCCESS;
    }
}
