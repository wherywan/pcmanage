package com.scfsoft.system.entity.jpa;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author sfx
 */
@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "REPORT_FILL")
public class ReportFill extends BasePo<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "ID")
    @GenericGenerator(name = "ID", strategy = "uuid")
    @Column(name = "ID ", nullable = false)
    private String id;

    @Column(name = "REPORT_ID")
    private String reportId;

    @Column(name = "FILL_FORM_ID")
    private String fillFormId;

    @Column(name = "FILL_FORM_NAME")
    private String fillFormName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "REPORT_DETAIL")
    private String reportDetail;

    @Column(name = "VIEW_AUTH")
    private String viewAuth;

    @Transient
    private String reportTitle;

    @Transient
    private String fileUrl;
}
