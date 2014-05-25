package cn.scau.scautreasure.service;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.ui.BorrowedBook_;

/**由NotificationReceiver操作
 * 弹出Notification
 * Created by Administrator on 2014/5/17.
 */
public class NotificationService extends Service {
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        super.onStart(intent, startId);

        try {
                        NotificationManager mNotificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                        int notificationIcon= R.drawable.icon;
                        CharSequence notificationTitle="来自华农宝";
                        long when = System.currentTimeMillis();

                        Notification notification=new Notification(notificationIcon, notificationTitle, when);

                        notification.defaults=Notification.DEFAULT_ALL;
                        notification.flags |= Notification.FLAG_AUTO_CANCEL;

                        Intent intentservice=new Intent(getApplicationContext(), BorrowedBook_.class);
                        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(), 0, intentservice, 0);
                        notification.setLatestEventInfo(getApplicationContext(),"借阅到期提示", "你有N本书即将满借阅",pendingIntent);

                        mNotificationManager.notify(1000, notification);

                } catch (Exception e) {
                    e.printStackTrace();
                }

        return Service.START_CONTINUATION_MASK;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
