package com.scfsoft.system.enums;

import com.scfsoft.sdk.common.api.enums.ErrorCodeEnum;

/**
 * 通用工具异常信息
 * @author James HE
 */
public enum CommonErrors implements ErrorCodeEnum {

      CLASS_NOT_FOUND
    , FILE_NOT_FOUND
    , IS_FILE
    , IS_DIRECTORY
    , FILE_ALREADY_EXIST
    , DIRECTORY_ALREADY_EXIST
    , CANNOT_COPY_FILE
    , CANNOT_CREATE_DIRECTORY
    , CANNOT_CREATE_FILE
    , CANNOT_DELETE_TARGET

    /* 泛型数组下标越界 */
    ,PARAM_TYPE_INDEX_OUT_OF_BOUNDRY

    /* 无法找到或访问类 */
    ,CANNOT_LOCATE_OR_ACCESS_CLASS

    /* 无法转换成带泛型参数的类型 */
    ,CANNOT_CONVERT_TO_PARAMETERED_TYPE

    /* 无法访问 */
    ,CANNOT_ACCESS

    /* 无法实例化 */
    ,CANNOT_INSTANTIATION
    ;

    @Override
    public String getBundle() {
        return "errormsg-commons";
    }

}
