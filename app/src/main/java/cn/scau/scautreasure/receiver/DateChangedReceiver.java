package cn.scau.scautreasure.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import cn.scau.scautreasure.RingerMode;
import cn.scau.scautreasure.util.ClassUtil;

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
        RingerMode duringMode = RingerMode.getModeByValue(config.duringClassRingerMode().get());
        RingerMode afterMode = RingerMode.getModeByValue(config.afterClassRingerMode().get());
        RingerMode.duringClassOn(context, duringMode, -1);
        RingerMode.afterClassOn(context, afterMode, 1);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(Intent.ACTION_DATE_CHANGED.equals(intent.getAction())){
            //手动修改时间引起的日期变化，重新判断当前是否上课，设置相应的情景模式
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
        if(Intent.ACTION_TIME_CHANGED.equals(intent.getAction())){
            if(ClassUtil.isDuringClassNow(context)){
                audioManager.setRingerMode(duringMode.getValue());
            } else {
                audioManager.setRingerMode(afterMode.getValue());
            }
        }
    }
}
