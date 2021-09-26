package com.wenxianm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求返回包装类
 * @author caiwx
 * @date 2021/9/24 - 17:58
 **/
@Data
@AllArgsConstructor
public class JsonResponse implements Serializable {

    private static final long serialVersionUID = -5145207117592729188L;

    /**
     * 响应状态码
     */
    private int status;

    /**
     * msg
     */
    private String msg;

    /**
     * 真实数据
     */
    private Object data;

    /**
     * traceId
     */
    private String traceId;

    public JsonResponse(int status) {
        this.status = status;
    }
}
