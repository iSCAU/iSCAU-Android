package cn.scau.scautreasure.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import org.androidannotations.annotations.EService;

import cn.scau.scautreasure.receiver.NetWorkStatusReceiver;

/**
 * Created by apple on 14-9-4.
 *检测网络状态服务
 */
@EService
public class NetworkStatusService extends Service{

    private NetWorkStatusReceiver mReceiver;

    
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("网络状态检测服务开启");
        mReceiver=new NetWorkStatusReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("网络状态检测服务关闭");
        unregisterReceiver(mReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//                Log.d("mark", "网络状态已经改变");
//                connectivityManager = (ConnectivityManager)
//
//                        getSystemService(Context.CONNECTIVITY_SERVICE);
//                info = connectivityManager.getActiveNetworkInfo();
//                if(info != null && info.isAvailable()) {
//                    String name = info.getTypeName();
//                    Log.d("mark", "当前网络名称：" + name);
//                } else {
//                    Log.d("mark", "没有可用网络");
//                }
//            }
//        }
//    };

}
