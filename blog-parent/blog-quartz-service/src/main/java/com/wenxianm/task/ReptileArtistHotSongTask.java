package com.wenxianm.task;

import com.google.common.collect.Lists;
import com.wenxianm.api.SongApi;
import com.wenxianm.model.Constants;
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
 * 歌手热门歌曲
 * @ClassName ReptileArtistHotSongTask
 * @Author cwx
 * @Date 2021/10/14 15:11
 **/
@Component
@Slf4j
@JobHandler("reptileArtistHotSongTask")
public class ReptileArtistHotSongTask extends IJobHandler {

    @Autowired
    private SongApi songApi;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("执行ReptileArtistHotSongTask");
        XxlJobLogger.log("开始执行ReptileArtistHotSongTask");
        songApi.reptileArtistHotSongs(Constants.ZERO_L);
        XxlJobLogger.log("结束执行ReptileArtistHotSongTask");
        return SUCCESS;
    }
}
