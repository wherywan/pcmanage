package com.scfsoft.system.web;

import com.scfsoft.system.service.DictService;
import com.scfsoft.system.web.SystemDictController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author James HE
 */
@RestController
public class DictController extends SystemDictController {

    public DictController(DictService dictService) {
        super(dictService);
    }

}
