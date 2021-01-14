package com.scfsoft.system.web;

import com.alibaba.fastjson.JSON;
import com.scfsoft.system.dto.Attachment;
import com.scfsoft.system.service.LocalAttachService;
import com.scfsoft.sdk.common.api.dto.StandardAttach;
import com.scfsoft.sdk.common.api.dto.StandardResponse;
import com.scfsoft.sdk.common.api.service.AttachService;
import com.scfsoft.sdk.common.dto.Response;
import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.sdk.web.controller.BaseExceptionHandledController;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * 附件控制器基类
 * @author James HE
 */
@Getter
@Setter
@Slf4j
public abstract class AttachController<I extends Attachment> extends BaseExceptionHandledController {

    protected abstract LocalAttachService getAttachService();

    @RequestMapping("/search")
    @ResponseBody
    public StandardResponse search(@RequestBody I attachInfo) {
        LocalAttachService attachService = getAttachService();
        List<StandardAttach> attachInfos = attachService.getBatchInfo(attachInfo.getBatchId());
        return Response.newInstance(attachInfos);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public StandardResponse delete(@RequestBody I attachInfo) {
        LocalAttachService attachService = getAttachService();
        attachService.dropAttach(attachInfo.getBatchId(), attachInfo.getAttachId());
        return Response.newInstance();
    }

    @RequestMapping(value = "/upload", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String upload(I attachInfo, @RequestParam("attach") CommonsMultipartFile file) throws Exception {
        LocalAttachService attachService = getAttachService();

        Response response = Response.newInstance();

        // 1. 构造附件元数据
        Attachment attachment = new Attachment();
        attachment.setBatchId(attachInfo.getBatchId());
        attachment.setAttachId(!StringUtils.isEmpty(attachInfo.getAttachId()) ? attachInfo.getAttachId() : UUID.randomUUID().toString().replaceAll("-", ""));
        attachment.setFileName(file.getOriginalFilename());
        attachment.setUserId(ProviderFactory.getCurrentSubject());
        attachment.setFileSize(file.getSize());

        // 2. 校验文件
        String validationResult = getAttachService().validate(attachment);
        if (!StringUtils.isEmpty(validationResult)) {
            response.setFlag(-1);
            response.setMsg(validationResult);
            return JSON.toJSONString(response);
        }

        // 3. 保存附件
        DiskFileItem diskFileItem = (DiskFileItem) file.getFileItem();
        File attachFile = diskFileItem.getStoreLocation();
        if (attachment.getFileSize() != 0) {
            attachService.saveAttach(attachInfo.getBatchId(), attachment, attachFile);
        } else {
            attachService.saveAttach(attachInfo.getBatchId(), attachment, null);
        }

        response.setData(attachment);
        return JSON.toJSONString(response);
    }

    @RequestMapping("/download")
    public void download(@RequestParam String batchId, @RequestParam String attachId, HttpServletResponse response) throws UnsupportedEncodingException {
        LocalAttachService attachService = getAttachService();

        Attachment attachment = attachService.getAttachInfo(batchId, attachId);
        // 附件不存在处理
        if (attachment == null) {
            log.error("附件不存在，batchId={}，attachId={}", batchId, attachId);
            throw new RuntimeException("文件未找到!");
        }

        String fileName = new String(attachment.getFileName().getBytes("gbk"), "ISO-8859-1");

        response.setContentType("application/octet-stream;charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

        byte[] b = new byte[1024 * 1024];
        int i;
        try (InputStream is = attachService.getAttach(batchId, attachId);
             ServletOutputStream out = response.getOutputStream()) {
            while ((i = is.read(b)) > 0) {
                out.write(b, 0, i);
            }

            out.flush();
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException("文件下载失败!");
        }
    }

}
