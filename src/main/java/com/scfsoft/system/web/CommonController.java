package com.scfsoft.system.web;

import com.scfsoft.sdk.common.config.SdkInfoConfig;
import com.scfsoft.system.service.DictService;
import com.scfsoft.system.service.ParamService;
import com.scfsoft.system.web.SystemCommonController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author James HE
 */
@RestController
public class CommonController extends SystemCommonController {

    public CommonController(DictService dictService, ParamService paramService, SdkInfoConfig sdkInfoConfig) {
        super(dictService, paramService);
    }

}
