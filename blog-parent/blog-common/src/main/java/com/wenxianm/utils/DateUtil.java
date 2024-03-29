package com.wenxianm.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName DateUtil
 * @Author cwx
 * @Date 2021/10/19 16:38
 **/
@Slf4j
public final class DateUtil {

    private DateUtil() {
    }

    /***/
    public static final String FORMAT1 = "yyyy-MM-dd";
    /***/
    public static final String FORMAT2 = "yyyy-MM-dd HH:mm:ss";
    /***/
    public static final String FORMAT3 = "HH:mm:ss";
    /***/
    public static final String FORMAT4 = "yyyy-MM-dd HH";
    /***/
    public static final String FORMAT5 = "yyyyMMddHHmmss";
    /***/
    public static final String FORMAT6 = "yyyy年MM月dd日";
    /***/
    public static final String FORMAT7 = "yyyyMMddHHmm";
    /***/
    public static final String FORMAT8 = "yyyyMMdd";
    /***/
    public static final String FORMAT9 = "yyyyMMddHHmmssSSS";
    /***/
    public static final String FORMAT10 = "yyMMddHHmmssSSS";

    public static final String FORMAT11 = "yyyy-MM-dd HH:mm";

    public static final String FORMAT12 = "HH:mm";

    /***
     * 时间类型枚举
     */
    public enum Field {
        /**
         * 年
         */
        YEAR,
        /**
         * 月
         */
        MONTH,
        /**
         * 天
         */
        DAY,
        /**
         * 时
         */
        HOUR,
        /**
         * 分
         */
        MINUTE,
        /**
         * 秒
         */
        SECOND,
        /**
         * 毫秒
         */
        MILLISECOND;

    }


    ;

    /***
     * 返回枚举指定的日期类型
     *
     * @param date  日期
     * @param field 日期类型
     * @return int
     */
    public static int get(Date date, Field field) {

        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        switch (field) {
            case YEAR:
                return ca.get(Calendar.YEAR);
            case MONTH:
                return ca.get(Calendar.MONTH) + 1;
            case DAY:
                return ca.get(Calendar.DAY_OF_MONTH);
            case HOUR:
                return ca.get(Calendar.HOUR_OF_DAY);
            case MINUTE:
                return ca.get(Calendar.MINUTE);
            case SECOND:
                return ca.get(Calendar.SECOND);
            case MILLISECOND:
                return ca.get(Calendar.MILLISECOND);
            default:
                return 0;
        }

    }

    /***
     * 转换为日期
     *
     * @param dateStr 日期字符串
     * @return Date
     */
    public static Date toDate(String dateStr) {
        String format = null;
        switch (dateStr.length()) {
            case 7:
                format = "yyyy-MM";
                break;
            case 6:
                format = "yyMMdd";
                break;
            case 8:
                format = "yyyyMMdd";
                break;
            case 10:
                format = dateStr.indexOf("-") > 0 ? "yyyy-MM-dd" : "yyyyMMddHH";
                break;
            case 12:
                format = "yyyyMMddHHmm";
                break;
            case 14:
                format = "yyyyMMddHHmmss";
                break;
            case 17:
                format = "yyyyMMddHHmmssSSS";
                break;
            case 19:
                format = "yyyy-MM-dd HH:mm:ss";
                break;
            default:
                return null;
        }

        return DateUtil.convert(dateStr, format);
    }

