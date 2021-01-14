package com.scfsoft.system.service;

import com.google.common.collect.Lists;
import com.scfsoft.sdk.common.providers.ProviderFactory;
import com.scfsoft.system.dao.jpa.PersonalDepositDAO;
import com.scfsoft.system.dao.jpa.SameBusiDepositDAO;
import com.scfsoft.system.dao.mybatis.PersonalDepositMapper;
import com.scfsoft.system.dao.mybatis.SameBusiPersonalDepositMapper;
import com.scfsoft.system.dto.PersonalDepositDto;
import com.scfsoft.system.entity.jpa.PersonalDeposit;
import com.scfsoft.system.entity.jpa.SameBusiPersonalDeposit;
import com.scfsoft.system.enums.OrgCode;
import com.scfsoft.system.utils.DatePickUtils;
import com.scfsoft.system.utils.RankComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author sunfx
 * @date 2020/6/19
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SaveFileDataService {

    private final PersonalDepositDAO depositDAO;
    private final SameBusiDepositDAO sbDepositDAO;

    private final PersonalDepositMapper depositMapper;
    private final SameBusiPersonalDepositMapper sbDepositMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveDepositData(List<String[]> dataList) throws Exception {
        if (dataList != null && dataList.size() > 3) {
            // 判断个人存款还是结构性存款
            String[] firstRow = dataList.get(0);
            // 对私存款
            if (firstRow != null && firstRow.length > 0 && firstRow[0] != null && firstRow[0].indexOf("对私存款")  > -1) {
                // 读取统计日期、单位、币种信息
                String secondRow = dataList.get(1)[0] != null ? dataList.get(1)[0] : "";
                if (StringUtils.isNotBlank(secondRow)) {
                    Pattern p = Pattern.compile("\\s+");
                    Matcher m = p.matcher(secondRow);
                    String[] vals = m.replaceAll(" ").split(" ");
                    String statDate = "";
                    String unit = "";
                    String ccy = "";
                    for (int i = 0; i < vals.length; i++) {
                        String tmpStr = vals[i];
                        if (tmpStr.startsWith("统计日期")) {
                            statDate = tmpStr.substring(tmpStr.indexOf("：") + 1);
                        } else if (tmpStr.startsWith("单位")) {
                            unit = tmpStr.substring(tmpStr.indexOf("：") + 1);
                        } else if (tmpStr.startsWith("币种")) {
                            ccy = tmpStr.substring(tmpStr.indexOf("：") + 1);
                            // 币种
                            if (ccy != null && ccy.indexOf("本外币合计") > -1) {
                                ccy = "1";
                            } else {
                                ccy = "2";
                            }
                        }
                    }
                    // 删除历史数据
                    depositMapper.deleteByStatDate(statDate, ccy);

                    // 获取历史数据 作比较
                    Map<String, PersonalDepositDto> map = getExpangdDeposit(statDate, ccy);

                    String finalStatDate = statDate;
                    String finalCcy = ccy;
                    String finalUnit = unit;
                    dataList = dataList.subList(3, dataList.size());
                    List<PersonalDeposit> list = dataList.parallelStream().map(cells -> {
                        PersonalDeposit deposit = new PersonalDeposit();

                        deposit.setOrgId(cells[3] != null ? cells[3].substring(0, cells[3].indexOf(".")) : "");
                        deposit.setStatDate(finalStatDate);
                        deposit.setCcy(finalCcy);
                        deposit.setUnit(finalUnit);
                        deposit.setTBal(new BigDecimal(cells[4]));
                        deposit.setTBalThanLastD(new BigDecimal(cells[5]));
                        deposit.setTBalThanLastM(new BigDecimal(cells[6]));
                        deposit.setTBalThanBeginY(new BigDecimal(cells[7]));
                        deposit.setABal(new BigDecimal(cells[8]));
                        deposit.setABalThanLastD(new BigDecimal(cells[9]));
                        deposit.setABalThanLastM(new BigDecimal(cells[10]));
                        deposit.setABalThanBeginY(new BigDecimal(cells[11]));
                        deposit.setCreateUser(ProviderFactory.getCurrentSubject());
                        if (finalCcy.equals("1")) {
                            BigDecimal ps = new BigDecimal(cells[4]).subtract(new BigDecimal(cells[8]));
                            deposit.setPriceScissors(ps);
                        }
                        setExpandDeposit(deposit, map);
                        return deposit;
                    }).collect(Collectors.toList());

                    // 保存明细信息 个人存款
                    depositDAO.saveAll(list);
                }
            } else {
                // 获取单位、日期
                String unit = dataList.get(0).length > 3 ? dataList.get(0)[3] : "";
                String statDate = dataList.get(1).length > 1 ? dataList.get(1)[1] : "";

                // 删除历史数据
                depositMapper.deleteByStatDate(statDate, "3");
                List<PersonalDeposit> list = Lists.newArrayList();
                for (int row = 4; row < dataList.size(); row ++) {
                    PersonalDeposit deposit = new PersonalDeposit();
                    String[] cells = dataList.get(row);

                    deposit.setOrgId(cells[1] != null ? cells[1].substring(0, cells[1].indexOf(".")) : "");
                    deposit.setStatDate(statDate);
                    deposit.setCcy("3");
                    deposit.setUnit(unit);
                    deposit.setTBal(new BigDecimal(cells[2]));
                    deposit.setTBalThanBeginY(new BigDecimal(cells[3]));
                    deposit.setABal(new BigDecimal(cells[4]));
                    deposit.setCreateUser(ProviderFactory.getCurrentSubject());
                    list.add(deposit);
                }

                // 保存明细信息 结构性存款
                depositDAO.saveAll(list);
            }
        }
    }

    private Map<String, PersonalDepositDto> getExpangdDeposit(String curDate, String ccy) {
        PersonalDeposit deposit = new PersonalDeposit();

        deposit.setCcy(ccy);
        List<String> dayList = Lists.newArrayList();
        dayList.add(DatePickUtils.getLastTenDaysDate(curDate));
        dayList.add(DatePickUtils.getLastSessionDate(curDate));
        dayList.add(DatePickUtils.getFirstDayOfYear(curDate));
        dayList.add(DatePickUtils.getFirstDayOfLastYear(curDate));
        dayList.add(DatePickUtils.getSameDayOfLastYear(curDate));
        if (deposit.getCcy().equals("3")) {
            dayList.add(DatePickUtils.getLastDay(curDate));
            dayList.add(DatePickUtils.getLastMonthDay(curDate));
            dayList.add(DatePickUtils.getBeginOfYear(curDate));
        }
        deposit.setDayList(dayList);
        // 查询指标值
        List<PersonalDepositDto> list = depositMapper.selectByStatDates(deposit);

        Map<String, PersonalDepositDto> resultMap = list.stream().collect(Collectors.toMap(PersonalDepositDto::getOrgId, a -> a,(k1, k2) -> k2));

        return resultMap;
    }


    // 计算上旬、上级、同期增加
    private void setExpandDeposit(PersonalDeposit deposit, Map<String, PersonalDepositDto> resultMap) {
        try {
            String orgId = deposit.getOrgId();
            String curDate = deposit.getStatDate();
            BigDecimal tBal = deposit.getTBal();
            BigDecimal aBal = deposit.getABal();
            // 上旬
            PersonalDepositDto lastTenDeposit = resultMap.get(orgId + DatePickUtils.getLastTenDaysDate(curDate));
            // 上季
            PersonalDepositDto lastSessionDeposit = resultMap.get(orgId + DatePickUtils.getLastSessionDate(curDate));
            // 今年 01-01
            PersonalDepositDto FirstDeposit = resultMap.get(orgId + DatePickUtils.getFirstDayOfYear(curDate));
            // 去年 01-01
            PersonalDepositDto lastFirstDeposit = resultMap.get(orgId + DatePickUtils.getFirstDayOfLastYear(curDate));
            // 去年同日
            PersonalDepositDto SameDayLastYearDeposit = resultMap.get(orgId + DatePickUtils.getSameDayOfLastYear(curDate));
            if (lastTenDeposit != null) {
                deposit.setTBalThanTendays(tBal.subtract(lastTenDeposit.getTBal()));
                deposit.setABalThanTendays(aBal.subtract(lastTenDeposit.getABal()));
            }
            if (lastSessionDeposit != null) {
                deposit.setTBalThanLastS(tBal.subtract(lastSessionDeposit.getTBal()));
                deposit.setABalThanLastS(aBal.subtract(lastSessionDeposit.getABal()));
            }
            if (FirstDeposit != null && lastFirstDeposit != null && SameDayLastYearDeposit != null) {
                deposit.setTBalThanLastY((tBal.subtract(FirstDeposit.getTBal())).subtract(SameDayLastYearDeposit.getTBal().subtract(lastFirstDeposit.getTBal())));
                deposit.setABalThanLastY((aBal.subtract(FirstDeposit.getABal())).subtract(SameDayLastYearDeposit.getABal().subtract(lastFirstDeposit.getABal())));
            }

            if (deposit.getCcy().equals("3")) {
                // 上日
                PersonalDepositDto lastDayDeposit = resultMap.get(orgId + DatePickUtils.getLastDay(curDate));
                // 上月
                PersonalDepositDto lastMonthDeposit = resultMap.get(orgId + DatePickUtils.getLastMonthDay(curDate));
                // 年初
                PersonalDepositDto beginYearDeposit = resultMap.get(orgId + DatePickUtils.getBeginOfYear(curDate));
                if (lastDayDeposit != null) {
                    deposit.setTBalThanLastD(tBal.subtract(lastDayDeposit.getTBal()));
                    deposit.setABalThanLastD(aBal.subtract(lastDayDeposit.getABal()));
                }
                if (lastMonthDeposit != null) {
                    deposit.setTBalThanLastM(tBal.subtract(lastMonthDeposit.getTBal()));
                    deposit.setABalThanLastM(aBal.subtract(lastMonthDeposit.getABal()));
                }
                if (beginYearDeposit != null) {
                    deposit.setABalThanBeginY(aBal.subtract(beginYearDeposit.getABal()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /*保存同业数据到数据库*/
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveSameBuiData(List<String[]> dataList){
        log.debug(dataList.get(3)[0]);
        if(dataList!=null&&dataList.size()>4){
            /*获取日期*/
            String str = dataList.get(0)[0];
            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(str);
            String[] newStr = m.replaceAll(" ").split(" ");
            log.info("{}",newStr.length);
            String date = newStr[newStr.length-1];

            /*将日期转为标准格式*/
            if(date.indexOf(".")>0){
                date = date.replace(".","-");
            }
            log.info("{}",date);

            /*获取表类型*/
            String typeString = dataList.get(2)[1];
//            log.info(typeString);
            String excelType = typeString.substring(0, typeString.indexOf("存款"));
//            log.info(excelType);
            if(excelType.equals("一般性")){
                excelType = "1";
            }else if(excelType.equals("核心")){
                excelType = "2";
            }
            /*删除历史数据*/
            sbDepositMapper.deleteByCurDate(date, excelType);
            sbDepositMapper.deleteByCurDate(date,"3");
            /*存放数据*/
            List<SameBusiPersonalDeposit> spdList = new ArrayList<>();
            for(int row = 4; row < dataList.size(); row++){
                String[] cell = dataList.get(row);
                SameBusiPersonalDeposit spDeposit = new SameBusiPersonalDeposit();
                spDeposit.setOrgId(OrgCode.getEnumMap().get(cell[0]));
                spDeposit.setCurDate(date);
                log.info(date);
                spDeposit.setType(excelType);
                spDeposit.setNewRank(Integer.parseInt(cell[10].substring(0,cell[10].indexOf("."))));
                spDeposit.setCcb(new BigDecimal(cell[11]));
                spDeposit.setIcbc(new BigDecimal(cell[12]));
                spDeposit.setAbc(new BigDecimal(cell[13]));
                spDeposit.setBc(new BigDecimal(cell[14]));
                spdList.add(spDeposit);
            }
            sbDepositDAO.saveAll(spdList);
        }
    }
}
