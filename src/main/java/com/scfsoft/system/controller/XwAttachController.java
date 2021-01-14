package com.scfsoft.system.controller;


import com.scfsoft.system.dto.Attachment;
import com.scfsoft.system.service.LocalAttachService;
import com.scfsoft.system.web.AttachController;
import com.scfsoft.sdk.common.api.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attach")
public class XwAttachController extends AttachController<Attachment> {

    @Autowired
    LocalAttachService attachService;

    @Override
    protected LocalAttachService getAttachService() {
        return attachService;
    }
}
