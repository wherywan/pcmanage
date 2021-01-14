package com.scfsoft.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author guojingyu
 */
@Getter
@Setter
@ApiModel("系统公告")
public class SystemNoticeSearchParam implements Serializable {

    @ApiModelProperty(value = "公告类型：info,warning等")
    private String noticeType;

    @ApiModelProperty(value = "关键字（对标题与内容的搜索）")
    private String key;

    @ApiModelProperty(value = "发布时间")
    private String publishTime;

}
