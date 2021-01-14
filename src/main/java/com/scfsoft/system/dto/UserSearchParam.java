package com.scfsoft.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunfengxin
 */
@Getter
@Setter
@ApiModel("用户搜索请求")
public class UserSearchParam implements Serializable {

    @ApiModelProperty(value = "机构ID")
    private String orgId;

    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @ApiModelProperty(value = "模糊查询文本（用户名）")
    private String search;

    private String excludeUser;

    private List<String> orgIds;
    private List<String> depIds;

}
