package com.scfsoft.system.enums;

import com.scfsoft.sdk.common.api.enums.DictEnum;
import lombok.Getter;

/**
 * 数据字典 - 消息类型
 * @author zhuhao
 */
@Getter
public enum MessageParam implements DictEnum {

    /** 全部 */
    all("全部"),
    /** 机构 */
    org("机构"),
    /** 角色 */
    role("角色");

    private String val;

    MessageParam(String val) {
        this.val = val;
    }

    @Override
    public String getType() {
        return "MessageParam";
    }

    @Override
    public String getKey() {
        return this.name();
    }

}
