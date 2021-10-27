package com.atguigu.aclservice.enums;

public enum ExpParamsEnum {

    EQ("eq", "等于 ="),
    NE("ne", "不等于 <>"),
    GT("gt", "大于 >"),
    GE("ge", "大于等于 >="),
    LT("lt", "小于 <"),
    LE("le", "小于等于 <="),
    BETWEEN("between", "BETWEEN 值1 AND 值2"),
    NOT_BETWEEN("notBetween", "NOT BETWEEN 值1 AND 值2"),
    LIKE("like", "LIKE '%值%'"),
    NOT_LIKE("notLike", "NOT LIKE '%值%'"),
    LIKE_LEFT("likeLeft", "LIKE '%值'"),
    LIKE_RIGHT("likeRight", "LIKE '值%'"),

    JOIN("__", "表达式与prop连接符"),
    MID_JOIN("_", "范围表达式与prop中间连接符"),
    PRE("pre", "范围表达式前缀"),
    SUF("suf", "范围表达式后缀");

    private String code;
    private String msg;

    ExpParamsEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
