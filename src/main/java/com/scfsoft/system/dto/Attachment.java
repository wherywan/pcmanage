package com.scfsoft.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfsoft.sdk.common.api.dto.StandardAttach;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 附件元数据
 * @author James HE
 */
@Getter
@Setter
public class Attachment extends StandardAttach {

    private String batchId;

    private String attachId;

    private String attachName;

    private String fileName;

    private String fileExtName;

    private String fileType;

    private String comment;

    private Long fileSize;

    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss" , timezone = "GMT+8")
    private Date uploadTime;

    private String userId;

    private String userName;

    private String templateName;

    private String templateUrl;

    private String fillId;

    private String reportId;

    private String transType;

}
