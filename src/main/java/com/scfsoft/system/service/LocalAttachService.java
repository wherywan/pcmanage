package com.scfsoft.system.service;

import com.google.common.collect.Lists;
import com.scfsoft.sdk.common.api.dto.StandardAttach;
import com.scfsoft.sdk.common.api.service.AttachService;
import com.scfsoft.sdk.common.exception.ServiceRuntimeException;
import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.sdk.common.utils.FileUtils;
import com.scfsoft.system.api.dto.Subscriber;
import com.scfsoft.system.dto.Attachment;
import com.scfsoft.system.enums.CommonErrors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 附件存取服务（本地存储实现）
 * @author James HE
 */
@Getter
@Setter
@Slf4j
public class LocalAttachService implements AttachService {

    /** 文件存储根目录 */
    private String rootDir;

    /** 附件大小上限（默认 0 表示无上限） */
    private Long maxFileSize = 0L;

    public LocalAttachService() {
    }

    @PostConstruct
    private void initService() {
        if (!StringUtils.isEmpty(rootDir)) {
            FileUtils.mkdirs(new File(rootDir));
        }
    }

    public void saveAttaches(String batchId, Map<StandardAttach, File> attaches) {
        // TODO 暂不支持批量上传
    }

    public void saveAttach(String batchId, Attachment attach, File file) throws Exception {
        File attachDir = new File(rootDir + "/" + attach.getBatchId() + "/" + attach.getAttachId());
        if (attachDir.exists()) {
            FileUtils.rm(attachDir);
        }

        FileUtils.mkdirs(attachDir);

        File attachFile = new File(rootDir + "/" + attach.getBatchId() + "/" + attach.getAttachId() + "/" + attach.getUserId() + "_" + attach.getFileName());
        if (file == null) {
            FileUtils.touch(attachFile);
            return;
        }
        FileUtils.copy(file, attachFile, true);
    }

    public void saveAttachInputStream(String batchId, Attachment attach, InputStream inputStream) {
        File attachDir = new File(rootDir + "/" + attach.getBatchId() + "/" + attach.getAttachId());
        if (attachDir.exists()) {
            FileUtils.rm(attachDir);
        }

        FileUtils.mkdirs(attachDir);
        File attachFile = new File(rootDir + "/" + attach.getBatchId() + "/" + attach.getAttachId() + "/" + attach.getUserId() + "_" + attach.getFileName());
        FileUtils.touch(attachFile);
        FileUtils.copy(inputStream, attachFile);
    }

    public void dropBatch(String batchId) {
        File batchDir = new File(rootDir + "/" + batchId);
        FileUtils.rm(batchDir);
    }

    public void dropAttach(String batchId, String attachId) {
        File attachDir = new File(rootDir + "/" + batchId + "/" + attachId);
        FileUtils.rm(attachDir);
    }

    public List<StandardAttach> getBatchInfo(String batchId) {
        List<StandardAttach> result = Lists.newArrayList();

        File batchDir = new File(rootDir + "/" + batchId);
        if (!batchDir.exists()) {
            return result;
        }

        File[] attachDirs = batchDir.listFiles();
        for (File attachDir : attachDirs) {
            String attachId = attachDir.getName();

            if (attachDir.isDirectory()) {
                StandardAttach attachment = getAttachInfo(batchId, attachId);
                result.add(attachment);
            }
        }

        return result;
    }

    public Attachment getAttachInfo(String batchId, String attachId) {
        File attachDir = new File(rootDir + "/" + batchId + "/" + attachId);

        File[] attachFiles = attachDir.listFiles();

        if (attachFiles != null && attachFiles.length == 1) {
            Attachment attachment = new Attachment();
            attachment.setBatchId(batchId);
            attachment.setAttachId(attachId);

            for (File attachFile : attachFiles) {
                if (attachFile.isFile()) {
                    String attachFileName = attachFile.getName();

                    int sepPos = attachFileName.indexOf('_');
                    String userId = attachFileName.substring(0, sepPos);
                    String fileName = attachFileName.substring(sepPos + 1, attachFileName.length());

                    attachment.setFileName(fileName);
                    attachment.setUserId(userId);
                    attachment.setUserName(userId);

                    if (StringUtils.isNotEmpty(userId)) {
                        Subscriber subscriber = (Subscriber) ProviderFactory.getCurrentSubscriber();
                        String userName = subscriber.getUsername();
                        attachment.setUserName(userName);
                    }

                    attachment.setFileSize(attachFile.length());
                    attachment.setUploadTime(FileUtils.getLastModifiedTime(attachFile));
                }
            }

            return attachment;
        }

        return null;
    }

    public InputStream getAttach(String batchId, String attachId) {
        Attachment StandardAttach = getAttachInfo(batchId, attachId);
        File attachFile = new File(rootDir + "/" + batchId + "/" + attachId + "/" + StandardAttach.getUserId() + "_" + StandardAttach.getFileName());
        InputStream is = null;
        try {
            is = new FileInputStream(attachFile);
        } catch (FileNotFoundException e) {
            throw new ServiceRuntimeException(CommonErrors.FILE_NOT_FOUND);
        }
        return is;
    }

    public InputStream getPreview(String batchId, String attachId) {
        // TODO 图片压缩
        return null;
    }

    public void copyByBatch(String batchId, String newBatchId) {
        // TODO 流程制作时实现即可
    }

    public String validate(StandardAttach StandardAttach) {
        if (this.maxFileSize>0 && StandardAttach.getFileSize()>this.maxFileSize) {
            return "附件大小不得大于 " + this.getMaxFileSize();  // TODO 优化文件大小显示值（按单位）
        }
        return null;
    }

}
