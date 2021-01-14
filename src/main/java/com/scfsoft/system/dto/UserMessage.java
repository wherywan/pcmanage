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
@ApiModel("消息")
public class UserMessage implements Serializable {

    @ApiModelProperty(value = "系统短消息ID")
    private String messageId;

    @ApiModelProperty(value = "发送人", example = "lisi")
    private String senderId;

    @ApiModelProperty(value = "接收人", example = "zhangsan")
    private String receiver;

    @ApiModelProperty(value = "消息类型", example = "emprisk")
    private String messageType;

    @ApiModelProperty(value = "消息标题", example = "title")
    private String title;

    @ApiModelProperty(value = "消息内容", example = "hello")
    private String content;

    @ApiModelProperty(value = "是否已读", example = "N")
    private String isRead;

    @ApiModelProperty(value = "回复消息id")
    private String replyId;


}
