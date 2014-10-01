package cn.scau.scautreasure.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.scau.scautreasure.api.SplashApi;
import cn.scau.scautreasure.model.SchoolActivityModel;
import cn.scau.scautreasure.model.SplashModel;
import cn.scau.scautreasure.service.SplashDownloadService;
import cn.scau.scautreasure.ui.Splash;
import cn.scau.scautreasure.util.CryptUtil;

/**
 * Created by stcdasqy on 2014/7/25.
 */
@EBean
public class SplashHelper {

    @OrmLiteDao(helper = DatabaseHelper.class, model = SplashModel.class)
    RuntimeExceptionDao<SplashModel, Integer> modelDao;
    Context mContext;
    SplashApi api;
    SplashModel.SplashList mSplashList;

    public void initHelper(SplashApi api, Context ctx){
        this.api = api;
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

    public long getLastUpdate() {
        return getLastUpdate(mContext);
    }

    public void setLastUpdate(long lastUpdate) {
        setLastUpdate(mContext, lastUpdate);
    }

    public SplashModel getSuitableSplash() {
        List<SplashModel> list = modelDao.queryForAll();
        long now = System.currentTimeMillis() / 1000;
        for (int i = 0; i < list.size(); i++) {
            SplashModel splash = list.get(i);
            if (splash.getEnd_time() < now) {
                modelDao.delete(splash);
                File f = mContext.getDir(getFileName(splash.getEdit_time()+""), Context.MODE_PRIVATE);
                if (f.exists()) f.delete();
            } else if (splash.getStart_time() < now && isSplashFileExist(splash.getEdit_time()+"")) {
                return splash;
            }/*else if(splash.getStart_time() < now && !isSplashFileExist(splash.getTitle())){
                //假如没有下载成功

            }*/
        }
        return null;
    }

    void loadData_() {
        try {
            mSplashList = api.getSplash(getLastUpdate());
            if (mSplashList != null && "success".equals(mSplashList.getStatus())) {
                Log.d("splash","获取到新闪屏");
                writeToDatabase(mSplashList);
            }
            setLastUpdate(System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d("splash","无网络");
        }

        CheckAndDownloadSplash();
        return;
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

    public void writeToDatabase(SplashModel.SplashList splashList) {
        modelDao.setAutoCommit(false);
        List<SplashModel> lists = splashList.getCourses();
        if(lists != null){
            for(int i=0;i<lists.size();i++)
                modelDao.createOrUpdate(lists.get(i));
        }
        try {
            modelDao.commit(modelDao.getConnectionSource().getReadWriteConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CheckAndDownloadSplash() {
        List<SplashModel> list = modelDao.queryForAll();
        for (int i = list.size() - 1; i >= 0; i--) {
            if (isSplashFileExist(list.get(i).getEdit_time()+"")) {
                list.remove(i);
            }
        }
        BinderService(list);
    }

    private void BinderService(final List<SplashModel> list) {
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
}
