package com.scfsoft.system.utils;

import com.scfsoft.sdk.common.utils.ThreadContextUtils;
import com.scfsoft.system.consts.SystemConsts;
import com.scfsoft.system.token.TokenPayload;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenPayloadUtils {

    public static String getOrgId() {
        TokenPayload payload = ThreadContextUtils.get(SystemConsts.THREAD_KEY_TOKEN_PAYLOAD, TokenPayload.class);
        return payload == null ? null : payload.getOrgId();
    }
}
