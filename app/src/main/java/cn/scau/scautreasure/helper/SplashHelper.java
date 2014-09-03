package cn.scau.scautreasure.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;

import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.scau.scautreasure.api.SplashApi;
import cn.scau.scautreasure.model.SplashModel;
import cn.scau.scautreasure.service.SplashDownloadService;
import cn.scau.scautreasure.util.CryptUtil;

/**
 * Created by stcdasqy on 2014/7/25.
 */
public class SplashHelper {
    Context mContext;

    SplashApi api = new SplashApi_();

    SplashModel.SplashList mSplashList;

    public SplashHelper(Context ctx) {
        mContext = ctx;
    }

    public static long getLastUpdate(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences("splash", Context.MODE_PRIVATE);
        return sp.getLong("lastUpdate", 0);
    }

    public static void setLastUpdate(Context ctx, long lastUpdate) {
        SharedPreferences sp = ctx.getSharedPreferences("splash", Context.MODE_PRIVATE);
        sp.edit().putLong("lastUpdate", lastUpdate).commit();
    }

    public static String getFileName(String title) {
        return "splash_" + CryptUtil.base64_url_safe(title);
    }

    ArrayList<SplashModel> getSplashList() {
        SplashDatabaseHelper sp = new SplashDatabaseHelper(mContext);
        return sp.getSplashList();
    }

    public long getLastUpdate() {
        return getLastUpdate(mContext);
    }

    public void setLastUpdate(long lastUpdate) {
        setLastUpdate(mContext, lastUpdate);
    }

    public SplashModel getSuitableSplash() {
        ArrayList<SplashModel> list = getSplashList();
        SplashDatabaseHelper sp = new SplashDatabaseHelper(mContext);
        long now = System.currentTimeMillis() / 1000;
        for (int i = 0; i < list.size(); i++) {
            SplashModel splash = list.get(i);
            if (splash.getEnd_time() < now) {
                sp.delete(splash.getId());
                File f = mContext.getDir(getFileName(splash.getTitle()), Context.MODE_PRIVATE);
                if (f.exists()) f.delete();
            } else if (splash.getStart_time() < now && isSplashFileExist(splash.getTitle())) {
                return splash;
            }
        }
        return null;
    }

    void loadData_() {
        try {
            mSplashList = api.getSplash(getLastUpdate());
            setLastUpdate(System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (mSplashList != null && "success".equals(mSplashList.getStatus())) {
            writeToDatabase(mSplashList);
            CheckAndDownloadSplash();
        }
    }

    public void loadData() {
        BackgroundExecutor.execute(
                new BackgroundExecutor.Task("", 0, "") {
                    @Override
                    public void execute() {
                        try {
                            loadData_();
                        } catch (Throwable e) {
                            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                        }
                    }

                }
        );
    }

    void writeToDatabase(SplashModel.SplashList splashList) {
        SplashDatabaseHelper dbHelper = new SplashDatabaseHelper(mContext);
        dbHelper.insert(splashList);
    }

    void CheckAndDownloadSplash() {
        SplashDatabaseHelper dbHelper = new SplashDatabaseHelper(mContext);
        ArrayList<SplashModel> list = dbHelper.getSplashList();
        for (int i = list.size() - 1; i >= 0; i--) {
            if (isSplashFileExist(list.get(i).getTitle())) {
                list.remove(i);
            }
        }
        BinderService(list);
    }

    private void BinderService(final ArrayList<SplashModel> list) {
        Intent intent = new Intent(mContext, SplashDownloadService.class);
        mContext.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder binder) {

                SplashDownloadService localService = ((SplashDownloadService.LocalBinder) binder).getService();
                localService.enqueue(list);
                mContext.unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                // i can help to do something.
            }
        }, Context.BIND_AUTO_CREATE);
    }

    boolean isSplashFileExist(String title) {
        return new File(mContext.getFilesDir() + "/" + getFileName(title))
                .exists();
    }

    public final class SplashApi_
            implements SplashApi {

        private String rootUrl;
        private RestTemplate restTemplate;
        private RestErrorHandler restErrorHandler;

        public SplashApi_() {
            rootUrl = "http://iscaucms.sinaapp.com/index.php?m=Api&a=splash&";
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        }

        @Override
        public SplashModel.SplashList getSplash(long time) {
            HashMap<String, Object> urlVariables = new HashMap<String, Object>();
            urlVariables.put("time", time);
            try {
                return restTemplate.exchange(rootUrl.concat("lastupdate={time}"), HttpMethod.GET, null, SplashModel.SplashList.class, urlVariables).getBody();
            } catch (RestClientException e) {
                if (restErrorHandler != null) {
                    restErrorHandler.onRestClientExceptionThrown(e);
                    return null;
                } else {
                    throw e;
                }
            }
        }

    }
}
