package cn.scau.scautreasure.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.androidannotations.annotations.EService;

/**
 * Created by apple on 14-8-31.
 */
/*
1、	在原有课表的基础上，增加一个课程提醒的功能，在每节课上课前30分钟，在通知栏里面提醒，内容：你即将要上的课是：xxxx(课名)，地点:xxx。
要求是：
			1、要用同一个notification，不要多个notification在通知栏叠加。
			2、这个notification不闪光，但震动提示，并且允许用户在通知栏清除。
			3、在设置页面添加一个选项：是否开启上课提醒，默认是开。当用户选择关时，这个功能就关闭。√
（这个是功能上的优化亮点，请务必完成）

 */
@EService
public class AlertClassSerice extends Service{
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
