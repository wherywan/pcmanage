package com.scfsoft.system.common;/*
package com.scfsoft.system.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


public class ExportExcelForEmp {

    public static void createExcel(HttpServletResponse response,
                                   List<?> list, String fileName) {
        try {
            String[] title = {"项目名称", "期次", "状态", "序号", "二级分行（省分行条线）", "客户编号", "客户身份证号", "客户账号", "发生日期", "交易时间", "借方发生额", "贷方发生额", "交易备注", "交易对方户名", "疑点描述", "疑点来源", "所属分行", "部门", "岗位", "员工姓名", "员工编号", "核查人", "核查结果", "核查过程详细描述"};
            // 创建Excel的工作书册 Workbook,对应到一个excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFCellStyle style2 = workbook.createCellStyle();
            // 生成一个字体
            HSSFFont font = workbook.createFont();
            HSSFFont font2 = workbook.createFont();
            // 字体增粗
            font.setFontHeightInPoints((short) 11);
            font.setBold(true);
            style.setFont(font);

            font2.setFontHeightInPoints((short) 15);
            font2.setBold(true);
            style2.setFont(font2);
            style2.setAlignment(HorizontalAlignment.CENTER);
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            HSSFSheet sheet = workbook.createSheet("sheet1");
            // 创建Excel的sheet的一行 (表头)
            HSSFRow rowHead = sheet.createRow((short) 0);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
            HSSFCell rowHeadCell = rowHead.createCell(0);
            rowHeadCell.setCellValue("可疑行为信息");
            rowHeadCell.setCellStyle(style2);

            HSSFRow row = sheet.createRow(1);
            // 表头内容填充
            for (int i = 0; i < title.length; i++) {
                // 设置excel每列宽度
                sheet.setColumnWidth(i, 5000);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(title[i]);
                cell.setCellStyle(style);
            }
            // 创建内容行
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);// 自动换行
            for (int j = 0; j < list.size(); j++) {
                HSSFRow contentRow = sheet.createRow(j + 2);
                for (int k = 0; k < title.length; k++) {
                    HSSFCell cell = contentRow.createCell(k);
                    switch (k) {
                        case 0:
                            if (list.get(j).getProjectName() != null) {// 项目名称
                                cell.setCellValue(list.get(j).getProjectName());
                            }
                            break;
                        case 1:
                            if (list.get(j).getPeriod() != null) {//期次
                                cell.setCellValue(list.get(j).getPeriod());
                            }
                            break;
                        case 2:
                            if (list.get(j).getDubiousActionStatus() != null) {//状态
                                cell.setCellValue(list.get(j).getDubiousActionStatus());
                            }
                            break;
                        case 3:
                            if (list.get(j).getSn() != null) {//序号
                                cell.setCellValue(list.get(j).getSn());
                            }
                            break;
                        case 4:
                            if (list.get(j).getSecondLevelBranch() != null) {//二级分行（省分行条线）
                                cell.setCellValue(list.get(j).getSecondLevelBranch());
                            }
                            break;
                        case 5:
                            if (list.get(j).getCNo() != null) {//客户编号
                                cell.setCellValue(list.get(j).getCNo());
                            }
                            break;
                        case 6:
                            if (list.get(j).getCCertno() != null) {//客户身份证
                                cell.setCellValue(list.get(j).getCCertno());
                            }
                            break;
                        case 7:
                            if (list.get(j).getCId() != null) {//客户账号
                                cell.setCellValue(list.get(j).getCId());
                            }
                            break;
                        case 8:
                            if (list.get(j).getHappenDate() != null) {//发生日期
                                cell.setCellValue(list.get(j).getHappenDate());
                            }
                            break;
                        case 9:
                            if (list.get(j).getTransTime() != null) {//交易时间
                                cell.setCellValue(list.get(j).getTransTime());
                            }
                            break;
                        case 10:
                            if (list.get(j).getDebitAmt() != null) {//借方发生额
                                cell.setCellValue(list.get(j).getDebitAmt());
                            }
                            break;
                        case 11:
                            if (list.get(j).getCreditAmt() != null) {//贷方发生额
                                cell.setCellValue(list.get(j).getCreditAmt());
                            }
                            break;
                        case 12:
                            if (list.get(j).getTransRemark() != null) {//交易备注
                                cell.setCellValue(list.get(j).getTransRemark());
                            }
                            break;
                        case 13:
                            if (list.get(j).getTransOtherUsername() != null) {//交易对方户名
                                cell.setCellValue(list.get(j).getTransOtherUsername());
                            }
                            break;
                        case 14:
                            if (list.get(j).getDoubtDes() != null) {//疑点描述
                                cell.setCellValue(list.get(j).getDoubtDes());
                            }
                            break;
                        case 15:
                            if (list.get(j).getDoubtSrc() != null) {//疑点来源
                                cell.setCellValue(list.get(j).getDoubtSrc());
                            }
                            break;
                        case 16:
                            if (list.get(j).getToBranch() != null) {//所属分行
                                cell.setCellValue(list.get(j).getToBranch());
                            }
                            break;
                        case 17:
                            if (list.get(j).getDep() != null) {//部门
                                cell.setCellValue(list.get(j).getDep());
                            }
                            break;
                        case 18:
                            if (list.get(j).getPost() != null) {//岗位
                                cell.setCellValue(list.get(j).getPost());
                            }
                            break;
                        case 19:
                            if (list.get(j).getEmpName() != null) {//员工姓名
                                cell.setCellValue(list.get(j).getEmpName());
                            }
                            break;
                        case 20:
                            if (list.get(j).getEmpNo() != null) {//员工编号
                                cell.setCellValue(list.get(j).getEmpNo());
                            }
                            break;
                        case 21:
                            if (list.get(j).getChecker() != null) {//核查人
                                cell.setCellValue(list.get(j).getEmpNo());
                            }
                            break;
                        case 22:
                            if (list.get(j).getCheckResult() != null) {//核查结果
                                cell.setCellValue(list.get(j).getEmpNo());
                            }
                            break;
                        case 23:
                            if (list.get(j).getCheckDes() != null) {//核查过程详细描述
                                cell.setCellValue(list.get(j).getCheckDes());
                            }
                            break;
                        default:
                            break;
                    }
                    cell.setCellStyle(cellStyle);
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
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes("gb2312"), "iso-8859-1"));
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
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

*/
