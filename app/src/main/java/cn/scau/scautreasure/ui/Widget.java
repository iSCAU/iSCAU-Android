package cn.scau.scautreasure.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.AppContext_;
import cn.scau.scautreasure.WidgetProvider;
import cn.scau.scautreasure.helper.WidgetHelper;
import cn.scau.scautreasure.helper.WidgetHelper_;

/**
 * 桌面小插件控制类
 * <p/>
 * User: special
 * Date: 13-9-10
 * Time: 下午8:23
 * Mail: specialcyci@gmail.com
 */
public class Widget extends AppWidgetProvider {

    private AppContext app;
    private AlarmManager alarmManager;
    private WidgetHelper widgetHelper;
    private Context context;

    private void setUpWidgetContext(Context context) {
        this.context = context;
        alarmManager = ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE));
        app = AppContext_.getInstance();
        widgetHelper = WidgetHelper_.getInstance_(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        setUpWidgetContext(context);
        startUpdateTimer();
        app.Log("intent: " + intent.getAction());
        if (intent.getAction().equals(AppConstant.INTENT_MONDAY)) {
            widgetHelper.showDayClassTableView(1);
        } else if (intent.getAction().equals(AppConstant.INTENT_TUESDAY)) {
            widgetHelper.showDayClassTableView(2);
        } else if (intent.getAction().equals(AppConstant.INTENT_WEDNESDAY)) {
            widgetHelper.showDayClassTableView(3);
        } else if (intent.getAction().equals(AppConstant.INTENT_THURDAY)) {
            widgetHelper.showDayClassTableView(4);
        } else if (intent.getAction().equals(AppConstant.INTENT_FRIDAY)) {
            widgetHelper.showDayClassTableView(5);
        } else if (intent.getAction().equals(AppConstant.INTENT_SATURDAY)) {
            widgetHelper.showDayClassTableView(6);
        } else if (intent.getAction().equals(AppConstant.INTENT_SUNDAY)) {
            widgetHelper.showDayClassTableView(7);
        } else if (intent.getAction().equals(AppConstant.INTENT_SETTINGS)) {
            startSettingsActivity();
        } else {
            widgetHelper.setUpViews();
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        setUpWidgetContext(context);
        startUpdateTimer();
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        setUpWidgetContext(context);
        stopUpdateTimer();
        super.onDisabled(context);
    }

    private void startSettingsActivity() {
        WidgetConfiguration_.intent(context).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
    }

    private void startUpdateTimer() {
        if (isUpdateTimerRunning()) return;
        long firstime = SystemClock.elapsedRealtime();
        long interval = AppConstant.WIDGET_UPDATE_INTERVAL;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
                interval, getUpdateTimer());
    }

    private PendingIntent getUpdateTimer() {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(AppConstant.INTENT_UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void stopUpdateTimer() {
        alarmManager.cancel(getUpdateTimer());
    }

    private boolean isUpdateTimerRunning() {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(AppConstant.INTENT_UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

//    onDeleted(Context context, int[] appWidgetIds) 删除App Widget是调用此方法
//    onDisabled(Context context) 最后一个App Widget实例删除后调用此方法
//    onEnabled(Context context) App WIdget实例第一次被创建是调用此方法
//    onReceive(Context context, Intent intent) 接收广播事件
//    onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 到达指定更新时间或用户向桌面添加了App Widget时调用此方法

}