    /**
     * 取得给定日期的凌晨0:00时间
     *
     * @param date 日期
     * @return String
     */
    public static Date getDateFirst(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        Calendar calfirstday = new GregorianCalendar(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH));
        return calfirstday.getTime();
    }

    /**
     * 取得给定日期的夜晚23:59:59时间
     *
     * @param date 日期
     * @return String
     */
    public static Date getDateLast(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 59, 59);
        return calendar.getTime();
    }

    /**
     * 指定格式,获取当前时间的字符串形式
     *
     * @param format 格式
     * @return String
     */
    public static String getCurrentDT(String format) {
        String res = "";
        try {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat(format);

            res = sf.format(date);
        } catch (Exception ex) {
            log.error("", ex);
        }
        return res;
    }

    /**
     * 取得给定日期相差n月的第一天凌晨0:00时间<br/> 1 n可以为负值或0,分别取之前的月份或指定月份
     *
     * @param date 日期
     * @param n 月份
     * @return Date
     */
    public static Date getMonthFirst(Date date, int n) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        Calendar calfirstday = new GregorianCalendar(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH) + n, 1);
        return calfirstday.getTime();
    }

    /**
     * 取得当月第一天凌晨0:00时间
     *
     * @return Date
     */
    public static Date getThisMonthFirst() {
        Calendar cal = Calendar.getInstance();
        Calendar calfirstday = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
        return calfirstday.getTime();
    }

    /**
     * 取得当月最后一天结束时间
     *
     * @return Date
     */
    public static Date getThisMonthLast() {
        //获取当前月最后一天
        Calendar cal = Calendar.getInstance();
        Calendar calfirstday = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return calfirstday.getTime();
    }

    /**
     * 取得某个月凌晨0:00时间
     *
     * @param date 日期
     * @return Date
     */
    public static Date getMonthFirst(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 取得某个月最后一天 最后一分一秒
     *
     * @param date 日期
     * @return Date
     */
    public static Date getMonthLast(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        //cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }


    /**
     * 取得下一天的凌晨0:00时间
     *
     * @return Date
     */
    public static Date getNextDay() {
        Calendar cal = Calendar.getInstance();
        Calendar calnextday = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1);
        calnextday.add(Calendar.MINUTE, 0);
        return calnextday.getTime();
    }

    /**
     * 取得今天凌晨00:00:00时间
     *
     * @return Date
     */
    public static Date getToday() {
        Calendar cal = Calendar.getInstance();
        Calendar calnextday = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        calnextday.add(Calendar.HOUR_OF_DAY, 0);
        calnextday.add(Calendar.MINUTE, 0);
        calnextday.add(Calendar.SECOND, 0);
        return calnextday.getTime();
    }

    /**
     * 取当前小时的0分0秒的时间
     *
     * @param date 日期
     * @return Date
     */
    public static Date getHourFirst(Date date) {
        if (date == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        return ca.getTime();
    }

    /**
     * 取得下一天的时间字符串
     *
     * @param date 日期
     * @return Date
     */
    public static String getNextDay(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            log.error("Exception:", e);
        }
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(cal.getTime());
    }

    /***
     * 获取下一天
     *
     * @param date 日期
     * @return date
     */
    public static Date getNextDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }


    /**
     * 字符串转化成日期
     *
     * @param date 日期字符串
     * @return date
     */
    public static Date convert(String date) {
        Date retValue = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
        try {
            retValue = sdf.parse(date);
        } catch (ParseException e) {
            log.error("", e);
        }
        return retValue;
    }

    /***
     * 日期转换
     *
     * @param date   日期字符串
     * @param format 转换格式
     * @return date
     */
    public static Date convert(String date, String format) {
        Date retValue = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            retValue = sdf.parse(date);
        } catch (ParseException e) {
            log.error("字符串转日期失败", e);
        }
        return retValue;
    }

    /***
     * 日期转换
     *
     * @param date 日期字符串
     * @return date
     */
    public static Date convert1(String date) {
        Date retValue = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT2);
        try {
            retValue = sdf.parse(date);
        } catch (ParseException e) {
            log.error("", e);
        }
        return retValue;
    }

    /***
     * 日期转换
     *
     * @param date 日期格式
     * @return date
     */
    public static Date convert2(String date) {
        Date retValue = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT3);
        try {
            retValue = sdf.parse(date);
        } catch (ParseException e) {
            log.error("", e);
        }
        return retValue;
    }

    /***
     * 日期转换
     *
     * @param date 日期字符串
     * @return date
     */
    public static Date convert3(String date) {
        Date retValue = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT4);
        try {
            retValue = sdf.parse(date);
        } catch (ParseException e) {
            log.error("", e);
        }
        return retValue;
    }

    /**
     * 日期转化成字符串
     *
     * @param date 日期格式
     * @param format 转换格式
     * @return String
     */
    public static String convert(Date date, String format) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String retstr = sdf.format(date);
        return retstr;
    }

    /**
     * 取得与今天相隔plus天凌晨0:00时间
     *
     * @param plus 天数
     * @return date
     */
    public static Date getDateFromToday(int plus) {
        Calendar cal = Calendar.getInstance();
        Calendar calnextday = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + plus);
        return calnextday.getTime();
    }

    /****
     * 取得与今天相隔plus天晚上23:59:59时间, add by zhoulong
     * @param plus 天数
     * @return
     */
    public static Date getLastDateFromToday(int plus) {
        Date date = getDateFromToday(plus);
        date = addDateHours(date, 23);
        date = addDateMinutes(date, 59);
        date = addDateSeconds(date, 59);
        return date;
    }
    /****
     * 取得传入时间的加减天、小时，分钟，秒后的时间
     * @param date 传入的时间
     * @param days 天数
     * @param hours 小时
     * @param minutes 分钟
     * @param seconds 秒
     * @return
     */
    public static Date addTime(Date date, int days, int hours, int minutes, int seconds) {
        if (null == date) {
            return null;
        }
        date = addDateDays(date, days);
        date = addDateHours(date, hours);
        date = addDateMinutes(date, minutes);
        date = addDateSeconds(date, seconds);
        return date;
    }

    /****
     * 设置传入时间的时分秒
     * @param date 传入时间
     * @param hours 时
     * @param minutes 分
     * @param seconds 秒
     * @return
     */
    public static Date setDateTime(Date date, int hours, int minutes, int seconds) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /***
     * 当前天数加上小时
     *
     * @param plus 小时
     * @return date
     */
    public static Date getDateFromTodayByHour(int plus) {

        Calendar cal = Calendar.getInstance();
        Calendar calnextday = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) + plus, 0);
        return calnextday.getTime();
    }


    /**
     * 取得list中比默认值小的最小date,null认为最大,全比默认值大，返回默认值
     *
     * @param dateList 日期集合
     * @param defaultDate 默认值
     * @return date
     */
    public static Date getMinDateByList(List<Date> dateList, Date defaultDate) {
        Date tempDate = defaultDate;
        for (int i = 0; i < dateList.size(); i++) {
            if (dateList.get(i) == null) {
                continue;
            }
            if (dateList.get(i).getTime() < tempDate.getTime()) {
                tempDate = dateList.get(i);
            }
        }
        return tempDate;
    }

    /**
     * 两个日期相差天数
     *
     * @param date1 日期
     * @param date2 日期
     * @return 天数
     */
    public static int getDiffDays(Date date1, Date date2) {
        if ((date1.getTime() - date2.getTime()) % (1000 * 60 * 60 * 24) == 0) {
            return (int) (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24);
        } else {
            return (int) (date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24) - 1;
        }
    }

    /**
     * 两个日期相差小时数
     *
     * @param date1 日期
     * @param date2 日期
     * @return 小时数
     */
    public static int getHourDays(Date date1, Date date2) {
        if ((date1.getTime() - date2.getTime()) % (1000 * 60 * 60 * 24) == 0) {
            return (int) (date1.getTime() - date2.getTime()) / (1000 * 60 * 60);
        } else {
            return (int) (date1.getTime() - date2.getTime()) / (1000 * 60 * 60) - 1;
        }
    }

    /**
     * 两个日期相差分钟数
     *
     * @param date1 日期
     * @param date2 日期
     * @return 分钟数
     */
    public static long getSecondDays(Date date1, Date date2) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;

        long diff = date1.getTime() - date2.getTime();
        return diff % nd % nh / nm;
    }

    /***
     * 计算两个日期之间的天数
     *
     * @param early 日期
     * @param late  日期
     * @return int
     */
    public static int daysofTwo(Date early, Date late) {
        int count = 0;
        if (early == null || late == null) {
            return count;
        }
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        // 设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        // 得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
        return days;
    }


    /***
     * 日期比较返回最小日期
     *
     * @param date1 日期
     * @param date2 日期
     * @return date
     */
    public static Date minDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date1);
        int day1 = ca.get(Calendar.DAY_OF_YEAR);
        ca.setTime(date2);
        int day2 = ca.get(Calendar.DAY_OF_YEAR);
        return day1 > day2 ? date2 : date1;
    }


    /***
     * 比较日期大小，如果有个日期为空。返回false
     *
     * @param d1 日期
     * @param d2 日期
     * @return true/false
     */
    public static boolean compareDate(Date d1, Date d2) {
        if (d1 != null && d2 != null) {
            Calendar ca = Calendar.getInstance();
            ca.setTime(d1);
            int day1 = ca.get(Calendar.DAY_OF_YEAR);
            ca.setTime(d2);
            int day2 = ca.get(Calendar.DAY_OF_YEAR);
            if (day1 > day2) {
                return true;
            }
        }
        return false;
    }

    /***
     * 在当前日期上加多少月
     *
     * @param date   日期
     * @param months 需要加的月份
     * @return date
     */
    public static Date addDateMonths(Date date, int months) {
        if (date == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.MONTH, months);
        return ca.getTime();
    }


    /***
     * 在当前日期上加多少天
     *
     * @param date 日期
     * @param days 天数
     * @return date
     */
    public static Date addDateDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DAY_OF_YEAR, days);
        return ca.getTime();
    }


    /***
     * <p>在当前日期上减多少天 <p/>
     *
     * @param date 日期
     * @param days 相加的天数
     * @return date
     */
    public static Date delDateDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DAY_OF_YEAR, -days);
        return ca.getTime();
    }

    /***
     * 在当前日期上加多少小时
     *
     * @param date  日期
     * @param hours 小时
     * @return date
     */
    public static Date addDateHours(Date date, int hours) {
        if (date == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, hours);
        return ca.getTime();
    }

    /***
     * 在当前日期上加多少分钟
     *
     * @param date    日期
     * @param minutes 分钟
     * @return date
     */
    public static Date addDateMinutes(Date date, int minutes) {
        if (date == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.MINUTE, minutes);
        return ca.getTime();
    }

    /**
     * 在当前日期上加多少秒
     */
    /***
     * @param date     日期
     * @param senconds 秒
     * @return date
     */
    public static Date addDateSeconds(Date date, int senconds) {
        if (date == null) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.SECOND, senconds);
        return ca.getTime();
    }

    /***
     * 是否为日期类型
     *
     * @param s          字符串
     * @param formatType 格式
     * @return date
     */
    public static boolean isRightDate(String s, String formatType) {
        boolean b = true;
        SimpleDateFormat simpledateformat = new SimpleDateFormat(formatType);
        try {
            simpledateformat.parseObject(s);
        } catch (Exception ex) {
            b = false;
        }
        return b;
    }

    /***
     * 当前日期字符串
     *
     * @return String
     */
    public static String nowDateTime() {
        String s = "";
        try {
            Date date = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmmss");
            s = simpledateformat.format(date);
        } catch (Exception ex) {
            log.error("", ex);
        }
        return s;
    }

    public static String nowDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT8);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(date);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt 日期
     * @param isS 是否返回为String 格式
     * @return 当前日期是星期几
     */
    public static String weekOfDate(Date dt, boolean isS) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        if (isS) {
            return weekDays[w];
        }
        return w + "";
    }

    /**
     * 获取当前小时
     *
     * @param dt 日期
     * @return int
     */
    public static int hourOfDate(Date dt) {
        int hour = -1;
        if (dt != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            hour = cal.get(Calendar.HOUR_OF_DAY);
        }
        return hour;
    }

    /**
     * 获取当前分钟
     *
     * @param dt 日期
     * @return int
     */
    public static int minuteOfDate(Date dt) {
        int minute = -1;
        if (dt != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            minute = cal.get(Calendar.MINUTE);
        }
        return minute;
    }

    /**
     * 获取当前秒钟
     *
     * @param dt 日期
     * @return int
     */
    public static int secondOfDate(Date dt) {
        int second = -1;
        if (dt != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            second = cal.get(Calendar.SECOND);
        }
        return second;
    }

    /**
     * 返回当前时间是这一年中第几周
     *
     * @param dt 日期
     * @return int
     */
    public static int weekOfYear(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.WEEK_OF_YEAR);
        return w;
    }

    /**
     * 获得本周一0点时间
     *
     * @return date
     */
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 获得本周日24点时间
     *
     * @return date
     */
    public static Date getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    /**
     * 获得本月第一天0点时间
     *
     * @return date
     */
    public static Date getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }


    /**
     * 获得本月最后一天24点时间
     *
     * @return date
     */
    public static Date getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    /***
     * 日期是否在合法的范围内
     *
     * @param compareDate 待比较时间
     * @param startDate   起点时间
     * @param endDate     结束时间
     * @return true/false
     */
    public static boolean isBetween(Date compareDate, Date startDate, Date endDate) {
        return (compareDate.compareTo(startDate) >= 0 && compareDate.compareTo(endDate) <= 0);
    }

    /**
     * 时间戳转时间字符串
     *
     * @param time 时间戳
     * @return String
     */
    public static String getDateString(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT2);

        return sdf.format(time);
    }

    /**
     * 时间戳转时间字符串
     *
     * @param time 时间戳
     * @return String
     */
    public static String getDateString3(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT11);

        return sdf.format(time);
    }

    /**
     * 时间戳转时间字符串
     *
     * @param time 时间戳
     * @return String
     */
    public static String getDateString4(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT6);

        return sdf.format(time);
    }

    /**
     * 时间戳转时间字符串
     *
     * @param time 时间戳
     * @return String
     */
    public static String getDateString2(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);

        return sdf.format(time);
    }


    /**
     * 时间戳转时间字符串
     *
     * @param time 时间戳
     * @return String
     */
    public static String getDateString1(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT3);

        return sdf.format(time);
    }

    /***
     * 根据当前时间获取本周第一天0点
     *
     * @param time 时间
     * @return date
     */
    public static Date getTimesWeekmorning(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /***
     * 根据当前时间获取本周最后一天时间
     *
     * @param d 日期
     * @return date
     */
    public static Date getTimesWeeknight(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning(d));
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    /**
     * 递归取时间集合(yyyy-MM-dd)
     */
    public static ArrayList<String> getDateList(String dateTimeStar, String dateTimeStop, ArrayList<String> arrDate) {
        Calendar star = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date starDate = DateUtil.convert(dateTimeStar);
        Date stopDate = DateUtil.convert(dateTimeStop);
        star.setTime(stopDate);

        if (!starDate.equals(stopDate)) {
            arrDate.add(formatter.format(star.getTime()));
            star.add(Calendar.DATE, -1);
            getDateList(dateTimeStar, formatter.format(star.getTime()), arrDate);
        } else {
            arrDate.add(formatter.format(star.getTime()));
        }

        return arrDate;
    }

    /****
     * 时间转String
     * @param date 日期
     * @param format 格式
     * @return
     */
    public static String dateToString(Date date, String format) {
        if (date == null) {
            return null;
        }
        if (StringUtils.isEmpty(format)) {
            format = FORMAT1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /***
     * 两个日期是否年月相同
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMonth(Date date1, Date date2) {

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    //获取指定月份的天数
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据月份获取当前月份日期
     */
    public static List<Map<String, Object>> dayReport(Integer year, Integer modth) {
        Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
        Date d1 = null;
        try {
            d1 = new SimpleDateFormat("yyyy-MM").parse(year + "-" + modth);
        } catch (Exception e) {

        }
        List<Map<String, Object>> date = new ArrayList<Map<String, Object>>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        int dayNumOfMonth = DateUtil.getDaysByYearMonth(year, modth);
        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始
        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
            Date d = cal.getTime();
            String weeks = DateUtil.weekOfDate(d, true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String df = simpleDateFormat.format(d);
            paramMap.put(df, weeks);
        }
        date.add(paramMap);
        return date;
    }

    /**
     * 获取当天开始时间戳
     */
    public static Long getNowStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 获取当天结束时间时间戳
     */
    public static Long getNowEndTimes() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 23);
        todayStart.set(Calendar.MINUTE, 59);
        todayStart.set(Calendar.SECOND, 59);
        todayStart.set(Calendar.MILLISECOND, 999);
        return todayStart.getTime().getTime();
    }

    /**
     * 获取下一天开始时间戳
     */
    public static Long getNowEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.add(Calendar.DATE, 1);
        todayEnd.set(Calendar.HOUR_OF_DAY, 0);
        todayEnd.set(Calendar.MINUTE, 0);
        todayEnd.set(Calendar.SECOND, 0);
        todayEnd.set(Calendar.MILLISECOND, 0);
        return todayEnd.getTime().getTime();
    }

    /**
     * 获取当天开始日期时间
     */
    public static Date getNowStartDate() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 获取当天结束日期时间
     */
    public static Date getNowEndDate() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /**
     * 3个时间相差距离多少小时多少分多少秒
     *
     * @param time1 时间参数 1 格式：1990-01-01 12:00:00
     * @param time2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx小时xx分xx秒
     */
    public static String getDistanceTime(Long time1, Long time2, Integer m) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        if (Objects.nonNull(m)) {
            diff = m * 60 * 1000 - diff;
        }
        hour = (diff / (60 * 60 * 1000) - day * 24) < 0 ? 0 : (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60) < 0 ? 0 : ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60) < 0 ? 0 : (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return hour + "," + min + "," + sec;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param time1 时间参数 1 格式：1990-01-01 12:00:00
     * @param time2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDiffTime(Long time1, Long time2) {
        long l = time1 - time2;
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long sec = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return day + "," + hour + "," + min + "," + sec;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param time1 时间戳
     * @param time2 时间戳
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDiffTimeFormat(Long time1, Long time2) {
        long l = time1 - time2;
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long sec = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }

    public static int getSecond(int minute) {
        return minute * 60;
    }

    public static long getTimeMillis(String timeStr) {
        int sec;
        String[] my = timeStr.split(":");
        int hour = Integer.parseInt(my[0]);
        int min = Integer.parseInt(my[1]);
        if (my.length == 3) {
            sec = Integer.parseInt(my[2]);
        } else {
            sec = 0;
        }
        return (hour * 3600 + min * 60 + sec) * 1000;
    }


    /**
     * 返回时间
     */
    public static String getTimeStr(Date time) {
        if (time == null) {
            return "";
        }
        Date currentDate = new Date();
        Integer minute = DateUtil.getDiffMinutes2(currentDate, time);
        if (minute < 3) {
            return "刚刚";
        } else if (minute < 60) {
            return minute + "分钟前";
        } else {

            Integer hour = minute / 60;
            if (hour < 24) {
                return hour + "小时前";
            } else {

                Integer day = hour / 24;
                if (day < 30) {
                    return day + "天前";
                } else {

                    return "很久以前";
                }
            }
        }
    }
    /**
     * 返回分钟内时间
     */
    public static String getTimeStr2(Date time) {
        if (time == null) {
            return "";
        }
        Date currentDate = new Date();
        Integer minute = DateUtil.getDiffMinutes2(currentDate, time);
        if (minute < 1) {
            return "刚刚";
        } else if (minute < 10) {
            return minute + "分钟前";
        } else {
            return 10 + "分钟前";
        }
    }
    /**
     * 两个日期相差分钟数
     *
     * @param date1 日期
     * @param date2 日期
     * @return 小时数
     */
    public static int getDiffMinutes2(Date date1, Date date2) {
        long cha = date1.getTime() - date2.getTime();
        int minute = (int) (cha / 1000 / 60);
        return minute;
    }

    public static Long getTodayTime() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        return start.getTime();
    }

    public static Long getYestodayTime() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        return start.getTime() - 24 * 60 * 60 * 1000L;
    }

    public static String formatLongTime(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static String formatTime(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static Long getTime(int diff) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, diff);
        return calendar.getTime().getTime();
    }

    /**
     * 视屏时长转化 0时2分10秒转化2分10秒
     */
    public static String getVideoTime(String videoTime) {
        if (videoTime.startsWith("时") && !videoTime.startsWith("时分")) {
            return videoTime.substring(1);
        } else if (videoTime.startsWith("时分") || videoTime.startsWith("0时0分")) {
            int index = videoTime.lastIndexOf("分") + 1;
            return videoTime.substring(index);
        }else if (videoTime.startsWith("0时")) {
            return videoTime.substring(2);
        }else {
            return videoTime;
        }
    }

}
