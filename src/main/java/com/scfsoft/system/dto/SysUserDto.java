package com.scfsoft.system.dto;

import com.scfsoft.system.api.dto.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author sunfengxin
 */
@Getter
@Setter
public class SysUserDto {
    private String sid;
    private String username;
    private String orgId;
    private String depId;
    private String roles;
    private String userType;
    private String userStatus;
    private String mobile;
    private String userCode;
}
