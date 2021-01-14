package com.scfsoft.system.web;

import com.scfsoft.system.service.CacheService;
import com.scfsoft.system.web.SystemCacheController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author James HE
 */
@RestController
public class CacheController extends SystemCacheController {

    public CacheController(CacheService cacheService) {
        super(cacheService);
    }

}
