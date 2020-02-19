package cn.niter.forum.enums;

public enum NewsColumnEnum {
    NEWS_COLUMN_HULIANWANG(1,"5572a109b3cdc86cf39001e3","互联网"),
    NEWS_COLUMN_KEJI(2,"5572a10ab3cdc86cf39001f4","科技"),
    NEWS_COLUMN_DIANNAO(3,"5572a10bb3cdc86cf39001f6","电脑"),
    NEWS_COLUMN_SHUMA(4,"5572a10bb3cdc86cf39001f5","数码"),
    NEWS_COLUMN_KEPU(5,"572a10bb3cdc86cf39001f7","科普"),
    NEWS_COLUMN_TIYU(6,"5572a109b3cdc86cf39001e6","体育"),
    NEWS_COLUMN_YULE(7,"5572a108b3cdc86cf39001d5","娱乐"),
    NEWS_COLUMN_GUONEI(8,"5572a108b3cdc86cf39001cd","国内"),
    ;
    private Integer code;
    private String strId;
    private String name;


    public String getStrId() {
        return strId;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Integer strIdToCode(String strId) {
        for (NewsColumnEnum value : NewsColumnEnum.values()) {
            if(strId.equals(value.getStrId())) return value.getCode();
        }
        return 0;
    }

    public static String codeToName(Integer code) {
        for (NewsColumnEnum value : NewsColumnEnum.values()) {
            if(code==value.getCode()) return value.getName();
        }
        return "其它";
    }

    NewsColumnEnum(Integer code,String strId,String name){
        this.code = code;
        this.strId = strId;
        this.name = name;
    }
}
