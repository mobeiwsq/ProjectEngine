package com.mobeiwsq.engine_project.utils;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {
    public static final ThreadLocal<DateFormat> yyyyMMdd = new ThreadLocal<DateFormat>() {
        @SuppressLint({"SimpleDateFormat"})
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    public static final ThreadLocal<DateFormat> yyyyMMddNoSep = new ThreadLocal<DateFormat>() {
        @SuppressLint({"SimpleDateFormat"})
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };
    public static final ThreadLocal<DateFormat> HHmmss = new ThreadLocal<DateFormat>() {
        @SuppressLint({"SimpleDateFormat"})
        protected DateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };
    public static final ThreadLocal<DateFormat> HHmm = new ThreadLocal<DateFormat>() {
        @SuppressLint({"SimpleDateFormat"})
        protected DateFormat initialValue() {
            return new SimpleDateFormat("HH:mm");
        }
    };
    public static final ThreadLocal<DateFormat> yyyyMMddHHmmss = new ThreadLocal<DateFormat>() {
        @SuppressLint({"SimpleDateFormat"})
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    public static final ThreadLocal<DateFormat> yyyyMMddHHmmssNoSep = new ThreadLocal<DateFormat>() {
        @SuppressLint({"SimpleDateFormat"})
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };
    public static final ThreadLocal<DateFormat> yyyyMMddHHmm = new ThreadLocal<DateFormat>() {
        @SuppressLint({"SimpleDateFormat"})
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };
    public static final ThreadLocal<DateFormat> yyyyMMddHHmmssSSS = new ThreadLocal<DateFormat>() {
        @SuppressLint({"SimpleDateFormat"})
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        }
    };
    private static final int YEAR_S = 31536000;
    private static final int MONTH_S = 2592000;
    private static final int DAY_S = 86400;
    private static final int HOUR_S = 3600;
    private static final int MINUTE_S = 60;
    private static final String[] CHINESE_ZODIAC = new String[]{"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
    private static final int[] ZODIAC_FLAGS = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};
    private static final String[] ZODIAC = new String[]{"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

    private DateUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String millis2String(long millis, DateFormat format) {
        return date2String(new Date(millis), format);
    }

    public static String date2String(Date date, DateFormat format) {
        return format != null ? format.format(date) : "";
    }

    public static long string2Millis(String time, DateFormat format) {
        try {
            if (format != null) {
                return format.parse(time).getTime();
            }
        } catch (ParseException var3) {
            ParseException e = var3;
            e.printStackTrace();
        }

        return -1L;
    }

    public static Date string2Date(String time, DateFormat format) {
        try {
            if (format != null) {
                return format.parse(time);
            }
        } catch (ParseException var3) {
            ParseException e = var3;
            e.printStackTrace();
        }

        return null;
    }

    public static long date2Millis(Date date) {
        return date != null ? date.getTime() : -1L;
    }

    public static Date millis2Date(long millis) {
        return new Date(millis);
    }

    public static String translateDateFormat(String time, DateFormat oldFormat, DateFormat newFormat) {
        if (StringUtils.isSpace(time)) {
            return "";
        } else {
            Date date = string2Date(time, oldFormat);
            return date != null ? date2String(date, newFormat) : "";
        }
    }

    public static String translateDateFormat(String time, String oldFormatType, String newFormatType) {
        return translateDateFormat(time, (DateFormat)(new SimpleDateFormat(oldFormatType)), (DateFormat)(new SimpleDateFormat(newFormatType)));
    }

    public static boolean isDateFormatRight(String time, DateFormat format) {
        if (!StringUtils.isSpace(time) && format != null) {
            try {
                Date date = format.parse(time);
                String s = format.format(date);
                return time.equals(s);
            } catch (ParseException var4) {
                ParseException e = var4;
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    @Nullable
    public static String convertTimeToFileName(String dateTime, String suffix) {
        if (StringUtils.isSpace(dateTime)) {
            return null;
        } else {
            Pattern p = Pattern.compile("[^\\d]+");
            Matcher m = p.matcher(dateTime);
            return m.replaceAll("").trim() + suffix;
        }
    }

    public static Date getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static Date getStartOfDay(Date date, int dayAfter) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, dayAfter);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(14, 999);
        return cal.getTime();
    }

    public static Date getEndOfDay(Date date, int dayAfter) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(5, dayAfter);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(14, 999);
        return cal.getTime();
    }

    public static String nDaysBeforeToday(int day, boolean isNeedHMS) {
        return nDaysAfterToday(-day, isNeedHMS);
    }

    public static String nDaysAfterToday(int day, boolean isNeedHMS) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(System.currentTimeMillis());
        rightNow.add(5, day);
        return isNeedHMS ? date2String(rightNow.getTime(), (DateFormat)yyyyMMddHHmmss.get()) : date2String(rightNow.getTime(), (DateFormat)yyyyMMdd.get());
    }

    public static Date nDaysBeforeToday(int day) {
        return nDaysAfterToday(-day);
    }

    public static Date nDaysAfterToday(int day) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(System.currentTimeMillis());
        rightNow.add(5, day);
        return rightNow.getTime();
    }

    public static Date nMonthsBeforeToday(int month) {
        return nMonthsAfterToday(-month);
    }

    public static Date nMonthsAfterToday(int month) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(System.currentTimeMillis());
        rightNow.add(2, month);
        return rightNow.getTime();
    }

    public static long getTimeSpan(String time0, String time1, DateFormat format, int unit) {
        return millis2TimeSpan(Math.abs(string2Millis(time0, format) - string2Millis(time1, format)), unit);
    }

    public static long getTimeSpan(Date date0, Date date1, int unit) {
        return millis2TimeSpan(Math.abs(date2Millis(date0) - date2Millis(date1)), unit);
    }

    public static long getTimeSpan(long millis0, long millis1, int unit) {
        return millis2TimeSpan(Math.abs(millis0 - millis1), unit);
    }

    public static String getFitTimeSpan(String time0, String time1, DateFormat format, int precision) {
        long delta = string2Millis(time0, format) - string2Millis(time1, format);
        return millis2FitTimeSpan(Math.abs(delta), precision);
    }

    public static String getFitTimeSpan(Date date0, Date date1, int precision) {
        return millis2FitTimeSpan(Math.abs(date2Millis(date0) - date2Millis(date1)), precision);
    }

    public static String getFitTimeSpan(long millis0, long millis1, int precision) {
        return millis2FitTimeSpan(Math.abs(millis0 - millis1), precision);
    }

    public static long getTimeSpanByNow(String time, DateFormat format, int unit) {
        return getTimeSpan(getNowString(format), time, format, unit);
    }

    public static long getTimeSpanByNow(Date date, int unit) {
        return getTimeSpan(new Date(), date, unit);
    }

    public static long getTimeSpanByNow(long millis, int unit) {
        return getTimeSpan(System.currentTimeMillis(), millis, unit);
    }

    public static String getFitTimeSpanByNow(String time, DateFormat format, int precision) {
        return getFitTimeSpan(getNowString(format), time, format, precision);
    }

    public static String getFitTimeSpanByNow(Date date, int precision) {
        return getFitTimeSpan(getNowDate(), date, precision);
    }

    public static String getFitTimeSpanByNow(long millis, int precision) {
        return getFitTimeSpan(System.currentTimeMillis(), millis, precision);
    }

    public static String getFriendlyTimeSpanByNow(String time, DateFormat format) {
        return getFriendlyTimeSpanByNow(string2Millis(time, format));
    }

    public static String getFriendlyTimeSpanByNow(Date date) {
        return getFriendlyTimeSpanByNow(date.getTime());
    }

    public static String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0L) {
            return String.format("%tc", millis);
        } else if (span < 1000L) {
            return "刚刚";
        } else if (span < 60000L) {
            return String.format(Locale.getDefault(), "%d秒前", span / 1000L);
        } else if (span < 3600000L) {
            return String.format(Locale.getDefault(), "%d分钟前", span / 60000L);
        } else {
            long wee = getWeeOfToday();
            if (millis >= wee) {
                return String.format("今天%tR", millis);
            } else {
                return millis >= wee - 86400000L ? String.format("昨天%tR", millis) : String.format("%tF", millis);
            }
        }
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(11, 0);
        cal.set(13, 0);
        cal.set(12, 0);
        cal.set(14, 0);
        return cal.getTimeInMillis();
    }

    public static String getFuzzyTimeDescriptionByNow(String time, DateFormat format) {
        return getFuzzyTimeDescriptionByNow(string2Millis(time, format));
    }

    public static String getFuzzyTimeDescriptionByNow(Date date) {
        return getFuzzyTimeDescriptionByNow(date.getTime());
    }

    public static String getFuzzyTimeDescriptionByNow(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000L;
        String timeStr;
        long span;
        if ((span = (long)Math.round((float)timeGap / 3.1536E7F)) > 0L) {
            timeStr = span + "年前";
        } else if ((span = (long)Math.round((float)timeGap / 2592000.0F)) > 0L) {
            timeStr = span + "个月前";
        } else if ((span = (long)Math.round((float)timeGap / 86400.0F)) > 0L) {
            timeStr = span + "天前";
        } else if ((span = (long)Math.round((float)timeGap / 3600.0F)) > 0L) {
            timeStr = span + "小时前";
        } else if ((span = (long)Math.round((float)timeGap / 60.0F)) > 0L) {
            timeStr = span + "分钟前";
        } else {
            timeStr = "刚刚";
        }

        return timeStr;
    }

    private static long timeSpan2Millis(long timeSpan, int unit) {
        return timeSpan * (long)unit;
    }

    private static long millis2TimeSpan(long millis, int unit) {
        return millis / (long)unit;
    }

    private static String millis2FitTimeSpan(long millis, int precision) {
        if (millis >= 0L && precision > 0) {
            precision = Math.min(precision, 5);
            String[] units = new String[]{"天", "小时", "分钟", "秒", "毫秒"};
            if (millis == 0L) {
                return 0 + units[precision - 1];
            } else {
                StringBuilder sb = new StringBuilder();
                int[] unitLen = new int[]{86400000, 3600000, 60000, 1000, 1};

                for(int i = 0; i < precision; ++i) {
                    if (millis >= (long)unitLen[i]) {
                        long mode = millis / (long)unitLen[i];
                        millis -= mode * (long)unitLen[i];
                        sb.append(mode).append(units[i]);
                    }
                }

                return sb.toString();
            }
        } else {
            return null;
        }
    }

    public static int getAgeByBirthDay(String birthDay, DateFormat format) throws IllegalArgumentException {
        return getAgeByBirthDay(string2Date(birthDay, format));
    }

    public static int getAgeByBirthDay(Date birthDay) throws IllegalArgumentException {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        } else {
            int yearNow = cal.get(1);
            int monthNow = cal.get(2);
            int dayNow = cal.get(5);
            cal.setTime(birthDay);
            int yearBirth = cal.get(1);
            int monthBirth = cal.get(2);
            int dayBirth = cal.get(5);
            int age = yearNow - yearBirth;
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayNow < dayBirth) {
                        --age;
                    }
                } else {
                    --age;
                }
            }

            return age;
        }
    }

    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    public static String getNowString(DateFormat format) {
        return millis2String(System.currentTimeMillis(), format);
    }

    public static Date getNowDate() {
        return new Date();
    }

    public static boolean isToday(String time, DateFormat format) {
        return isToday(string2Millis(time, format));
    }

    public static boolean isToday(Date date) {
        return isToday(date.getTime());
    }

    public static boolean isToday(long millis) {
        long wee = getWeeOfToday();
        return millis >= wee && millis < wee + 86400000L;
    }

    public static int getWeekIndex(String time, DateFormat format) {
        return getWeekIndex(string2Date(time, format));
    }

    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(1);
    }

    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(2) + 1;
    }

    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(5);
    }

    public static int getWeekIndex(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(7);
    }

    public static int getWeekIndex(long millis) {
        return getWeekIndex(millis2Date(millis));
    }

    public static String getChineseZodiac(String time, DateFormat format) {
        return getChineseZodiac(string2Date(time, format));
    }

    public static String getChineseZodiac(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return CHINESE_ZODIAC[cal.get(1) % 12];
    }

    public static String getChineseZodiac(long millis) {
        return getChineseZodiac(millis2Date(millis));
    }

    public static String getChineseZodiac(int year) {
        return CHINESE_ZODIAC[year % 12];
    }

    public static String getZodiac(String time, DateFormat format) {
        return getZodiac(string2Date(time, format));
    }

    public static String getZodiac(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        return getZodiac(month, day);
    }

    public static String getZodiac(long millis) {
        return getZodiac(millis2Date(millis));
    }

    public static String getZodiac(int month, int day) {
        return ZODIAC[day >= ZODIAC_FLAGS[month - 1] ? month - 1 : (month + 10) % 12];
    }
}

