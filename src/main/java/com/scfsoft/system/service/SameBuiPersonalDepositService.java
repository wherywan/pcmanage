package com.scfsoft.system.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scfsoft.sdk.common.api.dto.StandardPagination;
import com.scfsoft.system.dao.jpa.SameBusiDepositDAO;
import com.scfsoft.system.dao.mybatis.SameBusiPersonalDepositMapper;
import com.scfsoft.system.dto.SameBusiDepositDto;
import com.scfsoft.system.dto.SameBusiSearchParam;
import com.scfsoft.system.dto.SearchParam;
import com.scfsoft.system.entity.jpa.PersonalDeposit;
import com.scfsoft.system.entity.jpa.SameBusiPersonalDeposit;
import com.scfsoft.system.enums.OrgCode;
import com.scfsoft.system.utils.DatePickUtils;
import com.scfsoft.system.utils.ExcelUtils;
import com.scfsoft.system.utils.RankComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SameBuiPersonalDepositService {

    private final SaveFileDataService dataService;

    private final SameBusiDepositDAO sbDepositDAO;

    private final SameBusiPersonalDepositMapper sbDepositMapper;

    @Async
    public Future<String> handleFileData(File file, String fileName) {
        Future<String> future = null;
        try {
            InputStream in = new FileInputStream(file);
            //获取文件数据
            List<String[]> datalist = ExcelUtils.getFileData(in);
            //处理并保存数据到数据库中
            dataService.saveSameBuiData(datalist);
            future = new AsyncResult<>(fileName + "处理成功");
        }catch (Exception e){
            e.printStackTrace();
            future = new AsyncResult<>(fileName + "处理失败");
        }finally {
            return future;
        }
    }

    public void createExcel(HttpServletResponse response,String curDate,String tempFileName){
        /*获取模板文件*/
        Resource resource = new ClassPathResource("excleTemplate/"+tempFileName);
        /*获取文件名*/
        String[] ymd = curDate.split("-");
        String titleName = tempFileName.replace("YYYY",ymd[0]).replace("MM",ymd[1]).replace("DD",ymd[2]);
        titleName = titleName.substring(0,titleName.indexOf("."));
        /*创建工作簿*/
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(resource.getFile());
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow topRow = sheet.getRow(0);
            XSSFCellStyle rowCellStyle = topRow.getCell(0).getCellStyle();
            XSSFCell rowCell = topRow.createCell(0);
            /*设置首行单元格样式与值*/
            rowCell.setCellValue(titleName);
            rowCell.setCellStyle(rowCellStyle);
//          /*获取列数*/
            int cellNum = sheet.getRow(5).getLastCellNum();
            XSSFCellStyle contentStyle = sheet.getRow(6).getCell(0).getCellStyle();
            /*获取业务数据填充到工作簿中*/
            Map<Integer,Map<String,SameBusiDepositDto>> mapMap = getFormData(curDate);
            int rowNum = mapMap.get(1) != null ? mapMap.get(1).size() : 0;
            if(rowNum>0) {
                Map<String,SameBusiDepositDto> commonDeposit = null;
                Map<String, SameBusiDepositDto> personalDeposit = mapMap.get(1);
                Map<String, SameBusiDepositDto> coreDeposit = mapMap.get(2);
                Map<String, SameBusiDepositDto> structureDeposit = mapMap.get(3);
                int j = 0;
                for(int i=1;i<4;i++){
                    if(i==1){
                        j = 6;
                        commonDeposit = personalDeposit;
                    }else if(i==2){
                        j = 29;
                        commonDeposit = coreDeposit;
                    }else{
                        j = 49;
                        commonDeposit = structureDeposit;
                    }

                    for(String orgId:commonDeposit.keySet()){
                        SameBusiDepositDto deposit1 = commonDeposit.get(orgId);
                        /*根据反射获取data对象中的字段*/
                        Field[] field = deposit1.getClass().getDeclaredFields();
                        Field.setAccessible(field,true);
                        /*创建行*/
                        XSSFRow dataRow = sheet.createRow(j);
                        for(int column = 0;column<cellNum;column++){
                            XSSFCell cell = dataRow.createCell(column);
                            if(column==0){
                                cell.setCellValue((String) field[column].get(deposit1));
                                cell.setCellStyle(contentStyle);
                            }else if(column==1||column==4||column == 7||column==9||column==11||column==13){
                                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                                if(field[column+2].get(deposit1)!=null){
                                    cell.setCellValue(decimalFormat.format(field[column+2].get(deposit1)));
                                }
                                cell.setCellStyle(contentStyle);
                            }else if(column==3||column==6){
                                if(field[column+2].get(deposit1)!=null){
                                    cell.setCellValue((String)field[column+2].get(deposit1));
                                }
                                cell.setCellStyle(contentStyle);
                            }else {
                                if(field[column+2].get(deposit1)!=null){
                                    cell.setCellValue((Integer)field[column+2].get(deposit1));
                                }
                                cell.setCellStyle(contentStyle);
                            }
                        }
                        j++;
                    }
                }
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                workbook.write(os);
            }catch (IOException e){
                e.printStackTrace();
            }
            byte[] dataByte = os.toByteArray();
            /*将自己数组转化为输入*/
            InputStream is = new ByteArrayInputStream(dataByte);
            /*设置response参数*/
            response.reset(); //清空缓冲区数据
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(titleName+".xlsx","UTF-8"));
            response.setContentType("application/octet-stream;charset=utf-8");
            ServletOutputStream out =response.getOutputStream();
            /*创建缓存对象*/
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] bytes = new byte[2048];
                int byteRead = 0;
                while((byteRead=bis.read(bytes,0,bytes.length))!=-1){
                    bos.write(bytes,0,byteRead);
                }
                bos.flush();
            }catch (IOException e){
                throw e;
            }finally {
                if(bis!=null){
                    bis.close();
                }
                if(bos!=null){
                    bos.close();
                }
                if(is!=null){
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据日期获取表数据
     * @param date
     * @return
     */
    public Map<Integer, Map<String,SameBusiDepositDto>> getFormData(String date){
        /*构造搜索参数*/
        Map<Integer, Map<String,SameBusiDepositDto>> map = new HashMap<>();
        for(int i = 1; i<4; i++){
            SameBusiSearchParam searchParam = new SameBusiSearchParam();
            Map<String,SameBusiDepositDto> result = new LinkedHashMap<>();
            searchParam.setCurDate(date);
            searchParam.setType(String.valueOf(i));
            /*查数据*/
            List<SameBusiDepositDto> deposits = getResultList(searchParam);
//            log.info(deposits.get(0).getOrgName());
            Map<String, SameBusiDepositDto> dtoMap = deposits.stream().collect(Collectors.toMap(SameBusiDepositDto::getOrgId,p -> p,(k1,k2) -> k2));
            dtoMap.entrySet().stream().sorted(Map.Entry.<String, SameBusiDepositDto>comparingByKey()).forEachOrdered((e)->result.put(e.getKey(),e.getValue()));
            map.put(i, result);
        }
        return map;
    }

    /**
     * 获取报表数据
     * @param searchParam
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<SameBusiDepositDto> getResultList(SameBusiSearchParam searchParam){

        List<SameBusiPersonalDeposit> deposits = sbDepositMapper.getAllDeposit(searchParam);
        System.out.println("测试"+deposits.size());
        if(deposits.size()==0&&searchParam.getType().equals("3")){
            SameBusiSearchParam searchParam1 = new SameBusiSearchParam();
            searchParam1.setCurDate(searchParam.getCurDate());
            searchParam1.setType("1");
            SameBusiSearchParam searchParam2 = new SameBusiSearchParam();
            searchParam2.setCurDate(searchParam.getCurDate());
            searchParam2.setType("2");
            List<SameBusiPersonalDeposit> deposits1 = sbDepositMapper.getAllDeposit(searchParam1);
            List<SameBusiPersonalDeposit> deposits2 = sbDepositMapper.getAllDeposit(searchParam2);
            if(deposits1.size()>0&&deposits2.size()>0){
                List<SameBusiPersonalDeposit> depositList =new ArrayList<>();
                deposits1.forEach(generalDeposit->{
                    SameBusiPersonalDeposit deposit =new SameBusiPersonalDeposit();
                    List<BigDecimal> list = new ArrayList<>();
                    SameBusiPersonalDeposit coreDeposit = sbDepositMapper.getUpTenDays(generalDeposit.getCurDate(),generalDeposit.getOrgId(),"2");
                    deposit.setCurDate(generalDeposit.getCurDate());
                    deposit.setOrgId(generalDeposit.getOrgId());
                    deposit.setType("3");
                    BigDecimal structureCcb = generalDeposit.getCcb().subtract(coreDeposit.getCcb());
                    deposit.setCcb(structureCcb);
                    list.add(structureCcb);
                    deposit.setIcbc(generalDeposit.getIcbc().subtract(coreDeposit.getIcbc()));
                    list.add(generalDeposit.getIcbc().subtract(coreDeposit.getIcbc()));
                    deposit.setAbc(generalDeposit.getAbc().subtract(coreDeposit.getAbc()));
                    list.add(generalDeposit.getAbc().subtract(coreDeposit.getAbc()));
                    deposit.setBc(generalDeposit.getBc().subtract(coreDeposit.getBc()));
                    list.add(generalDeposit.getBc().subtract(coreDeposit.getBc()));
                    list.sort(new RankComparator());
                    deposit.setNewRank(getNewRank(list,structureCcb));
                    depositList.add(deposit);
                });
                sbDepositDAO.saveAll(depositList);
                deposits = depositList;
            }
        }
        if(deposits.size()>0){
            List<SameBusiDepositDto> depositDtoList = new ArrayList<>();
            deposits.forEach((deposit)->{
                depositDtoList.add( calculateSameBuiDeposit(deposit));
            });
            return depositDtoList;
        }
        return new ArrayList<SameBusiDepositDto>();
    }
    /**
     * 根据查询参数得到分页结果列表
     * @param page 分页信息
     * @param searchParam 搜索参数
     * @return 返回结果列表
     */
    public List<SameBusiDepositDto> getSameBusiDepositList(StandardPagination page,SameBusiSearchParam searchParam){
        PageHelper.startPage(page.getPageNumber(),page.getPageSize());
        List<SameBusiDepositDto> depositDtoList =getResultList(searchParam);
            PageInfo<SameBusiDepositDto> pageInfo = new PageInfo<>(depositDtoList);
            page.setTotalPage(pageInfo.getPages());
            log.info("{}", pageInfo.getPages());
            log.info("{}", pageInfo.getTotal());
            page.setTotalRecord((int) pageInfo.getTotal());
            return depositDtoList;
    }
    //计算同业数据并存入dto
    public SameBusiDepositDto calculateSameBuiDeposit(SameBusiPersonalDeposit spDeposit){
        SameBusiDepositDto sameBusiDepositDto = new SameBusiDepositDto();
        /*从数据库查当年的数据 1.前n旬日期，2，org_id,3、类型*/
        SameBusiPersonalDeposit yearTotalParam = new SameBusiPersonalDeposit();
        yearTotalParam.setDayList(DatePickUtils.getYearDateList(spDeposit.getCurDate()));
        yearTotalParam.setOrgId(spDeposit.getOrgId());
        yearTotalParam.setType(spDeposit.getType());
        SameBusiPersonalDeposit sameYearTotal = sbDepositMapper.getYearTotalByParam(yearTotalParam);
        log.info("{}",sameYearTotal);
        /*计算增长和排名*/
        List<BigDecimal> bankTotal = new ArrayList<>();
        RankComparator rankComparator = new RankComparator();
        BigDecimal ccbTotal = null;
        if(sameYearTotal!=null) {
            log.info("{}", sameYearTotal.getCcbTotal());
            ccbTotal = spDeposit.getCcb().add(sameYearTotal.getCcbTotal());
            bankTotal.add(ccbTotal);
            bankTotal.add(spDeposit.getIcbc().add(sameYearTotal.getIcbcTotal()));
            bankTotal.add(spDeposit.getAbc().add(sameYearTotal.getAbcTotal()));
            bankTotal.add(spDeposit.getBc().add(sameYearTotal.getBcTotal()));
        }else {
            ccbTotal = spDeposit.getCcb();
            bankTotal.add(ccbTotal);
            bankTotal.add(spDeposit.getIcbc());
            bankTotal.add(spDeposit.getAbc());
            bankTotal.add(spDeposit.getBc());
        }
        bankTotal.sort(rankComparator);
        log.info("{}", bankTotal.get(0));
        Integer rankYear = null;
        String offsetYear = "";
        rankYear = getNewRank(bankTotal,ccbTotal);
        if(rankYear<3){
            offsetYear = "-";
        }else if(rankYear == 3){
            offsetYear = "1";
        }else{
            offsetYear ="2";
        }
        /*获取当旬排名偏差*/
        String offsetCur = "";
        if(spDeposit.getNewRank()<3){
            offsetCur = "-";
        }else if(spDeposit.getNewRank() == 3){
            offsetCur = "1";
        }else{
            offsetCur ="2";
        }
        /*获取上旬数据*/
        String upTenDaysDate = DatePickUtils.getLastTenDaysDate(spDeposit.getCurDate());
        String upType = spDeposit.getType();
        String upOrgId = spDeposit.getOrgId();
        SameBusiPersonalDeposit upTenDaysDeposit = sbDepositMapper.getUpTenDays(upTenDaysDate,upOrgId,upType);
        BigDecimal timeUpThanUp;
        BigDecimal changeCurThanUp;
        Integer rankUpThanUp;
        Integer rankCurThanUp;
//        BigDecimal changeCcbDeposit = null;
        List<BigDecimal> changeBankDeposit = new ArrayList<>();
        if(upTenDaysDeposit ==null){
            timeUpThanUp = null;
            rankUpThanUp = null;
            changeCurThanUp = null;
            rankCurThanUp  = null;
        }else{
            timeUpThanUp = upTenDaysDeposit.getCcb();
            rankUpThanUp = upTenDaysDeposit.getNewRank();
            changeCurThanUp = spDeposit.getCcb().subtract(upTenDaysDeposit.getCcb());
            changeBankDeposit.add(spDeposit.getIcbc().subtract(upTenDaysDeposit.getIcbc()));
            changeBankDeposit.add(changeCurThanUp);
            changeBankDeposit.add(spDeposit.getAbc().subtract(upTenDaysDeposit.getAbc()));
            changeBankDeposit.add(spDeposit.getBc().subtract(upTenDaysDeposit.getBc()));
            changeBankDeposit.sort(rankComparator);
            rankCurThanUp =getNewRank(changeBankDeposit,changeCurThanUp);
        }
        /*获取去年同期数据*/
        String lastYearDate = DatePickUtils.getSameDayOfLastYear(spDeposit.getCurDate());
        SameBusiPersonalDeposit lastYearDeposit = sbDepositMapper.getLastYearDeposit(lastYearDate,spDeposit.getOrgId(),spDeposit.getType());
        BigDecimal timeYearThanYear;
        BigDecimal changeCurThanYear;
        Integer rankYearThanYear;
        Integer rankCurThanYear;
//        BigDecimal changeCcbDepositThanYear = null;
        List<BigDecimal> changeBankDepositThanYear = new ArrayList<>();
        if(lastYearDeposit ==null){
            timeYearThanYear = null;
            rankYearThanYear = null;
            changeCurThanYear = null;
            rankCurThanYear  = null;
        }else{
            timeYearThanYear = lastYearDeposit.getCcb();
            rankYearThanYear = lastYearDeposit.getNewRank();
            changeCurThanYear = spDeposit.getCcb().subtract(lastYearDeposit.getCcb());
            changeBankDepositThanYear.add(spDeposit.getIcbc().subtract(lastYearDeposit.getIcbc()));
            changeBankDepositThanYear.add(changeCurThanYear);
            changeBankDepositThanYear.add(spDeposit.getAbc().subtract(lastYearDeposit.getAbc()));
            changeBankDepositThanYear.add(spDeposit.getBc().subtract(lastYearDeposit.getBc()));
            changeBankDepositThanYear.sort(rankComparator);
            rankCurThanYear =getNewRank(changeBankDepositThanYear,changeCurThanYear);
        }
        sameBusiDepositDto.setCurDate(spDeposit.getCurDate());
        sameBusiDepositDto.setOrgId(spDeposit.getOrgId());
        sameBusiDepositDto.setTimeYear(ccbTotal);
        sameBusiDepositDto.setRankYear(rankYear);
        sameBusiDepositDto.setOffsetYear(offsetYear);
        sameBusiDepositDto.setTimeCur(spDeposit.getCcb());
        sameBusiDepositDto.setRankCur(spDeposit.getNewRank());
        sameBusiDepositDto.setOffsetCur(offsetCur);
        sameBusiDepositDto.setTimeUpThanUp(timeUpThanUp);
        sameBusiDepositDto.setRankUpThanUp(rankUpThanUp);
        sameBusiDepositDto.setChangeCurThanUp(changeCurThanUp);
        sameBusiDepositDto.setRankCurThanUp(rankCurThanUp);
        sameBusiDepositDto.setTimeYearThanYear(timeYearThanYear);
        sameBusiDepositDto.setRankYearThanYear(rankYearThanYear);
        sameBusiDepositDto.setChangeCurThanYear(changeCurThanYear);
        sameBusiDepositDto.setRankCurThanYear(rankCurThanYear);
        sameBusiDepositDto.setOrgName(OrgCode.getOrgNameMap().get(spDeposit.getOrgId()));
        return sameBusiDepositDto;
    }
    /*获得建行新增排名*/
    public Integer getNewRank(List<BigDecimal> bankDeposit,BigDecimal ccbDeposit){
        int newRank = 0;
        for (int rank = 0;rank < bankDeposit.size();rank++) {
            if(ccbDeposit.compareTo(bankDeposit.get(rank)) == 0){
                newRank = rank+1;
                break;
            }
        }
        return newRank;
    }
}
