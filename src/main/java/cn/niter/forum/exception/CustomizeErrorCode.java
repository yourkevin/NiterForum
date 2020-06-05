package cn.niter.forum.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "问题已经找不到了，已被删除或压根就不存在？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论"),
    NO_LOGIN(2003, "当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004, "服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG(2005, "类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了，要不要换个试试？"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009, "消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL(2010, "图片上传失败"),
    CAN_NOT_EDIT_QUESTION(2020, "您无法编辑此问题"),
    CAN_NOT_LIKE(2021, "点赞/收藏异常"),
    CAN_NOT_LIKE_AGAIN(2022, "不要重复点赞哦"),
    CAN_NOT_LIKE_QUESTION_AGAIN(2023, "不要重复点赞哦"),
    SEND_MAIL_FAILED(2030, "验证码发送失败请重试!请确保您的邮箱正确！"),
    SUBMIT_MAIL_FAILED(2031, "邮箱绑定/更新失败,可能该号码已在其他账号上绑定，请重试！"),
    SEND_PHONE_FAILED(2032, "短信验证码发送失败，请重试或联系管理员！"),
    PHONE_CODE_ERROR(2033, "验证码错误或失效，请稍后再试！"),
    SUBMIT_PHONE_FAILED(2034, "手机号绑定/更新失败,可能该号码已在其他账号上绑定，请重试！"),
    USER_IS_EMPTY(2040, "该用户不存在或已被屏蔽！"),
    NEWS_NOT_FOUND(2050, "该条资讯已经找不到了，已被删除或压根就不存在？"),
    VALIDATE_ERROR(2060,"参数校验失败"),
    TOKEN_ERROR(2061,"令牌校验失败"),
    ;

    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }



    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}

