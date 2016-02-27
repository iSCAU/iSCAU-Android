package cn.scau.scautreasure.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.umeng.common.Log;

import org.androidannotations.annotations.EReceiver;

//import cn.scau.scautreasure.service.NotifyFoodService_;

/**
 * Created by apple on 14-9-4.
 * 外卖监视活动上传接收器
 */

@EReceiver
public class NetWorkStatusReceiver extends BroadcastReceiver {
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;

    @Override
    public void onReceive( Context context, Intent intent ) {


        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
          System.out.println("网络状态已经改变");
            connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();
            if(info != null && info.isAvailable()) {

                //NotifyFoodService_.intent(context).start();//同步记录

                String name = info.getTypeName();
               System.out.println("当前网络名称：" + name);
            } else {
                System.out.println("没有可用网络");
            }
        }


//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
//        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if ( activeNetInfo != null ) {
////            Toast.makeText( context, "Active Network Type : " +
////                    activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
//        }
//        if( mobNetInfo != null ) {
////            Toast.makeText( context, "Mobile Network Type : " +
////                    mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
//        }
    }
}