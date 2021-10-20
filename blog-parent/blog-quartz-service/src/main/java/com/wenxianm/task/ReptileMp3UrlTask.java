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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName ReptileMp3UrlTask
 * @Author cwx
 * @Date 2021/10/14 15:11
 **/
@Component
@Slf4j
@JobHandler("ReptileMp3UrlTask")
public class ReptileMp3UrlTask extends IJobHandler {

    @Autowired
    private SongApi songApi;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("执行ReptileMp3UrlTask");
        XxlJobLogger.log("开始执行ReptileMp3UrlTask");
        if (!StringUtils.isEmpty(param)) {
            String[] split = param.split(",");
            List<Long> list = Arrays.asList(split).stream().map(v -> Long.parseLong(v)).collect(Collectors.toList());
            songApi.reptileMp3Url(list);
            return SUCCESS;
        }
        songApi.reptileMp3Url(Lists.newArrayList());
        XxlJobLogger.log("结束执行ReptileMp3UrlTask");
        return SUCCESS;
    }
}
