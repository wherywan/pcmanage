package com.scfsoft.system.web;

import com.scfsoft.system.service.ParamService;
import com.scfsoft.system.web.SystemParamController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author James HE
 */
@RestController
public class ParamController extends SystemParamController {

    public ParamController(ParamService paramService) {
        super(paramService);
    }

}
