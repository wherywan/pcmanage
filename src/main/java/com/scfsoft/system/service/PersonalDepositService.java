package com.scfsoft.system.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.scfsoft.sdk.common.api.dto.StandardPagination;
import com.scfsoft.system.dao.mybatis.PersonalDepositMapper;
import com.scfsoft.system.dto.SearchParam;
import com.scfsoft.system.entity.jpa.PersonalDeposit;
import com.scfsoft.system.utils.ExcelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author sunfx
 * @date 2020/6/19
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PersonalDepositService {

    private final SaveFileDataService dataService;

    private final PersonalDepositMapper depositMapper;

    private final SysCommonService commonService;

    @Async
    public Future<String> handleFileData(File file, String fileName) {
        Future<String> future = null;
        try {
            InputStream in = new FileInputStream(file);
            List<String[]> dataList = ExcelUtils.getFileData(in);

            // 解析数据并保存数据库
            dataService.saveDepositData(dataList);
            future = new AsyncResult<String>(fileName + "处理成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            future = new AsyncResult<String>(fileName + "处理异常");
        } finally {
            return future;
        }
    }

    public List<PersonalDeposit> selectDepositList(StandardPagination page, SearchParam searchParam) {
        PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        List<PersonalDeposit> lists = Lists.newArrayList();
        if (searchParam.getOrgId().equals("320000000")) {
            lists = depositMapper.fetchSumDatasByPages(searchParam);
        } else {
            lists = depositMapper.fetchDatasByPages(searchParam);
        }
        PageInfo<PersonalDeposit> pageInfo = new PageInfo<>(lists);
        page.setTotalPage(pageInfo.getPages());
        page.setTotalRecord((int) pageInfo.getTotal());

        return lists;
    }

    public void createExcel(HttpServletResponse response, String statDate, String tempFileName, String orgId) {
        try {
            // 获取模板文件
            Resource resource = new ClassPathResource("excleTemplate/" + tempFileName);
            // 生成文件名、标题名
            String[] ymd = statDate.split("-");
            String titleName = tempFileName.replace("YYYY", ymd[0]).replace("MM", String.valueOf(Integer.parseInt(ymd[1]))).replace("DD", String.valueOf(Integer.parseInt(ymd[2])));
            titleName = titleName.substring(0, titleName.indexOf("."));
            // 创建Excel的工作书册 Workbook,对应到一个excel文档
            XSSFWorkbook workbook = new XSSFWorkbook(resource.getFile());
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow headRow = sheet.getRow(0);
            XSSFCellStyle headStyle = headRow.getCell(0).getCellStyle();
            XSSFCell rowHeadCell = headRow.createCell(0);
            rowHeadCell.setCellValue(titleName);
            rowHeadCell.setCellStyle(headStyle);

            // 获取列数
            int cellNum = sheet.getRow(3).getLastCellNum();

            // 创建内容行
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            // 自动换行
            // cellStyle.setWrapText(true);

            // 获取业务数据填充EXCEL
            Map<Integer, Map<String, PersonalDeposit>> resultMap = getBusiData(statDate, orgId);
            int rowNum = resultMap.get(1) != null ? resultMap.get(1).size() : 0;
            if (rowNum > 0) {
                Map<String, PersonalDeposit> total = resultMap.get(1);
                Map<String, PersonalDeposit> foreign = resultMap.get(2);
                Map<String, PersonalDeposit> struct = resultMap.get(3);
                int j = 4;
                for (String org : total.keySet()) {
                    PersonalDeposit deposit1 = total.get(org);
                    Field fields1[] = deposit1.getClass().getDeclaredFields();
                    Field.setAccessible(fields1, true);
                    // 取行名
                    String orgName = "";
                    if (StringUtils.isBlank(deposit1.getOrgName())) {
                        orgName = commonService.getOrgName(deposit1.getOrgId());
                    }

                    PersonalDeposit deposit2 = foreign.get(org);
                    Field fields2[] = deposit2.getClass().getDeclaredFields();
                    Field.setAccessible(fields2, true);

                    PersonalDeposit deposit3 = struct.get(org);
                    Field fields3[] = deposit3.getClass().getDeclaredFields();
                    Field.setAccessible(fields3, true);
                    XSSFRow contentRow = sheet.createRow(j);
                    for (int k = 0; k < cellNum; k++) {
                        XSSFCell cell = contentRow.createCell(k);
                        if (k < 16) {
                            if (k == 0) {
                                if (StringUtils.isNotBlank(orgName)) {
                                    cell.setCellValue(orgName);
                                } else {
                                    cell.setCellValue((String) fields1[k + 1].get(deposit1));
                                }
                                cell.setCellStyle(cellStyle);
                            } else {
                                DecimalFormat df = new DecimalFormat("#,##0.00");
                                if (fields1[k+1].get(deposit1) != null) {
                                    cell.setCellValue(df.format(fields1[k+1].get(deposit1)));
                                } else {
                                    cell.setCellValue(0.00);
                                }
                            }
                        } else if (k >= 16 && k < 30) {
                            DecimalFormat df = new DecimalFormat("#,##0.00");
                            if (fields3[k-14].get(deposit3) != null) {
                                cell.setCellValue(df.format(fields3[k-14].get(deposit3)));
                            } else {
                                cell.setCellValue(0.00);
                            }
                        } else {
                            DecimalFormat df = new DecimalFormat("#,##0.00");
                            if (fields2[k-28].get(deposit2) != null) {
                                cell.setCellValue(df.format(fields2[k-28].get(deposit2)));
                            } else {
                                cell.setCellValue(0.00);
                            }
                        }
                    }
                    j++;
                }
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                workbook.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(titleName + ".xlsx", "UTF-8"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
                bos.flush();
            } catch (final IOException e) {
                throw e;
            } finally {
                if (os != null) {
                    os.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<Integer, Map<String, PersonalDeposit>> getBusiData(String statDate, String orgId) {
        Map<Integer, Map<String, PersonalDeposit>> map = Maps.newHashMap();

        // 获取三种产品类型的统计信息
        for (int i = 1; i < 4; i++) {
            SearchParam searchParam = new SearchParam();
            searchParam.setStatDate(statDate);
            searchParam.setCcy(String.valueOf(i));
            searchParam.setOrgId(orgId);
            List<PersonalDeposit> lists = Lists.newArrayList();
            if (orgId.equals("320000000")) {
                lists = depositMapper.fetchSumDatasByPages(searchParam);
            } else {
                lists = depositMapper.fetchDatasByPages(searchParam);
            }
            Map<String, PersonalDeposit> resultMap = lists.stream().collect(Collectors.toMap(PersonalDeposit::getOrgId, a -> a,(k1, k2) -> k2));
            map.put(i, resultMap);
        }

        return map;
    }

}
