package com.wenxianm.service.mq.impl;

import com.google.common.collect.Lists;
import com.wenxianm.dao.mq.IMqMessageDao;
import com.wenxianm.exception.BaseException;
import com.wenxianm.model.PageData;
import com.wenxianm.model.ServerCode;
import com.wenxianm.model.dto.MqMessageDto;
import com.wenxianm.model.entity.MqMessage;
import com.wenxianm.model.param.MqMessageParam;
import com.wenxianm.service.mq.IMqMessageService;
import com.wenxianm.utils.BeanUtil;
import com.wenxianm.utils.IDUtil;
import com.wenxianm.utils.PojoUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName MqMessageServiceImpl
 * @Author cwx
 * @Date 2021/10/28 11:00
 **/
@Service
@Slf4j
public class MqMessageServiceImpl implements IMqMessageService {

    @Autowired
    private IMqMessageDao mqMessageDao;

    @Override
    public void addOne(MqMessage message) {
        try {
            message.setId(IDUtil.random().toString());
            message.setCreateTime(new Date());
            message.setModifyTime(new Date());
            mqMessageDao.insertSelective(message);
        } catch (Exception e) {
            log.error("新增mq消息出错: ", e);
            throw new BaseException(ServerCode.ERROR, "新增mq消息出错");
        }
    }

    @Override
    public void updateOne(MqMessageDto mqMessageDto) {
        if (StringUtils.isEmpty(mqMessageDto.getMqId())) {
            log.warn("mq消息id不可为空");
            return;
        }
        try {
            Example example = new Example(MqMessage.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(PojoUtil.field(MqMessage::getMqId), mqMessageDto.getMqId());
            MqMessage message = BeanUtil.from(mqMessageDto, MqMessage.class);
            message.setModifyTime(new Date());
            mqMessageDao.updateByExampleSelective(message, example);
        } catch (Exception e) {
            log.error("更新mq消息出错: ", e);
            throw new BaseException(ServerCode.ERROR, "更新mq消息出错");
        }
    }

    @Override
    public MqMessageDto getOne(MqMessageParam param) {
        Example example = new Example(MqMessage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(PojoUtil.field(MqMessage::getMqId), param.getMqId());
        List<MqMessage> mqMessages = mqMessageDao.selectByExample(example);
        if (CollectionUtils.isEmpty(mqMessages)) {
            return null;
        }
        return BeanUtil.from(mqMessages.get(0), MqMessageDto.class);
    }

    @Override
    public List<MqMessageDto> list(MqMessageParam param) {
        Example example = new Example(MqMessage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(PojoUtil.field(MqMessage::getMqId), param.getMqId());
        List<MqMessage> mqMessages = mqMessageDao.selectByExample(example);
        if (CollectionUtils.isEmpty(mqMessages)) {
            return Lists.newArrayList();
        }
        return BeanUtil.fromList(mqMessages, MqMessageDto.class);
    }

    @Override
    public PageData<MqMessageDto> listPage(MqMessageParam param) {
        Example example = new Example(MqMessage.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(param.getMqId())) {
            criteria.andEqualTo(PojoUtil.field(MqMessage::getMqId), param.getMqId().trim());
        }
        if (Objects.nonNull(param.getType())) {
            criteria.andEqualTo(PojoUtil.field(MqMessage::getType), param.getType());
        }
        if (Objects.nonNull(param.getStatus())) {
            criteria.andEqualTo(PojoUtil.field(MqMessage::getStatus), param.getStatus());
        }
        int count = mqMessageDao.selectCountByExample(example);
        if (count == 0) {
            return PageData.emptyPage();
        }
        param.setTotalItem(count);
        example.setOrderByClause("send_time desc");
        RowBounds rowBounds = new RowBounds(param.getOffset(), param.getLimit());
        List<MqMessage> mqMessages = mqMessageDao.selectByExampleAndRowBounds(example, rowBounds);
        return new PageData(BeanUtil.fromList(mqMessages, MqMessageDto.class), param);
    }
}
