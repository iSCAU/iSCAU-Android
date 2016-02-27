package cn.scau.scautreasure.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.rest.RestService;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.model.PersonModel;

/**
 * Created by apple on 14-9-5.
 */
@EService
public class LoginService extends Service {
    public LoginService() {
        super();
    }

    @RestService
    EdusysApi api;
    @App
    AppContext app;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("登录服务开始");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: 2016-2-27 不记录任何信息
        //login();
        
        return super.onStartCommand(intent, flags, startId);
    }

    @Background
    void login() {
        PersonModel personModel = api.login(app.config.userName().get(), app.config.eduSysPassword().get(), 4);
        if (personModel.getMsg().equals("password_error")) {
            System.out.println("登录失败");
        } else {
            app.config.stuName().put(personModel.getName());
            app.config.major().put(personModel.getMajor());
            app.config.department().put(personModel.getDepartment());
            app.config.grade().put(personModel.getGrade());
            app.config.collage().put(personModel.getCollage());
            app.config.classes().put(personModel.getClasses());
            System.out.println("登录成功==="+personModel.getName());
            UpLoadUsersService_.intent(getApplicationContext()).start();
            this.stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("登录服务关闭");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
