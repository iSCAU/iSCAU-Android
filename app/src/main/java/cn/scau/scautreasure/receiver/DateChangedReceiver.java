package cn.scau.scautreasure.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.scau.scautreasure.RingerMode;
import cn.scau.scautreasure.util.AlertClassUtil;

/**
 * 日期变化广播接收器<br/>
 * 包括从23:59到次日0:00的日期变化和手动修改时间引起的日期变化
 * Created by robust on 14-4-9.
 */
public class DateChangedReceiver extends BroadcastReceiver {
    cn.scau.scautreasure.AppConfig_ config;

    @Override
    public void onReceive(Context context, Intent intent) {
        config = new cn.scau.scautreasure.AppConfig_(context);

        if(config.isAlertClass().get()) { //如果启动提醒
            AlertClassUtil.startAlertClass(context);
        }

        RingerMode duringMode = RingerMode.getModeByValue(config.duringClassRingerMode().get());
        RingerMode afterMode = RingerMode.getModeByValue(config.afterClassRingerMode().get());
        if (!RingerMode.isSet(duringMode.getValue()) && !RingerMode.isSet(afterMode.getValue())) {
            return;
        }
        RingerMode.duringClassOn(context, duringMode, -1);
        RingerMode.afterClassOn(context, afterMode, 1);


    }
}
