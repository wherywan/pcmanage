package com.scfsoft.system.entity.jpa;

import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.sdk.das.jpa.po.Po;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;

/**
 * @author James HE
 */
@Getter
@Setter
@MappedSuperclass
public class BasePo<K extends Serializable> extends Po<K> {

    @ApiModelProperty(value = "创建时间")
    @Column(name = "CREATED_TS", updatable = false)
    private Date createTime;

    @ApiModelProperty(value = "创建人SID", example = ProviderFactory.ANONYMOUS_SID)
    @Column(name = "CREATED_BY", updatable = false)
    private String createUser;

    @ApiModelProperty(value = "修改时间")
    @Column(name = "LAST_UPD_TS")
    private Date updateTime;

    @ApiModelProperty(value = "修改人SID", example = ProviderFactory.ANONYMOUS_SID)
    @Column(name = "LAST_UPD_BY")
    private String updateUser;

    @PrePersist
    protected void beforeInsert() {
        this.createTime = new Date();
        this.updateTime = new Date();
        if (StringUtils.isEmpty(createUser)) {
            this.createUser = ProviderFactory.getCurrentSubject();
            this.updateUser = this.createUser;
        }
    }

    @PreUpdate
    protected void beforeUpdate() {
        this.updateTime = new Date();
        if (StringUtils.isEmpty(updateUser)) {
            this.updateUser =  ProviderFactory.getCurrentSubject();
        }
    }

}
