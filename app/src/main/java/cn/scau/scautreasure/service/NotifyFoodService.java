package cn.scau.scautreasure.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
//import org.apache.commons.logging.Log;

import cn.scau.scautreasure.AppContext;

/**
 * Created by apple on 14-9-4.
 */
@EService
public class NotifyFoodService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("同步最新一次点餐记录服务开启");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        access();
        return super.onStartCommand(intent, flags, startId);

    }

    @Background
    void access() {
        try {
            if (AppContext.notifyFood(getApplicationContext())) {
                System.out.println("同步最后一次点餐记录成功");

            } else {
//            System.out.println();

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.stopSelf();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
