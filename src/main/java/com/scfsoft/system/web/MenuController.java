package com.scfsoft.system.web;

import com.scfsoft.system.service.MenuService;
import com.scfsoft.system.web.SystemMenuController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author James HE
 */
@RestController
public class MenuController extends SystemMenuController {

    public MenuController(MenuService menuService) {
        super(menuService);
    }

}
