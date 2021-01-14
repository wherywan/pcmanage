package com.scfsoft.system.web;

import com.scfsoft.system.schedule.service.ScheduleService;
import com.scfsoft.system.web.SystemScheduleController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author James HE
 */
@RestController
public class ScheduleController extends SystemScheduleController {

    public ScheduleController(ScheduleService scheduleService) {
        super(scheduleService);
    }

}
