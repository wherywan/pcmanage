package com.scfsoft.system.dao.mybatis;

import com.scfsoft.system.dto.SysUserDto;
import com.scfsoft.system.dto.UserSearchParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author lukejia
 * @date 2020/3/12 14:27
 */
@Mapper
public interface UserMapper {
    /**
     * 获取user的非角色
     * @param roleId
     * @param sids
     * @return
     */
    List<String> getUserByRoles(@Param("roleId") String roleId, @Param("sids") List<String> sids);

    /**
     * 获取user的非机构
     * @param orgId
     * @param sids
     * @return
     */
    List<String> getUserByOrgs(@Param("orgId") String orgId, @Param("sids") List<String> sids);

    List<String> getUserEmpId();

    List<SysUserDto> getSysAllUsers(UserSearchParam request);

    List<SysUserDto> fetchUsersByPage(UserSearchParam param);

    SysUserDto getUserInfo(@Param("id") String id);

    void saveUserInfo(SysUserDto userDto);
    void updateUser(SysUserDto userDto);
}
