package com.scfsoft.system.web;

import com.scfsoft.system.service.OperationLogService;
import com.scfsoft.system.web.SystemOperationLogController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author James HE
 */
@RestController
public class OperationLogController extends SystemOperationLogController {

    public OperationLogController(OperationLogService operationLogService) {
        super(operationLogService);
    }

}
