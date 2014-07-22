package cn.scau.scautreasure.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.RingerMode;

/**
 * 情景模式需要更改广播接收器
 * Created by robust on 14-4-9.
 */
public class RingerModeAlarmReceiver extends BroadcastReceiver {
    cn.scau.scautreasure.AppConfig_ config;

    @Override
    public void onReceive(Context context, Intent intent) {
        config = new cn.scau.scautreasure.AppConfig_(context);
        String action = intent.getAction();
        int mode;
        if(AppConstant.ACTION_RINGER_MODE_ALARM_DURING.equals(action)){
            mode = config.duringClassRingerMode().get();
        } else{
            mode = config.afterClassRingerMode().get();
        }
        if(!RingerMode.isSet(mode)){
            return;
        }
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(mode);
    }
}
