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
public class FillFormUserDto {

    private String fillFormId;
    private String fillFormName;
    private boolean viewAuth;

}
