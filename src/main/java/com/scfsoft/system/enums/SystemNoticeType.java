package com.scfsoft.system.enums;

import com.scfsoft.system.dto.NoticeType;
import com.scfsoft.sdk.common.api.enums.DictEnum;
import lombok.Getter;

/**
 * 系统公告类型
 * @author guojingyu
 * @date 2020/03/11
 */
@Getter
public enum SystemNoticeType implements DictEnum {

    /** 警告 */
    NOTICE_WARNING("warning",new NoticeType("警告","Orange")),
    /** 通知 */
    NOTICE_INFO("info",new NoticeType("通知","LightSlateGray")),
    /** 错误 */
    NOTICE_ERROR("error",new NoticeType("错误","Red"))
    ;

    private String key;
    private NoticeType val;

    SystemNoticeType(String key,NoticeType val) {
        this.key = key;
        this.val = val;
    }

    @Override
    public String getType() {
        return "SYSTEM_NOTICE_TYPE";
    }
}


