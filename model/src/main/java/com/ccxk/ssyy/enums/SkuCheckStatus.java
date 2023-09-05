package com.ccxk.ssyy.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SkuCheckStatus {
    NOTCHECK(0, "未审核"),
    CHECKOK(1, "审核通过");

    @EnumValue
    private Integer code;
    private String comment;

    SkuCheckStatus(Integer code, String comment) {
        this.code = code;
        this.comment = comment;
    }
}
