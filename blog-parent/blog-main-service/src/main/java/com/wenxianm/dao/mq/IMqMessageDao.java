package com.wenxianm.dao.mq;

import com.wenxianm.model.entity.Artist;
import com.wenxianm.model.entity.MqMessage;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * mq消息
 * @ClassName IArtistDao
 * @Author cwx
 * @Date 2021/10/20 10:40
 **/
@Repository
public interface IMqMessageDao extends Mapper<MqMessage> {
}
