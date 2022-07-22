package com.example.blog.vo;

import org.springframework.beans.BeanUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;


public class BaseVo {
    public <T> void copyIgnoreNullPropertiesTo(T baseModel) {
        BeanUtil.copyProperties(this, baseModel, true, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
    }

    public <T> void copyIgnoreNullProperties(T baseModel) {
        BeanUtil.copyProperties(baseModel, this, true, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
    }

    public <T> void copyPropertiesTo(T baseModel) {
        BeanUtils.copyProperties(this, baseModel);
    }

    public <T> void copyProperties(T baseModel) {
        BeanUtils.copyProperties(baseModel, this);
    }
}
