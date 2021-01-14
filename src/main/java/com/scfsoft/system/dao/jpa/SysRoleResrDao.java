package com.scfsoft.system.dao.jpa;

import com.scfsoft.system.entity.jpa.SysRoleResr;
import com.scfsoft.sdk.das.jpa.dao.JpaDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SysRoleResrDao extends JpaDao<SysRoleResr, String> {

    @Query("select r.resrId from SysRoleResr r where r.roleId = :roleId")
    List<SysRoleResr> queryByRoleId(@Param("roleId") String roleId);
}
