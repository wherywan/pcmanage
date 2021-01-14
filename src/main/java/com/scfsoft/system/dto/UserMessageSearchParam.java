package com.scfsoft.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhuhao
 */
@Getter
@Setter
@ApiModel("站内信搜索")
public class UserMessageSearchParam implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private String sid;

    @ApiModelProperty(value = "消息类型")
    private String sendType;

    @ApiModelProperty(value = "搜索内容")
    private String searchContent;

}
