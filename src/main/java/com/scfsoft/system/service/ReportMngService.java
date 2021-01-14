package com.scfsoft.system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.scfsoft.sdk.common.api.dto.StandardPagination;
import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.system.dao.jpa.ReportFillDAO;
import com.scfsoft.system.dao.jpa.ReportInfoDAO;
import com.scfsoft.system.dao.jpa.SysOrgDAO;
import com.scfsoft.system.dao.mybatis.ReportMapper;
import com.scfsoft.system.dto.FillFormUserDto;
import com.scfsoft.system.entity.SysOrg;
import com.scfsoft.system.entity.jpa.ReportFill;
import com.scfsoft.system.entity.jpa.ReportInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.SystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sunfx
 * @date 2020/6/16
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ReportMngService {

    private final SubscriberServiceSyncImpl subscriberService;

    private final ReportInfoDAO reportInfoDAO;

    private final ReportFillDAO reportFillDAO;

    private final ReportMapper reportMapper;

    public List<ReportInfo> selectReportList(StandardPagination page) {
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<ReportInfo> lists = reportMapper.fetchReportByPage(ProviderFactory.getCurrentSubject());
        PageInfo<ReportInfo> pageInfo = new PageInfo<>(lists);
        page.setTotalPage(pageInfo.getPages());
        page.setTotalRecord((int) pageInfo.getTotal());

        return lists;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveReportTask(ReportInfo reportInfo) {
        // 保存报表基本信息
        // 查询发布人姓名
        List<FillFormUserDto> fillUsers = reportInfo.getFillUsers();
        if (fillUsers != null && fillUsers.size() > 0) {
            String userId = ProviderFactory.getCurrentSubject();
            reportInfo.setCreateUser(userId);
            reportInfo.setUpdateUser(userId);
            reportInfo.setTotalNum(fillUsers.size());
            reportInfo.setCreateName(subscriberService.getUserInfo(userId));
            reportInfo = reportInfoDAO.save(reportInfo);

            ReportInfo finalReportInfo = reportInfo;
            List<ReportFill> list = fillUsers.stream().map(fillUser -> {
                ReportFill reportFill = new ReportFill();
                reportFill.setReportId(finalReportInfo.getId());
                reportFill.setFillFormId(fillUser.getFillFormId());
                reportFill.setFillFormName(fillUser.getFillFormName());
                reportFill.setStatus("0");
                reportFill.setViewAuth(fillUser.isViewAuth() ? "1" : "0");
                reportFill.setCreateUser(userId);
                reportFill.setUpdateUser(userId);
                return reportFill;
            }).collect(Collectors.toList());
            reportFillDAO.saveAll(list);
        } else {
            throw new RuntimeException("填表人不能为空！");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteReport(String id) {
        reportInfoDAO.deleteById(id);
        reportMapper.deleteByReportId(id);
    }

}
