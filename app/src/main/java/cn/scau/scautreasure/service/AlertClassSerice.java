package cn.scau.scautreasure.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Calendar;
import java.util.List;

import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.receiver.AlertClassReceiver_;
import cn.scau.scautreasure.util.ClassUtil;
import cn.scau.scautreasure.util.DateUtil;

/**
 * Created by apple on 14-8-31.
 */
/*
1、	在原有课表的基础上，增加一个课程提醒的功能，在每节课上课前30分钟，在通知栏里面提醒，内容：你即将要上的课是：xxxx(课名)，地点:xxx。
要求是：
			1、要用同一个notification，不要多个notification在通知栏叠加。
			2、这个notification不闪光，但震动提示，并且允许用户在通知栏清除。
			3、在设置页面添加一个选项：是否开启上课提醒，默认是开。当用户选择关时，这个功能就关闭。√

 */
@EService
public class AlertClassSerice extends Service{
    //当天的课程ListView
    private   List<ClassModel> dayClassList = null;
    private  int currentDay ;
    private  String chineseDay;
    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @Bean
    ClassHelper classHelper;
    @Bean
    DateUtil dateUtil;
    //30分钟前触发
    private int offsetMinute=-30;
    private String time="";

    @Override
    public void onCreate() {
        super.onCreate();

        currentDay =  dateUtil.getDayOfWeek();
          chineseDay = dateUtil.numDayToChinese(currentDay);

        dayClassList = classHelper.getDayLessonWithParams(chineseDay);
        if(config.isAlertClass().get()) {
            if (dayClassList.size() > 0) {
                for (int i = 0; i < dayClassList.size(); i++) {
                    Log.i("今天课程:", "第" + dayClassList.get(i).getNode() + "节--" + dayClassList.get(i).getClassname());
                    startAlert(dayClassList.get(i).getClassname(), dayClassList.get(i).getLocation(), dayClassList.get(i).getNode(), i);

                }
            }
        }
        stopSelf();
    }

    /**
     *
     * @param className
     * @param node  节次
     */
    void startAlert(String className,String classBlock,String node,int id){
            long triggerAtTime=countTime(node);
        Calendar c_cur = Calendar.getInstance();
        //只添加未来的课
        if(c_cur.getTimeInMillis()<triggerAtTime) {
            Intent intent = new Intent(getApplicationContext(), AlertClassReceiver_.class);
            intent.putExtra("className", className);
            intent.putExtra("classTime", time);
            intent.putExtra("classBlock", classBlock);

            PendingIntent pendIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Log.i("课程提醒", node + "|" + className + "|" + time);
            am.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pendIntent);

        }

    }

    long countTime(String node){
        int  startNode= Integer.valueOf(node.split(",")[0]);
        Calendar c = Calendar.getInstance();
        time =ClassUtil.genClassBeginTime(c, startNode);

         c.add(Calendar.MINUTE, offsetMinute);
        return c.getTimeInMillis();
//        alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendIntent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("AlertClassService","开启");
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("AlertClassService","关闭");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
