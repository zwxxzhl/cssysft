package com.atguigu.aclservice.enums;

public enum ExpParamsEnum {

    EXP("exp", "表达式 key"),
    PROP("prop", "参数名"),
    PRE_PROP("preProp", "参数名"),
    SUF_PROP("sufProp", "参数名"),
    VAL("val", "参数值"),
    PRE_VAL("preVal", "参数值"),
    SUF_VAL("sufVal", "参数值"),

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
    IN("in", "IN(val,val,val)"),
    NOT_IN("notIn", "NOT IN(val,val,val)"),
    OR("or", "(key=val or key=val)"),

    // 排序
    ASC("orderByAsc", "升序"),
    DESC("orderByDesc", "降序");


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
