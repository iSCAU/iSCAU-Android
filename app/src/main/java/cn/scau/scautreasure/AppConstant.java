package cn.scau.scautreasure;

import cn.scau.scautreasure.util.CacheUtil;

/**
 * User: special
 * Date: 13-9-8
 * Time: 下午5:07
 * Mail: specialcyci@gmail.com
 */
public class AppConstant {


    /**
     * 更新服务器上应用程序编号;
     */
    public static final int APPLICATION_ID = 1;

    /**
     * 更新服务器地址;
     */
    public static final String UPDATE_SERVER_ADDRESS = "http://42.121.131.125:3000/";

    /**
     * 教务系统的参数选择缓存有效时间;
     */
    public static final int PARAMS_CACHE_TIME   = 7 * CacheUtil.TIME_DAY;

    /**
     * 巴士线路信息的缓存有效时间;
     */
    public static final int BUS_LINE_CACHE_TIME = 7 * CacheUtil.TIME_DAY;

    /**
     * 巴士站点信息的缓存有效时间;
     */
    public static final int BUS_SITE_CACHE_TIME = 7 * CacheUtil.TIME_DAY;

    /**
     * 桌面小插件刷新时间
     */
    public static final long WIDGET_UPDATE_INTERVAL = (long) (0.5 * CacheUtil.TIME_HOUR) * 1000;

    /**
     * 彩虹色配色;
     */
    public static final int[] IV_COLOR = new int[]{
            0xff70a8fb,
            0xffff8be8,
            0xffff7272,
            0xff81e895,
            0xfff8fe52
    };

    /**
     * 桌面小插件的Intent;
     */
    public static final String INTENT_CONFIGURE   = "android.appwidget.action.APPWIDGET_CONFIGURE";
    public static final String INTENT_SETTINGS    = "cn.scau.scautreasure.SETTINGS";
    public static final String INTENT_UPDATE      = "cn.scau.scautreasure.RECEIVER_UPDATE";
    public static final String INTENT_MONDAY      = "cn.scau.scautreasure.MONDAY";
    public static final String INTENT_TUESDAY     = "cn.scau.scautreasure.TUESDAY";
    public static final String INTENT_WEDNESDAY   = "cn.scau.scautreasure.WEDNESDAY";
    public static final String INTENT_THURDAY     = "cn.scau.scautreasure.THURDAY";
    public static final String INTENT_FRIDAY      = "cn.scau.scautreasure.FRIDAY";
    public static final String INTENT_SATURDAY    = "cn.scau.scautreasure.SATURDAY";
    public static final String INTENT_SUNDAY      = "cn.scau.scautreasure.SUNDAY";

    /**
     * 更改上课情景模式
     */
    public static final String ACTION_RINGER_MODE_ALARM_DURING = "cn.scau.scautreasure.RINGER_MODE_ALARM_DURING";
    /**
     * 更改下课情景模式
     */
    public static final String ACTION_RINGER_MODE_ALARM_AFTER = "cn.scau.scautreasure.RINGER_MODE_ALARM_AFTER";
    /**
     * 0:00闹钟广播
     */
    public static final String ACTION_DATE_CHANGED = "cn.scau.scautreasure.DATE_CHANGED";

    public static final String LOG_TAG = "scautreasure";
    public static final String LOG_FILE_NAME = "scautreasure/log.txt";

}
