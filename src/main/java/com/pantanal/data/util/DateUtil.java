package com.pantanal.data.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    public static final long DAYMILLI = 24 * 60 * 60 * 1000;
    public static final long MINUTEMILLI = 60 * 1000;
    public static final int DAY_MINUTE = 24 * 60;
    /**
     * 时间格式 "yyyyMMddHHmmss"
     */
    public static final String DATE_FMT_1 = "yyyyMMddHHmmss";
    /**
     * 时间格式 "yyyy-MM-dd HH:mm:ss"
     */
    public static final String DATE_FMT_2 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式 "yyyy-MM-dd"
     */
    public static final String DATE_FMT_3 = "yyyy-MM-dd";
    /**
     * 时间格式 "HH:mm:ss"
     */
    public static final String DATE_FMT_4 = "HH:mm:ss";
    /**
     * 时间格式 "yyyy.MM.dd-HH:mm:ss"
     */
    public static final String DATE_FMT_5 = "yyyy.MM.dd-HH:mm:ss";
    /**
     * 时间格式 "yyyyMMdd"
     */
    public static final String DATE_FMT_6 = "yyyyMMdd";

    /**
     * 将一定格式的字符串转化成Date类型
     *
     * @param dateStr
     * @param sFmt
     * @return
     */
    public static Date toDate(String dateStr, String sFmt) {
        SimpleDateFormat sdfFrom = null;
        Date date;

        try {
            sdfFrom = new SimpleDateFormat(sFmt);
            date = sdfFrom.parse(dateStr);
        } catch (Exception e) {
            return null;
        } finally {
            sdfFrom = null;
        }

        return date;
    }

    /**
     * 将Date类型按照要求的格式转换成字符串
     *
     * @param date
     * @param sFmt
     * @return
     */
    public static String toString(Date date, String sFmt) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat sdfFrom = null;
        String sRet = null;

        try {
            sdfFrom = new SimpleDateFormat(sFmt);
            sRet = sdfFrom.format(date).toString();
        } catch (Exception e) {
            return "";
        } finally {
            sdfFrom = null;
        }

        return sRet;
    }

    /**
     * 根据某个格式判断是否是时间格式
     *
     * @param str
     * @param format
     * @return
     */
    public static boolean isDate(String str, String format) {
        if (str == null) {
            return false;
        }

        Date date = toDate(str, format);
        if (date == null) {
            return false;
        } else {
            return str.equals(toString(date, format));
        }
    }

    /**
     * 转换时间格式，将一种时间格式的字符串转换成另一种时间格式的字符串
     *
     * @param sDate
     * @param sFmtFrom
     * @param sFmtTo
     * @return
     */
    public static String formatDate(String sDate, String sFmtFrom, String sFmtTo) {
        SimpleDateFormat sdfFrom = null;
        SimpleDateFormat sdfTo = null;
        Date dt;

        if (sDate == null) {
            return sDate;
        }

        try {
            sdfFrom = new SimpleDateFormat(sFmtFrom);
            dt = sdfFrom.parse(sDate);

            sdfTo = new SimpleDateFormat(sFmtTo);

            return sdfTo.format(dt);

        } catch (Exception e) {
            return sDate;
        }
    }

    /**
     * 返回两个时间间隔的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int subDateDays(Date date1, Date date2) {
        int days;
        days = (int) (Math.abs(date2.getTime() - date1.getTime()) / 86400000);
        return days;
    }

    /**
     * 增加天数
     *
     * @param fromDate
     * @param day
     * @return
     */
    public static Date addDateDays(Date fromDate, int day) {
        if (fromDate == null) {
            return fromDate;
        }

        if (day == 0) {
            return fromDate;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_1);
        Calendar calendar = dateFormat.getCalendar();
        calendar.setTime(fromDate);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 增加天数
     *
     * @param dateString
     * @param day
     * @return 格式yyyyMMddHHmmss的时间
     *
     */
    public static String addDays(String dateString, int day) {
        Date date = null;
        if (isDate(dateString, DATE_FMT_1)) {
            date = toDate(dateString, DATE_FMT_1);
        } else if (isDate(dateString, DATE_FMT_2)) {
            date = toDate(dateString, DATE_FMT_2);
        } else if (isDate(dateString, DATE_FMT_3)) {
            date = toDate(dateString, DATE_FMT_3);
        } else if (isDate(dateString, DATE_FMT_4)) {
            date = toDate(dateString, DATE_FMT_4);

        }

        if (dateString == null || "".equals(dateString)) {
            return dateString;
        }

        if (day == 0) {
            return dateString;
        }

        return addDays(date, day);
    }

    /**
     * 增加天数
     *
     * @param date
     * @param day
     * @return 格式yyyyMMddHHmmss的时间
     */
    public static String addDays(Date date, int day) {
        String dateString;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_1);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);

            dateString = dateFormat.format(calendar.getTime());

            return dateString;
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 增加小时数，格式yyyyMMddHHmmss
     *
     * @param date
     * @param hour
     * @return
     */
    public static String addHours(String date, int hour) {
        if (date == null || "".equals(date)) {
            return date;
        }

        if (hour == 0) {
            return date;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_1);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(dateFormat.parse(date));

            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);
            date = dateFormat.format(calendar.getTime());
            return date;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 增加分钟数，格式yyyyMMddHHmmss
     *
     * @param date
     * @param minute
     * @return
     */
    public static String addMinutes(String date, int minute) {
        if (date == null || "".equals(date)) {
            return date;
        }

        if (minute == 0) {
            return date;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FMT_1);
            Calendar calendar = dateFormat.getCalendar();
            calendar.setTime(dateFormat.parse(date));

            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
            date = dateFormat.format(calendar.getTime());
            return date;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 计算两个时间之间相差的天数
     *
     * @param fromDate
     *            时间格式 "yyyyMMddHHmmss"
     * @param endDate
     *            时间格式 "yyyyMMddHHmmss"
     * @return
     */
    public static short countDays(String fromDate, String endDate) {
        Timestamp t1 = Timestamp.valueOf(formatDate(fromDate, DATE_FMT_1, DATE_FMT_2));
        Timestamp t2 = Timestamp.valueOf(formatDate(endDate, DATE_FMT_1, DATE_FMT_2));
        return new Long((t2.getTime() - t1.getTime()) / DAYMILLI).shortValue();
    }

    /**
     * 计算两个时间之间相差的分钟数
     *
     * @param fromDate
     *            时间格式 "yyyyMMddHHmmss"
     * @param endDate
     *            时间格式 "yyyyMMddHHmmss"
     * @return
     */
    public static long countMinutes(String fromDate, String endDate) {
        Timestamp t1 = Timestamp.valueOf(formatDate(fromDate, DATE_FMT_1, DATE_FMT_2));
        Timestamp t2 = Timestamp.valueOf(formatDate(endDate, DATE_FMT_1, DATE_FMT_2));
        return new Long((t2.getTime() - t1.getTime()) / MINUTEMILLI + 1).longValue();
    }

    /**
     * 获取现在时间,时间格式 "yyyyMMddHHmmss"
     *
     * @return
     */
    public static String timeNow() {
        long currentTime = System.currentTimeMillis();
        Calendar ca = new GregorianCalendar();
        ca.setTimeInMillis(currentTime);
        Date optDate = ca.getTime();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FMT_1);
        String strOptDate = dbDateFormat.format(optDate);
        return strOptDate;
    }

    /**
     * 获取现在时间,时间格式 "yyyy.MM.dd-HH:mm:ss"
     *
     * @return
     */
    public static String timeNowString() {
        long currentTime = System.currentTimeMillis();
        Calendar ca = new GregorianCalendar();
        ca.setTimeInMillis(currentTime);
        Date optDate = ca.getTime();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FMT_5);
        String strOptDate = dbDateFormat.format(optDate);
        return strOptDate;
    }

    /**
     * 获取当前时间 ，Date类型
     *
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 在某个时间上增加现在的时分秒
     *
     * @param optDate
     * @return
     */
    public static String addNowTime(Date optDate) {
        return toString(optDate, DATE_FMT_3) + " " + formatDate(timeNow(), DATE_FMT_1, DATE_FMT_4);
    }

    /**
     * 时间比较，time2是否晚于time1
     *
     * @param time1
     *            时间格式 "yyyyMMddHHmmss"
     * @param time2
     *            时间格式 "yyyyMMddHHmmss"
     * @return
     */
    public static boolean timeCompare(String time1, String time2) {
//        System.out.println(time1 + "  ======  " + time2 );
        Timestamp t1 = Timestamp.valueOf(formatDate(time1, DATE_FMT_1, DATE_FMT_2));
        Timestamp t2 = Timestamp.valueOf(formatDate(time2, DATE_FMT_1, DATE_FMT_2));
        return t2.getTime() > t1.getTime();
    }

    /**
     * 判定时间是否早于现在
     *
     * @param date
     * @return
     */
    public static boolean isHistoryTime(Date date) {
        return date.getTime() <= System.currentTimeMillis();
    }
}
