package com.scfsoft.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scfsoft.sdk.common.dto.TreeNode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sfx
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgDto extends TreeNode<OrgDto> {

    private String orgId;
    private String orgName;

}
