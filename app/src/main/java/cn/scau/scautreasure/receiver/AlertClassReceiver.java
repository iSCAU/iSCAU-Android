package cn.scau.scautreasure.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.ui.Welcome_;

/**
 * Created by apple on 14-9-1.
 */

@EReceiver
public class AlertClassReceiver extends BroadcastReceiver
{
    private Context context;
    private String className;
    private String classTime;
    private String classBlock;
    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context=context;
        // TODO Auto-generated method stub
        this.className=intent.getStringExtra("className");
        this.classTime=intent.getStringExtra("classTime");
        this.classBlock=intent.getStringExtra("classBlock");
        Log.i("课程提醒Notification",className+"|"+classTime+"|"+classBlock);
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        addNotificaction();
    }
    /**
     * 添加一个notification
     */
    private void addNotificaction() {
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建一个Notification
        Notification notification = new Notification();
        //
        notification.icon = R.drawable.icon;
         notification.tickerText = className;
        // 添加振动
       // notification.defaults=Notification.DEFAULT_VIBRATE;

        //下边的两个方式可以添加音乐

        Intent intent = new Intent(context, Welcome_.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //点击状态栏的图标出现的提示信息设置
        //你即将要上的课是：xxxx(课名)，地点:xxx
        notification.setLatestEventInfo(context, "华农宝提醒你：", className+"("+classBlock+"|"+classTime+")", pendingIntent);
        
        if(config.isAlertClass().get()){
            manager.notify(1, notification);

        }


    }
}
