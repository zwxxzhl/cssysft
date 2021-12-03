package com.atguigu.aclservice.enums;

public enum TimeStatusEnum {

    NORMAL("1", "正常"),
    TIMEOUT("2", "超时");

    private String code;
    private String name;

    TimeStatusEnum(String code, String name) {
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
