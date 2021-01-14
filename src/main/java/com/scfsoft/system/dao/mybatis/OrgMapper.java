package com.scfsoft.system.dao.mybatis;

import com.scfsoft.system.dto.OrgDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sfx
 */
@Mapper
public interface OrgMapper {
    List<OrgDto> getOrgTree();
}
