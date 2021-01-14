package com.scfsoft.system.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author sfx
 * @date 2020/5/9
 */
@Slf4j
public class ExcelUtils {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {
        numberFormat.setGroupingUsed(false);
    }

    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";
    /**
     * 获取xlsx文件内容某个sheet（从0开始）的内容，以二维数组形式返回
     *
     * @param inputStream xlsx文件的绝对路径
     * @return xlsx文件的文本内容
     */
    public static List<String[]> getArrayFromXLSX(InputStream inputStream, int bookIndex) {
        Workbook workbook = null;
        List<String[]> allContents = Lists.newArrayList();
        try {
            workbook = new XSSFWorkbook(inputStream);
            if (bookIndex >= workbook.getNumberOfSheets()) {
                log.error("getArrayFromXLSX error: bookIndex={} is to large! ", bookIndex);
                return null;
            }
            // 循环取sheet
            for (int sn = 0; sn < workbook.getNumberOfSheets(); sn++) {
                Sheet sheet = workbook.getSheetAt(sn);
                int rowNum = sheet.getLastRowNum() + 1;
                List<String[]> singleSheet = Lists.newArrayList();
                for (int rn = 0; rn < rowNum; rn++) {
                    Row row = sheet.getRow(rn);
                    String[] content = null;
                    if (row != null) {
                        content = new String[row.getLastCellNum()];
                        for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                            content[cn] = getXCellFormatValue(row.getCell(cn));
                        }
                        singleSheet.add(content);
                    }
                }
                allContents.addAll(singleSheet);
            }
        } catch (FileNotFoundException fe) {
            log.error("getArrayFromXLSX error: FileNotFoundException", fe);
        } catch (IOException ie) {
            log.error("getArrayFromXLSX error: IOException", ie);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("getArrayFromXLSX close Exception", e);
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error("getArrayFromXLSX close Exception", e);
                }
            }
        }
        return allContents;
    }

    public static List<String[]> getFileData(InputStream inputStream) {
        Workbook workbook = null;
        List<String[]> allContents = Lists.newArrayList();
        try {
            workbook = new XSSFWorkbook(inputStream);
            // 循环取sheet
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum() + 1;
            List<String[]> singleSheet = Lists.newArrayList();
            for (int rn = 0; rn < rowNum; rn++) {
                Row row = sheet.getRow(rn);
                String[] content = null;
                if (row != null) {
                    content = new String[row.getLastCellNum()];
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        content[cn] = getXCellValue(row.getCell(cn));
                    }
                    singleSheet.add(content);
                }
            }
            allContents.addAll(singleSheet);
        } catch (FileNotFoundException fe) {
            log.error("getArrayFromXLSX error: FileNotFoundException", fe);
        } catch (IOException ie) {
            log.error("getArrayFromXLSX error: IOException", ie);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("getArrayFromXLSX close Exception", e);
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error("getArrayFromXLSX close Exception", e);
                }
            }
        }
        return allContents;
    }

    public static void createExcel(HttpServletResponse response, String[] title) {
        try {
            // 创建Excel的工作书册 Workbook,对应到一个excel文档
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFCellStyle style = workbook.createCellStyle();
            // 生成一个字体
            XSSFFont font = workbook.createFont();
            // 字体增粗
            font.setFontHeightInPoints((short) 11);
            font.setBold(true);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            // 创建Excel的工作sheet,对应到一个excel文档的tab
            XSSFSheet sheet = workbook.createSheet("sheet1");
            XSSFRow row = sheet.createRow(0);
            // 表头内容填充
            for (int i = 0; i < title.length; i++) {
                // 设置excel每列宽度
                sheet.setColumnWidth(i, 5000);
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(title[i]);
                cell.setCellStyle(style);
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
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("报表字段集合" + ".xlsx", "UTF-8"));
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

    public static String getArrayFromWORD(String fileAbsolutePath) throws IOException {
        String buffer = "";
        WordExtractor ex = null;
        POIXMLTextExtractor extractor = null;
        try {
            if (fileAbsolutePath.endsWith(".doc")) {
                InputStream is = null;
                try {
                    is = new FileInputStream(new File(fileAbsolutePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ex = new WordExtractor(is);
                buffer = ex.getText();
            } else if (fileAbsolutePath.endsWith(".docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(fileAbsolutePath);
                try {
                    extractor = new XWPFWordExtractor(opcPackage);
                } catch (XmlException e) {
                    e.printStackTrace();
                } catch (OpenXML4JException e) {
                    e.printStackTrace();
                }
                buffer = extractor.getText();
            }
        } finally {
            if (ex != null) {
                ex.close();
            }
            if (extractor != null) {
                extractor.close();
            }
        }
        return buffer;
    }

    public static String getXCellFormatValue(Cell cell) {
        String cellValue = "";
        double d = 0.00;
        if (null != cell) {
            switch (cell.getCellType()) {
                case XSSFCell.CELL_TYPE_STRING:
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                case XSSFCell.CELL_TYPE_NUMERIC:
                    cellValue = getDateValue(cell.getCellStyle().getDataFormat(), cell.getCellStyle().getDataFormatString(), cell.getNumericCellValue());
                    break;
                case XSSFCell.CELL_TYPE_FORMULA:
                    d = cell.getNumericCellValue();
                    cellValue = numberFormat.format(d);
                    break;
                case XSSFCell.CELL_TYPE_BLANK:
                    cellValue = "";
                    break;
                case XSSFCell.CELL_TYPE_BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case XSSFCell.CELL_TYPE_ERROR:
                    cellValue = "ERROR";
                    break;
                default:
                    cellValue = " ";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    public static String getXCellValue(Cell cell) {
        String cellValue = "";
        double d = 0.00;
        if (null != cell) {
            switch (cell.getCellType()) {
                case XSSFCell.CELL_TYPE_STRING:
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                case XSSFCell.CELL_TYPE_NUMERIC:
                    DecimalFormat format = new DecimalFormat(".00");
                    cellValue = String.valueOf(format.format(cell.getNumericCellValue()));
                    break;
                case XSSFCell.CELL_TYPE_FORMULA:
                    d = cell.getNumericCellValue();
                    cellValue = numberFormat.format(d);
                    break;
                case XSSFCell.CELL_TYPE_BLANK:
                    cellValue = "";
                    break;
                case XSSFCell.CELL_TYPE_BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case XSSFCell.CELL_TYPE_ERROR:
                    cellValue = "ERROR";
                    break;
                default:
                    cellValue = " ";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    public static void main(String[] args) {
/*        List<String[]> rowArray = getArrayFromXLSX("D:\\tmp\\src\\testdb.xlsx", 0);
        for (String[] row : rowArray) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }*/
    }

    /**
     * 得到date单元格格式的值
     * @param dataFormat
     * @param dataFormatString
     * @param value
     * @return
     */
    public static String getDateValue(Short dataFormat, String dataFormatString, double value){
        if (!DateUtil.isValidExcelDate(value)){
            return numberFormat.format(value);
        }

        Date date = DateUtil.getJavaDate(value);
        if (date.getTime() < 0) {
            return numberFormat.format(value);
        }
        /**
         * 年月日时分秒
         */
        if (Constants.EXCEL_FORMAT_INDEX_DATE_NYRSFM_STRING.contains(dataFormatString)) {
            return Constants.COMMON_DATE_FORMAT.format(date);
        }
        /**
         * 年月日
         */
        if (Constants.EXCEL_FORMAT_INDEX_DATE_NYR_STRING.contains(dataFormatString)) {
            return Constants.COMMON_DATE_FORMAT_NYR.format(date);
        }
        /**
         * 年月
         */
        if (Constants.EXCEL_FORMAT_INDEX_DATE_NY_STRING.contains(dataFormatString) || Constants.EXCEL_FORMAT_INDEX_DATA_EXACT_NY.equals(dataFormat)) {
            return Constants.COMMON_DATE_FORMAT_NY.format(date);
        }
        /**
         * 月日
         */
        if (Constants.EXCEL_FORMAT_INDEX_DATE_YR_STRING.contains(dataFormatString) || Constants.EXCEL_FORMAT_INDEX_DATA_EXACT_YR.equals(dataFormat)) {
            return Constants.COMMON_DATE_FORMAT_YR.format(date);

        }
        if (Constants.COMMON_DATE_FORMAT_YR_CUSTOMIZE_STRING.contains(dataFormatString)) {
            return Constants.COMMON_DATE_FORMAT_YR_CUSTOMIZE.format(date);

        }
        /**
         * 月
         */
        if (Constants.EXCEL_FORMAT_INDEX_DATE_Y_STRING.contains(dataFormatString)) {
            return Constants.COMMON_DATE_FORMAT_Y.format(date);
        }
        /**
         * 星期X
         */
        if (Constants.EXCEL_FORMAT_INDEX_DATE_XQ_STRING.contains(dataFormatString)) {
            return Constants.COMMON_DATE_FORMAT_XQ + dateToWeek(date);
        }
        /**
         * 周X
         */
        if (Constants.EXCEL_FORMAT_INDEX_DATE_Z_STRING.contains(dataFormatString)) {
            return Constants.COMMON_DATE_FORMAT_Z + dateToWeek(date);
        }
        /**
         * 时间格式
         */
        if (Constants.EXCEL_FORMAT_INDEX_TIME_STRING.contains(dataFormatString) || Constants.EXCEL_FORMAT_INDEX_TIME_EXACT.contains(dataFormat)) {
            return Constants.COMMON_TIME_FORMAT.format(DateUtil.getJavaDate(value));
        }
        /**
         * 单元格为其他未覆盖到的类型
         */
        if (DateUtil.isADateFormat(dataFormat, dataFormatString)) {
            return Constants.COMMON_TIME_FORMAT.format(value);
        }

        return null;
    }

    private static String dateToWeek(Date date) {
        if (date == null){
            return "";
        }
        // 获得一个日历
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 指示一个星期中的某天。
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return Constants.WEEK_DAYS[w];
    }
}
