package cn.scau.scautreasure.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.rest.RestService;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.api.SchoolActivityApi;
import cn.scau.scautreasure.model.ActivityCountModel;

/**
 * Created by apple on 14-9-5.
 */
@EService
public class ActivityCountService extends Service{
    @RestService
    SchoolActivityApi api;
    @App
    AppContext app;
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("红点服务开启");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("红点服务结束");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        load();

        return super.onStartCommand(intent, flags, startId);
    }
    @Background
    void load(){
        ActivityCountModel model=api.getActivityCount(String.valueOf(app.config.lastRedPoint().get()));
        if (Integer.parseInt(model.getResult())>0){
            System.out.println("校园活动有更新");
            app.config.lastRedPoint().put(Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0,10)));
            this.stopSelf();
        }else{
            System.out.println("校园活动无更新");
            this.stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
