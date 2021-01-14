package com.scfsoft.system.service;

import com.scfsoft.sdk.common.dto.Response;
import com.scfsoft.system.api.dto.Organization;
import com.scfsoft.system.service.OrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lukejia
 * @date 2020/3/11 14:58
 */
@RequiredArgsConstructor
@Service
public class SyncOrgService {
    private final PortalServiceInterface portalService;

    private final OrgService orgService;

    /**
     * 同步机构
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void syncOrg() {
        Response<List<Organization>> response = portalService.getOrgTree();
        List<Organization> list = response.getData();
        orgService.saveOrgTree(list);
    }
}
