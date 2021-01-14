package com.scfsoft.system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scfsoft.sdk.common.api.dto.StandardPagination;
import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.system.dao.mybatis.FillFormMapper;
import com.scfsoft.system.dto.Attachment;
import com.scfsoft.system.entity.jpa.ReportFill;
import com.scfsoft.system.utils.ExcelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sunfx
 * @date 2020/6/16
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ReportFillService {

    private final FillFormMapper fillFormMapper;

    private final LocalAttachService localAttachService;

    public List<ReportFill> selectFillFormList(StandardPagination page) {
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<ReportFill> lists = fillFormMapper.fetchReportFillByPage(ProviderFactory.getCurrentSubject());
        PageInfo<ReportFill> pageInfo = new PageInfo<>(lists);
        page.setTotalPage(pageInfo.getPages());
        page.setTotalRecord((int) pageInfo.getTotal());

        return lists;
    }

    public void stuffDownload(HttpServletResponse response, HttpServletRequest request, String batchId, String attachId) throws Exception{
        Attachment attachInfo = localAttachService.getAttachInfo(batchId, attachId);
        if (attachInfo == null) {
            throw new RuntimeException("下载文档时出现异常！");
        }
        String fileName = new String(attachInfo.getFileName());
        fileName = URLEncoder.encode(fileName,"utf-8");
        response.setContentType("application/octet-stream;charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        byte[] b = new byte[1024 * 1024];
        int i;
        try (InputStream is = localAttachService.getAttach(batchId, batchId);
             ServletOutputStream out = response.getOutputStream()) {
            while ((i = is.read(b)) > 0) {
                out.write(b, 0, i);
            }
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("下载文档时发生异常，请联系系统管理员！");
        }
    }

    public void resultDownload(HttpServletResponse response, String reportId) {
        // 根据ID 查询字段列表
        List<String> fieldList = fillFormMapper.fetchReportDetail(reportId);
        StringBuffer allField = new StringBuffer();
        if (fieldList != null && fieldList.size() > 0) {
            for (String field : fieldList) {
                allField.append(field).append(",");
            }

            allField.setLength(allField.length() - 1);
            String[] split = allField.toString().split(",");
            // 将Stream转化为List集合
            List<String> collect = Arrays.stream(split).distinct().collect(Collectors.toList());
            // 集合转化为数组
            String[] title = collect.toArray(new String[collect.size()]);

            // 生成结果文件
            ExcelUtils.createExcel(response, title);
        } else {
            try {
                response.setContentType("application/json");
                response.setHeader("errcode", "1001");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveFillFile(Attachment attachment, File file) throws Exception {
        InputStream in = new FileInputStream(file);
        List<String[]> list = ExcelUtils.getFileData(in);

        if (list != null && list.size() > 0) {
            // 只取第一行表头信息
            String[] fieldList = list.get(0);
            String detail = StringUtil.join(fieldList, ",");
            ReportFill fill = new ReportFill();
            fill.setId(attachment.getFillId());
            fill.setReportDetail(detail);
            fill.setStatus("1");
            fillFormMapper.updateFillResult(fill);
            fillFormMapper.updateDoneNum(attachment.getReportId());
        }

        return;
    }
}
