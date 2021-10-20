package com.wenxianm.exception;

import com.wenxianm.model.ServerCode;
import lombok.Data;

/**
 * 通用异常
 * @ClassName BaseException
 * @Author cwx
 * @Date 2021/10/12 18:04
 **/
@Data
public class BaseException extends RuntimeException{

    private static final long serialVersionUID = -7320959498334150097L;


    private String platform;

    /***
     * 错误编码
     */
    private ServerCode serverCode;

    /***
     * 异常包含的附件
     */
    private Object attachment;

    /**
     * 异常信息描述
     */
    private String desc;

    public BaseException(ServerCode serverCode) {
        super();
        this.serverCode = null != serverCode ? serverCode : ServerCode.SERVER_EXCEPTION_NULLPOINTER;
        this.desc = this.serverCode.getDesc();
    }

    public BaseException(ServerCode serverCode, String desc) {
        super(desc);
        this.serverCode = null != serverCode ? serverCode : ServerCode.SERVER_EXCEPTION_NULLPOINTER;
        this.desc = (null == desc || desc.length() == 0) ? this.serverCode.getDesc() : desc;
    }

    public BaseException(ServerCode serverCode, String desc, String platform) {
        super(desc);
        this.serverCode = null != serverCode ? serverCode : ServerCode.SERVER_EXCEPTION_NULLPOINTER;
        this.desc = (null == desc || desc.length() == 0) ? this.serverCode.getDesc() : desc;
        this.platform = platform;
    }
}