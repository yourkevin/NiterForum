package cn.niter.forum.enums;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2),
    SUB_COMMENT(3),
    TALK(11),
    TALK_COMMENT(12),
    TALK_SUB_COMMENT(13);
    private Integer type;


    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
