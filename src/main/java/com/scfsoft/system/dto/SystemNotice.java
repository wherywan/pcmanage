package com.scfsoft.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author guojingyu
 */
@Getter
@Setter
@ApiModel("系统公告")
public class SystemNotice implements Serializable {

    @ApiModelProperty(value = "公告id")
    private String id;

    @ApiModelProperty(value = "公告类型：info,warning等")
    private String noticeType;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "公告内容（HTML富文本字符串）")
    private String content;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    @ApiModelProperty(value = "是否可见：Y-可见 N-不可见")
    private String isVisible;

    // todo 针对子应用

    @ApiModelProperty(value = "公告范围：all-全部可见 org-指定机构可见 role-指定角色可见  user-指定用户id")
    private String scope;

    @ApiModelProperty(value = "公告范围的具体内容：all-all org-可见机构id role-可见角色id user-对应用户id")
    private String targetId;

    @ApiModelProperty(value = "创建者")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTs;

    @ApiModelProperty(value = "修改者")
    private String lastUpdBy;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastUpdTs;

}
