package com.wenxianm.model;

/**
 * @ClassName ServerCode
 * @Author cwx
 * @Date 2021/9/24 18:01
 **/
public enum ServerCode {

    SUCCESS(100, "success"),

    SUCCESS_CODE(0, "success"),

    WARNING(1,"warning"),

    ERROR(-1, "系统繁忙，请稍后再试"),

    SERVER_EXCEPTION_NULLPOINTER(-300, "资源不存在"),

    UPLOAD_FILE_ERROR(-333, "文件上传失败"),

    INVALID_PARAMS_CONVERSION(-400, "请求参数非法"),

    DATA_NOT_FOUND(-404, "资源不存在"),

    PERMISSION_UNLOGIN(-401, "登录已经超时，请重新登录"),

    PERMISSION_DENIED(-402, "您无该资源权限"),

    PERMISSION_MENU_DENIED(-403, "菜单权限非法"),

    HTTP_MESSAGE_NOT_READABLE(-410, "HTTP消息不可读"),

    REQUEST_METHOD_NOT_SUPPORTED(-405, "不支持的HTTP请求方法"),

    DECODE_ERROR(-601, "解析异常"),

    DUPLICATE_KEY(-701, "操作过快, 请稍后再试"),

    CLIENT_EXCEPTION(-801, "服务器升级维护中，请稍后再试"),

    SERVER_UPGRADE(-900, "系统升级中,该功能请稍后再试"),

    USER_NOT_EXISTS(-1001, "用户不存在, 请联系管理员"),

    INVALID_CREDENTIAL(-1002, "用户名或密码错误"),

    USER_STATE_LOCKED(-1003, "该用户已被禁用"),

    ILLEGAL_ARGUMENT_ERROR(-10003, "参数非法"),

    USER_NOT_AUTHORIZE(-10005, "用户未授权"),

    WRONG_PARAM(-19999, "错误参数"),

    SERVER_UNKNOWN_ERROR(-99999, "系统繁忙，请稍后再试"),

    PARAM_LENGTH_LIMIT(-4016, "参数超出长度限制");
    private int code;

    private String desc;

    ServerCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ServerCode getInstance(int code) {
        for (ServerCode entity : ServerCode.values()) {
            if (entity.getCode() == code) {
                return entity;
            }
        }
        return null;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
