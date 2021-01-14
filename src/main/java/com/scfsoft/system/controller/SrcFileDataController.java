package com.scfsoft.system.controller;

import com.alibaba.fastjson.JSON;
import com.scfsoft.sdk.common.dto.Response;
import com.scfsoft.system.dto.Attachment;
import com.scfsoft.system.service.PersonalDepositService;
import com.scfsoft.system.service.SameBuiPersonalDepositService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author sfx
 */
@Slf4j
@RestController
@RequestMapping("/source-files")
public class SrcFileDataController {
    @Autowired
    PersonalDepositService pdService;

    @Autowired
    SameBuiPersonalDepositService spdService;

    @RequestMapping(value = "/upload", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String uploadFiles(@RequestParam("transCode") String transCode, @RequestParam("files") List<CommonsMultipartFile> files) throws Exception{
        Response response = Response.newInstance();
        System.out.println(transCode);
        log.info(transCode);
        // 获取交易码 0001：个人存款 0002：基金 0003：同业 0004：保险
        if (StringUtils.isBlank(transCode)) {
            throw new RuntimeException("交易类型为空！");
        }
        // 存放所有的线程，用于获取结果
        List<Future<String>> lstFuture = new ArrayList<>();

        long start = System.currentTimeMillis();
        for (CommonsMultipartFile file : files) {
            DiskFileItem diskFileItem = (DiskFileItem) file.getFileItem();
            File attachFile = diskFileItem.getStoreLocation();
            if (transCode.equals("0001")) {
                Future<String> result = pdService.handleFileData(attachFile, diskFileItem.getName());
                lstFuture.add(result);
            }else if(transCode.equals("0004")){
                Future<String> result = spdService.handleFileData(attachFile, diskFileItem.getName());
                lstFuture.add(result);
            }
        }

        // 获取值.get是阻塞式，等待当前线程完成才返回值
        for (Future<String> future : lstFuture) {
            try {
                log.debug("{}", future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        log.debug("源文件全部处理结束，用时{}秒", (System.currentTimeMillis() - start)/1000);

        return JSON.toJSONString(response);
    }

}
