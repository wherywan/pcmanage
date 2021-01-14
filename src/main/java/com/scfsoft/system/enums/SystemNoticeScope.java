package com.scfsoft.system.enums;

import com.scfsoft.sdk.common.api.enums.DictEnum;
import lombok.Getter;

/**
 * 系统公告类型
 * @author guojingyu
 * @date 2020/03/11
 */
@Getter
public enum SystemNoticeScope implements DictEnum {

    /** 全体 */
    ALL("all","全体"),
    /** 指定部门 */
    ORG("org","指定部门"),
    /** 指定角色 */
    ROLE("role","指定角色"),
    /** 指定用户 */
    USER("user","指定用户"),
    /** 指定子应用 */
    APP("app","指定子应用")
    ;

    private String key;
    private String val;

    SystemNoticeScope(String key,String val) {
        this.key = key;
        this.val = val;
    }

    @Override
    public String getType() {
        return "SYSTEM_NOTICE_SCOPE";
    }
}
