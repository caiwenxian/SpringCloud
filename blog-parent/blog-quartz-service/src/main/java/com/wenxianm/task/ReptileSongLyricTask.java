package com.wenxianm.task;

import com.google.common.collect.Lists;
import com.wenxianm.api.SongApi;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * reptile歌词
 * @ClassName ReptileMp3UrlTask
 * @Author cwx
 * @Date 2021/10/14 15:11
 **/
@Component
@Slf4j
@JobHandler("ReptileSongLyricTask")
public class ReptileSongLyricTask extends IJobHandler {

    @Autowired
    private SongApi songApi;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("执行ReptileSongLyricTask");
        XxlJobLogger.log("开始执行ReptileSongLyricTask");
        songApi.reptileLyric();
        XxlJobLogger.log("结束执行ReptileSongLyricTask");
        return SUCCESS;
    }
}
