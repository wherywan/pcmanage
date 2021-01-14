package com.scfsoft.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuhao
 */
@Getter
@Setter
@ApiModel("消息")
public class Message implements Serializable {

    @ApiModelProperty(value = "消息id", example = "1223312312")
    private String id;

    @ApiModelProperty(value = "消息类型", example = "org")
    private String sendType;

    @ApiModelProperty(value = "消息对象", example = "1212")
    private String target;

    @ApiModelProperty(value = "消息标题", example = "标题")
    private String title;

    @ApiModelProperty(value = "消息内容", example = "内容")
    private String content;

    @ApiModelProperty(value = "有效期", example = "2020-02-06 09:40:28")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date validDate;


}
