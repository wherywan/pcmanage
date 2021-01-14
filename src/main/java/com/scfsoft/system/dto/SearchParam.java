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
@ApiModel("搜索请求")
public class SearchParam implements Serializable {

    @ApiModelProperty(value = "机构ID")
    private String orgId;

    @ApiModelProperty(value = "统计日期")
    private String statDate;

    @ApiModelProperty(value = "产品类型")
    private String ccy;

}
