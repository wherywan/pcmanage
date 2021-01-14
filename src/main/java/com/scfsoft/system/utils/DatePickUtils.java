package com.scfsoft.system.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author sfx
 * @date 2020-06-22
 */
public class DatePickUtils {
    private final static String[] sessionDays = {"03-31", "06-30", "09-30", "12-31"};

    public static final int ONE = 1;
    public static final int THREE = 3;
    public static final int SIX = 6;
    public static final int NINE = 9;
    public static final int TWELVE = 12;
    public static final int TEN = 10;
    public static final int TWENTY = 20;
    public static final int ZERO = 0;

    /**
     * @author sfx
     * @date 2020-06-22
     * @param curDate 当前日期
     * @return 上旬日
     */
    public static String getLastTenDaysDate(String curDate) {
        String[] ymd = curDate.split("-");
        int year = Integer.parseInt(ymd[0]);
        int month = Integer.parseInt(ymd[1]);
        int day = Integer.parseInt(ymd[2]);
        if (day > ZERO && day <= TEN && month == ONE) {
            return (year - 1) + "-12-31";
        } else if (day > ZERO && day <= TEN && month > ONE) {
            // 取上月末
            return getLastMonthDay(curDate);
        } else if (day > TEN && day <= TWENTY) {
            return year + "-" + String.format("%02d", month) + "-" + TEN;
        } else {
            return year + "-" + String.format("%02d", month) + "-" + TWENTY;
        }
    }
    /*
    * 获取前n旬日期列表
    * */
    public static List<String> getYearDateList(String curDate){
        String[] ymd = curDate.split("-");
        int year = Integer.parseInt(ymd[0]);
        int month = Integer.parseInt(ymd[1]);
        int day = Integer.parseInt(ymd[2]);
        String date1 = "";
        String date2 = "";
        String date3 = "";
        List<String> dayList = new ArrayList<>();
        for(int curMonth = 1;curMonth<month;curMonth++){
            date1 = year+"-"+String.format("%02d",curMonth)+"-"+TEN;
            date2 = year+"-"+String.format("%02d",curMonth)+"-"+TWENTY;
            date3 = getLastMonthDay(year+"-"+String.format("%02d",curMonth+1)+"-"+TEN);
            dayList.add(date1);
            dayList.add(date2);
            dayList.add(date3);
        }
        if(day<=TEN){
            return dayList;
        }else if(day<=TWENTY){
            dayList.add(year+"-"+String.format("%02d",month)+"-"+TEN);
            return dayList;
        }else {
            dayList.add(year+"-"+String.format("%02d",month)+"-"+TEN);
            dayList.add(year+"-"+String.format("%02d",month)+"-"+TWENTY);
            return dayList;
        }
    }
    /**
     * @author sfx
     * @date 2020-06-22
     * @param curDate 当前日期
     * @return 上季日
     */
    public static String getLastSessionDate(String curDate) {
        String[] ymd = curDate.split("-");
        int year = Integer.parseInt(ymd[0]);
        int month = Integer.parseInt(ymd[1]);
        if (month > THREE && month <= SIX) {
            return year + "-" + sessionDays[0];
        } else if (month > SIX && month <= NINE) {
            return year + "-" + sessionDays[1];
        } else if (month > NINE && month <= TWELVE) {
            return year + "-" + sessionDays[2];
        } else {
            return (year - 1) + "-" + sessionDays[3];
        }
    }

    /**
     * @author sfx
     * @date 2020-06-22
     * @param curDate 当前日期
     * @return 当年第一天
     */
    public static String getFirstDayOfYear(String curDate) {
        return curDate.substring(0, 4) + "-01-01";
    }

    /**
     * @author sfx
     * @date 2020-06-22
     * @param curDate 当前日期
     * @return 上年第一天
     */
    public static String getFirstDayOfLastYear(String curDate) {
        String[] ymd = curDate.split("-");
        int year = Integer.parseInt(ymd[0]);
        return (year - 1) + "-01-01";
    }

    /**
     * @author sfx
     * @date 2020-06-22
     * @param curDate 当前日期
     * @return 上年同一天
     */
    public static String getSameDayOfLastYear(String curDate) {
        String[] ymd = curDate.split("-");
        int year = Integer.parseInt(ymd[0]);
        return (year - 1) + curDate.substring(4);
    }

    /**
     * @author sfx
     * @date 2020-06-22
     * @param curDate 当前日期
     * @return 前一天
     */
    public static String getLastDay(String curDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        try {
            cale.setTime(format.parse(curDate));
            int day = cale.get(Calendar.DATE);
            cale.set(Calendar.DATE, day - 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(cale.getTime());
    }

    /**
     * @author sfx
     * @date 2020-06-22
     * @param curDate 当前日期
     * @return 上月
     */
    public static String getLastMonthDay(String curDate) {
        // 取上月末
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        try {
            cale.setTime(format.parse(curDate));
            cale.set(Calendar.DAY_OF_MONTH, 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(cale.getTime());
    }

    /**
     * @author sfx
     * @date 2020-06-22
     * @param curDate 当前日期
     * @return 年初
     */
    public static String getBeginOfYear(String curDate) {
        String[] ymd = curDate.split("-");
        int year = Integer.parseInt(ymd[0]);
        return (year - 1) + "-12-31";
    }
    /*
    * 得到当年日期
    *
    * */
    public static void main(String[] args) {
        String curDate = "2020-05-03";
        System.out.println(getYearDateList(curDate));
//        System.out.println(getLastTenDaysDate(curDate));
//        System.out.println(getLastSessionDate(curDate));
//        System.out.println(getFirstDayOfYear(curDate));
//        System.out.println(getLastDay(curDate));
//        System.out.println(getLastMonthDay(curDate));
//        System.out.println(getBeginOfYear(curDate));
//        System.out.println(getFirstDayOfLastYear(curDate));
//        System.out.println(getSameDayOfLastYear(curDate));
    }

}
