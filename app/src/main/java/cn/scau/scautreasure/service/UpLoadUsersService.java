package cn.scau.scautreasure.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.rest.RestService;

import cn.scau.scautreasure.AppConfig;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.api.UsersApi;
import cn.scau.scautreasure.model.UpLoadUsersModel;

/**
 * Created by apple on 14-9-5.
 */
@EService
public class UpLoadUsersService extends Service {
    @RestService
    UsersApi Api;

    @App
    AppContext app;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UpLoad();
        return super.onStartCommand(intent, flags, startId);
    }

    @Background
    void UpLoad() {
        System.out.println("开始统计");
        try {
            UpLoadUsersModel upLoadUsersModel = Api.upLoadUsers(app.config.userName().get(), "暂无", app.config.major().get());

            if (upLoadUsersModel != null)
                if (upLoadUsersModel.getResult().equals("success")) {
                    System.out.println("统计成功");
                    app.config.hasUpdatedUsers().put(true);

                } else {
                    System.out.println("统计出错");

                }
        }catch (Exception e){
            System.out.println("统计出错");
        }
        this.stopSelf();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("上传用户资料服务结束");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("上传用户资料服务开启");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
