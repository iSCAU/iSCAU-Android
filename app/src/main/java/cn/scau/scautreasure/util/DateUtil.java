package cn.scau.scautreasure.util;

import org.androidannotations.annotations.EBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: special
 * Date: 13-8-27
 * Time: 下午11:21
 * Mail: specialcyci@gmail.com
 */
@EBean
public class DateUtil {

    public Date parseDate(String date) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            return f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public String parseCardQueryDate(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        return f.format(date);
    }

    /* 获得当前时间 */
    public String getCurrentTime() {
        Date currentTime = new Date();
        String TimeString = currentTime.getHours() + ":"
                + currentTime.getMinutes();
        return TimeString;
    }

    /**
     * 获得 星期几 的数字编码
     *
     * @return
     */
    public int getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == 1 ? 7 : dayOfWeek - 1;
    }

    /* 获得当前日期 */
    public String getCurrentDateString() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        return (sf.format(date));
    }

    public Date getCurrentDate() {
        return new Date();
    }

    /**
     * get the current month - date;
     *
     * @return
     */
    public String getCurrentMonthDate() {
        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日");
        Date date = new Date();
        return (sf.format(date));
    }

    public String getCurrentYearMonth() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return (sf.format(date));
    }

    public String getCurrentDay() {

        SimpleDateFormat sf = new SimpleDateFormat("dd");
        Date date = new Date();
        return (sf.format(date));
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @return 当前日期是星期几
     */
    public String getWeekOfDate() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 获取当前日期是星期几(单字)<br>
     *
     * @return 当前日期是星期几
     */
    public String getShortWeekOfDate() {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /* 根据当前日期与开学时间获得周 */
    public int dateToSchoolWeek(String date, String termStartDate) {
        try {
            return dateDiff(termStartDate, date,
                    "yyyy-MM-dd");
        } catch (java.text.ParseException e) {
//            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 判断单双周;
     *
     * @param schoolWeek
     *
     * @return
     */
    public String judgeDsz(int schoolWeek) {
        String strNowDsz;
        int intNowDSZ = schoolWeek % 2;
        switch (intNowDSZ) {
            case 0:
                strNowDsz = "双";
                break;

            default:
                strNowDsz = "单";
        }
        return strNowDsz;
    }

    /* 计算周差 */
    public int dateDiff(String startTime, String endTime, String format)
            throws java.text.ParseException {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60; // 一天的毫秒数
        long nh = 1000 * 60 * 60; // 一小时的毫秒数
        long nm = 1000 * 60; // 一分钟的毫秒数
        long ns = 1000; // 一秒钟的毫秒数
        long diff;

        try {
            // 获得两个时间的毫秒时间差异
            // -nd 减去一天;
            diff = sd.parse(endTime).getTime() + nd - sd.parse(startTime).getTime();
            long day = diff / nd; // 计算差多少天

            // System.out.println("day:" + day / 7.0);

            return ((int) Math.ceil(day / 7.0)) > 0 ? ((int) Math.ceil(day / 7.0)) : 0;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 中文日期到数字;
     *
     * @param day
     *
     * @return
     */
    public int chineseToNumDay(String day) {

        if (day.equals("一")) return 0;
        if (day.equals("二")) return 1;
        if (day.equals("三")) return 2;
        if (day.equals("四")) return 3;
        if (day.equals("五")) return 4;
        if (day.equals("六")) return 5;
        if (day.equals("日")) return 6;
        return 0;

    }

    /**
     * 数字星期转换成中文日期;
     *
     * @param day
     *
     * @return
     */
    public String numDayToChinese(int day) {

        String cday = null;
        switch (day) {
            case 1:
                cday = "一";
                break;

            case 2:
                cday = "二";
                break;

            case 3:
                cday = "三";
                break;

            case 4:
                cday = "四";
                break;

            case 5:
                cday = "五";
                break;

            case 6:
                cday = "六";
                break;

            case 7:
                cday = "日";
                break;

            default:
                break;
        }
        return cday;
    }


}
