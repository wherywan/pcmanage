package com.scfsoft.system.enums;

import com.scfsoft.sdk.common.api.enums.ErrorCodeEnum;

/**
 * @author James HE
 */
public enum PortalError implements ErrorCodeEnum {

    /** 用户名或密码不正确 */
    YPWUASS00000;

    @Override
    public String getBundle() {
        return "portal-errors";
    }
}
