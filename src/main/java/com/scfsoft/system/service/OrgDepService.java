package com.scfsoft.system.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.scfsoft.sdk.common.utils.BeanUtils;
import com.scfsoft.sdk.common.utils.TreeUtils;
import com.scfsoft.sdk.das.jpa.param.Searchable;
import com.scfsoft.sdk.das.utils.DynamicSearchUtils;
import com.scfsoft.system.api.dto.Organization;
import com.scfsoft.system.dao.jpa.SysOrgDAO;
import com.scfsoft.system.entity.SysOrg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sunfx
 * @date 2020/5/15
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class OrgDepService {

    private final SysOrgDAO sysOrgDao;

    public Map<String, String> getAllDep() {
        Map<String, String> resultMap = Maps.newHashMap();

        List<SysOrg> list = sysOrgDao.findAll();

        list.stream().forEach(dep -> {
            resultMap.put(dep.getId(), dep.getOrgNameShort());
        });

        return resultMap;
    }

}
