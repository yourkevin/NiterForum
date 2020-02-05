package cn.niter.forum.enums;

public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "回复了您的问题"),
    REPLY_COMMENT(2, "回复了您的评论"),
    REPLY_SUB_COMMENT(21, "回复了您的子评论"),
    LIKE_QUESTION(3, "收藏了您的问题"),
    LIKE_COMMENT(4, "点赞了您的评论");
    private int type;
    private String name;


    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }

    public static String nameOfType(int type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getType() == type) {
                return notificationTypeEnum.getName();
            }
        }
        return "";
    }
}
