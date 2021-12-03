package com.atguigu.aclservice.enums;

public enum UserFormStatusEnum {

    NOT_REPORT("1", "未汇报"),
    REPORT_STAGE("2", "汇报中"),
    REPORT_COMPLETE("3", "汇报完成");

    private String code;
    private String name;

    UserFormStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
