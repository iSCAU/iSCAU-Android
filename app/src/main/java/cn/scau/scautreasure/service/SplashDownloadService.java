package cn.scau.scautreasure.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.helper.SplashHelper;
import cn.scau.scautreasure.model.SplashModel;

/**
 * Created by stcdasqy on 2014/7/25.
 */
public class SplashDownloadService extends Service {

    private static final String TAG = "SplashDownloadService";
    ArrayList<SplashModel> downloadList = new ArrayList<SplashModel>();
    Object sync = new Object();
    boolean isDownloadThreadStarted = false;
    Runnable downloadThread = new Runnable() {
        @Override
        public void run() {
            if (isDownloadThreadStarted) return;
            isDownloadThreadStarted = true;
            while (true) {
                String path = null;
                if (downloadList.size() != 0) {
                    SplashModel sm = downloadList.get(0);
                    path = SplashHelper.getFileName(sm.getEdit_time()+"");
                    if (downloadFile(sm.getUrl(), path)) {
                        synchronized (sync) {
                            downloadList.remove(0);
                        }
                    } else {
                        try {
                            Thread.sleep(60 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    break;
                }
            }
            isDownloadThreadStarted = false;
        }
    };
    private IBinder binder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void enqueue(SplashModel splashModel) {
        synchronized (sync) {
            if (downloadList.contains(splashModel)) return;
            else downloadList.add(splashModel);
            new Thread(downloadThread).start();
        }
    }

    public void enqueue(List<SplashModel> splashModels) {
        synchronized (sync) {
            for (int i = 0; i < splashModels.size(); i++) {
                if (!downloadList.contains(splashModels.get(i)))
                    downloadList.add(splashModels.get(i));
            }
            new Thread(downloadThread).start();
        }
    }

    boolean downloadFile(String http, String filename) {
        try {
            Log.d("splash_download",http);
            URL url = new URL(http);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            OutputStream os = this.openFileOutput(filename + ".temp", Context.MODE_PRIVATE);
            byte buffer[] = new byte[4 * 1024];
            int byteRead;
            while ((byteRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, byteRead);
            }
            os.flush();
            os.close();
            ;
            synchronized (sync) {
                File f = new File(this.getFilesDir() + "/" + filename);
                if (f.exists()) f.delete();

                File f2 = new File(this.getFilesDir() + "/" + filename + ".temp");
                f2.renameTo(f);
                Log.d("splash","文件写入完成");
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return true; //既然地址无效，那就return true删掉好了，留着也没用。
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false; //这里该return true还是false好，我还不知道。
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public class LocalBinder extends Binder {
        //返回本地服务
        public SplashDownloadService getService() {
            return SplashDownloadService.this;
        }
    }
}
