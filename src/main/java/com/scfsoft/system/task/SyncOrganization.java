package com.scfsoft.system.task;

import com.scfsoft.sdk.common.api.dto.StandardParamDefinition;
import com.scfsoft.system.consts.SystemConsts;
import com.scfsoft.system.service.SyncOrgService;
import com.scfsoft.system.schedule.api.BaseScheduledTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lukejia
 * @date 2020/3/11 16:12
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties(SystemConsts.SCHEDULE_CONFIG_COMMON_PREFIX + "sync-org")
public class SyncOrganization extends BaseScheduledTask {
    private final SyncOrgService syncOrgService;

    @Override
    public void execute(StringBuilder remarkBuilder) throws InterruptedException {
        log.debug("同步机构任务开始");
        syncOrgService.syncOrg();
        remarkBuilder.append(" - 完成同步机构");
        log.debug("同步机构任务结束");
    }

    @Override
    public String getDescription() {
        return "同步机构信息";
    }

    @Override
    public List<StandardParamDefinition> getConfig() {
        return null;
    }
}
