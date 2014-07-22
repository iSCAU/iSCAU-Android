package cn.scau.scautreasure;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import java.util.Calendar;
import java.util.List;

import cn.scau.scautreasure.helper.ClassHelper_;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.util.ClassUtil;
import cn.scau.scautreasure.util.DateUtil;

/**
 * 情景模式，顺序请于arrays.xml中的ringer_mode保持一致<br/>
 * Created by robust on 14-4-7.
 */
public enum RingerMode {
    /**
     * 未设置
     */
    NOT_SET(-1),
    /**
     * 正常模式（声音、震动开启）
     */
    NORMAL(AudioManager.RINGER_MODE_NORMAL),
    /**
     * 震动模式
     */
    VIBRATE(AudioManager.RINGER_MODE_VIBRATE),
    /**
     * 静音
     */
    SILENT(AudioManager.RINGER_MODE_SILENT);

    private final int value;

    private RingerMode(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static final boolean isSet(int value){
        return value != -1;
    }

    public static RingerMode getModeByValue(int value){
        for(RingerMode mode : RingerMode.values()){
            if(mode.getValue() == value){
                return mode;
            }
        }
        return NOT_SET;
    }

    /**
     * 设置上课时的情景模式
     * @param context
     * @param mode
     * @param  offsetMinute 上课前触发的时间
     */
    public static final void duringClassOn(Context context, RingerMode mode, int offsetMinute){
        int value = mode.getValue();
        ClassHelper_ helper = ClassHelper_.getInstance_(context);
        List<ClassModel> klasses = helper.getDayLessonWithParams(new DateUtil().getDayOfWeek());
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AppConstant.ACTION_RINGER_MODE_ALARM_DURING);
        PendingIntent pendIntent;
        Calendar c = Calendar.getInstance();
        int node;
        for(ClassModel classModel : klasses){
            node = Integer.parseInt(classModel.getNode().split(",")[0]);
            pendIntent = PendingIntent.getBroadcast(context,
                    node, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if(isSet(value)){
                ClassUtil.genClassBeginTime(c, node);
                c.add(Calendar.MINUTE, offsetMinute);
                alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendIntent);
            } else {
                alarmMgr.cancel(pendIntent);
            }
        }
    }

    /**
     * 设置下课后的情景模式
     * @param context
     * @param mode
     * @param offsetMinute 下课后触发的时间
     */
    public static final void afterClassOn(Context context, RingerMode mode, int offsetMinute){
        int value = mode.getValue();
        ClassHelper_ helper = ClassHelper_.getInstance_(context);
        List<ClassModel> klasses = helper.getDayLessonWithParams(new DateUtil().getDayOfWeek());
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AppConstant.ACTION_RINGER_MODE_ALARM_AFTER);
        PendingIntent pendIntent;
        Calendar c = Calendar.getInstance();
        int node;
        String[] nodes;
        for(ClassModel classModel : klasses){
            nodes = classModel.getNode().split(",");
            node = Integer.parseInt(nodes[nodes.length - 1]);
            //节次作为reqCode
            pendIntent = PendingIntent.getBroadcast(context,
                    node, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            if(isSet(value)){
                ClassUtil.genClassOverTime(c, node);
                c.add(Calendar.MINUTE, offsetMinute);
                alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendIntent);
            } else {
                alarmMgr.cancel(pendIntent);
            }
        }
    }

    /**
     * 设置每天0:00触发的闹钟
     */
    public static void setDateChangedAlarm(Context context){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AppConstant.ACTION_DATE_CHANGED);
        PendingIntent pendIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), CacheUtil.TIME_DAY * 1000, pendIntent);
    }

    /**
     * 取消每天0:00触发的闹钟
     * @param context
     */
    public static void cancelDateChangedAlarm(Context context){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AppConstant.ACTION_DATE_CHANGED);
        PendingIntent pendIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(pendIntent);
    }
}
