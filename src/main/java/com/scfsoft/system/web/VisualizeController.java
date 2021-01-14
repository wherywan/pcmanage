package com.scfsoft.system.web;

import com.scfsoft.sdk.visualize.service.VisualizeService;
import com.scfsoft.system.web.SystemVisualizeController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author James HE
 */
@RestController
public class VisualizeController extends SystemVisualizeController {

    public VisualizeController(VisualizeService visualizeService) {
        super(visualizeService);
    }

}
