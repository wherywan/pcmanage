package com.scfsoft.system.entity.jpa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scfsoft.system.dto.FillFormUserDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * @author sfx
 */
@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "REPORT_INFO")
public class ReportInfo extends BasePo<String> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "ID")
    @GenericGenerator(name = "ID", strategy = "uuid")
    @Column(name = "ID ", nullable = false)
    private String id;

    @Column(name = "REPORT_TITLE")
    private String reportTitle;

    @Column(name = "TOTAL_NUM")
    private int totalNum;

    @Column(name = "DONE_NUM")
    private int doneNum;

    @Column(name = "CREATE_NAME")
    private String createName;

    @Column(name = "FILE_URL")
    private String fileUrl;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Transient
    private List<FillFormUserDto> fillUsers;

    @Transient
    private String fillUser;

    @Transient
    private String completeStatus;

}
