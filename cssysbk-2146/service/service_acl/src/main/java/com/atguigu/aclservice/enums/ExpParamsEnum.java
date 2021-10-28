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
    IN("in", "IN(val,val,val)"),
    NOT_IN("notIn", "NOT IN(val,val,val)"),
    OR_SAME("orSame", "同名字段(key=val or key=val)"),
    OR_DIFF("orDiff", "不同字段(key1=val or key2=val)"),

    // 示例：eq__code / between__pre_code / orSame__pre_code
    JOIN("__", "表达式前置连接符: eq__"),
    MID_JOIN("_", "范围 或 OR_SAME 表达式中间连接符"),
    PRE("pre", "范围 或 OR_SAME 表达式前缀"),
    SUF("suf", "范围 或 OR_SAME 表达式后缀"),

    // orDiff 示例：qrDiff__code#Name_code / qrDiff__code#Name_name
    OR_JOIN("#", "OR_DIFF 表达式两字段间连接符"),

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
