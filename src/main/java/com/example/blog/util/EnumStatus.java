package com.example.blog.util;

public enum EnumStatus {

    VAILD(1, "有效"),
    INVAILD(0, "未生效"),
    FAILD(-1, "失败");

    private final Integer code;
    private final String name;

    EnumStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
