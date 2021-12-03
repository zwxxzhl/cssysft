package com.atguigu.aclservice.enums;

public enum FormStatusEnum {

    DISTRIBUTE("1", "分发"),
    REPORTING("2", "汇报中"),
    REPORT_COMPLETE("3", "汇报完成"),
    INVALID("4", "作废");

    private String code;
    private String name;

    FormStatusEnum(String code, String name) {
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
