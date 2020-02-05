package cn.niter.forum.enums;

public enum ColumnEnum {
    ASK(1),
    SHARE(2),
    SUGGEST(3),
    TALK(4),
    NOTICE(5),
    NEWS(6),
    OTHER(9);
    private Integer type;
    public Integer getType() {
        return type;
    }

    ColumnEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (ColumnEnum columnEnum : ColumnEnum.values()) {
            if (columnEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

}
