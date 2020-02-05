package cn.niter.forum.enums;

public enum LikeTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;


    public Integer getType() {
        return type;
    }

    LikeTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (LikeTypeEnum likeTypeEnum : LikeTypeEnum.values()) {
            if (likeTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
