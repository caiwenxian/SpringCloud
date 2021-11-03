package com.wenxianm.model.param;

import com.wenxianm.model.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MqMessageParam
 * @Author cwx
 * @Date 2021/10/28 10:51
 **/
@Data
public class MqMessageParam extends Page implements Serializable {

    /**
     * 消息id
     */
    private Long id;

    /**
     * mq工具id
     */
    private String mqId;

    /**
     * mq类型
     */
    private Integer type;

    /**
     * mq状态
     */
    private Integer status;

}
