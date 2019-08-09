package cc.ikevin.community.exception;

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
    CAN_NOT_LIKE(2021, "点赞异常"),
    CAN_NOT_LIKE_AGAIN(2022, "不要重复点赞哦"),
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

