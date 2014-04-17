package cn.scau.scautreasure.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import cn.scau.scautreasure.RingerMode;
import cn.scau.scautreasure.util.ClassUtil;

/**
 * 开机广播接收器
 * Created by robust on 14-4-9.
 */
public class BootReceiver extends BroadcastReceiver {
    cn.scau.scautreasure.AppConfig_ config;

    @Override
    public void onReceive(Context context, Intent intent) {
        config = new cn.scau.scautreasure.AppConfig_(context);
        RingerMode duringMode = RingerMode.getModeByValue(config.duringClassRingerMode().get());
        RingerMode afterMode = RingerMode.getModeByValue(config.afterClassRingerMode().get());
        if(!RingerMode.isSet(duringMode.getValue()) && !RingerMode.isSet(afterMode.getValue())){
            return;
        }
        RingerMode.duringClassOn(context, duringMode, -1);
        RingerMode.afterClassOn(context, afterMode, 1);
        RingerMode.setDateChangedAlarm(context);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(ClassUtil.isDuringClassNow(context)){
            if(RingerMode.isSet(duringMode.getValue())){
                audioManager.setRingerMode(duringMode.getValue());
            }
        } else {
            if(RingerMode.isSet(afterMode.getValue())){
                audioManager.setRingerMode(afterMode.getValue());
            }
        }
    }
}
