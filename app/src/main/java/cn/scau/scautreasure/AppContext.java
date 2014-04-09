package cn.scau.scautreasure;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.util.CryptUtil;

/**
* AppContext.
* User:  Special Leung
* Date:  13-7-26
* Time:  下午8:27
* Mail:  specialcyci@gmail.com
*/
@EApplication
public class AppContext extends Application {

    public static String userName;
    public static String eduSysPassword;
    public static String libPassword;
    public static String cardPassword;
    public static int    server = 4;

    @Pref
    public static cn.scau.scautreasure.AppConfig_ config;

    @Override
    public void onCreate() {
        super.onCreate();
        compatiable();
        getAccountSettings();
    }

    private void compatiable(){
        System.out.println("update compatiable");
        AppCompatible appCompatible = AppCompatible_.getInstance_(this);
        appCompatible.upgrade();
    }

    @Background
    public void getAccountSettings(){
        CryptUtil cryptUtil = new CryptUtil();
        userName       = config.userName().get();
        eduSysPassword = cryptUtil.decrypt(config.eduSysPassword().get());
        libPassword    = cryptUtil.decrypt(config.libPassword().get());
        cardPassword   = cryptUtil.decrypt(config.cardPassword().get());
        server         = config.eduServer().get();
    }

    /**
     * 根据返回的requestCode,处理整个流程一般错误;
     * @param requestCode
     */
    public static void showError(int requestCode,Activity act){
        try{
            AppException appException = new AppException();
            appException.parseException(requestCode,act);
        }catch (AppException e){
            AppMsg.makeText(act,e.getMessage(), AppMsg.STYLE_ALERT).show();
        }
    }

    public static int getServer() {
        return server;
    }

    public static void setServer(int server) {
        AppContext.server = server;
        config.eduServer().put(server);
    }

    public String getEncodeEduSysPassword(){
        return CryptUtil.base64_url_safe(eduSysPassword);
    }

    public String getEncodeLibPassword(){
        return CryptUtil.base64_url_safe(libPassword);
    }

    public String getEncodeCardPassword(){
        return CryptUtil.base64_url_safe(cardPassword);
    }

    public void Log(int var){
        Log("" + var);
    }

    public void Log(long var){
        Log("" + var);
    }

    public void Log(String log){
        if(BuildConfig.DEBUG){
            Log.d("App",log);
        }
    }

}
