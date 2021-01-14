package com.scfsoft.system.dao.mybatis;

import com.scfsoft.system.dto.UserOrg;
import com.scfsoft.system.entity.jpa.SysUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper {

    String getUserRole(@Param("userId") String userId);

    void saveBl(@Param("userId") String userId, @Param("businessLine") String businessLine);

    String getBlId(@Param("userId") String userId);

    List<SysUserEntity> getBl(@Param("userCode") String userCode);

    UserOrg selectOrgByUser(@Param("userId") String userId);

    void deleteRoleByUserId(@Param("sid") String userId);
}
