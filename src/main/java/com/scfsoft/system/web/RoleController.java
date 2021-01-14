package com.scfsoft.system.web;

import com.scfsoft.system.service.RoleService;
import com.scfsoft.system.web.SystemRoleController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author James HE
 */
@RestController
public class RoleController extends SystemRoleController {

    public RoleController(RoleService roleService) {
        super(roleService);
    }

}
