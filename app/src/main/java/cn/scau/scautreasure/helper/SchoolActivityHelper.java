package cn.scau.scautreasure.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.scau.scautreasure.model.SchoolActivityModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.util.CryptUtil;

/**
 * Created by stcdasqy on 2014-08-11.
 */
public class SchoolActivityHelper {

    private final static String cacheKey = "school_activity";
    private final static String SP_Name = cacheKey;
    private Context mContext;
    private long todayTime;
    private long tomorrowTime;
    private long laterTime;
    private Date todayDate;
    private Date tomorrowDate;
    private Date laterDate;
    private boolean hasInit = false;
    private List<SchoolActivityModel> today = new ArrayList<SchoolActivityModel>();
    private List<SchoolActivityModel> tomorrow = new ArrayList<SchoolActivityModel>();
    private List<SchoolActivityModel> later = new ArrayList<SchoolActivityModel>();

    public SchoolActivityHelper(Context ctx) {
        mContext = ctx;
        calcTime();
        initLists();
    }

    void calcTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        todayDate = new Date(year - 1900, month, day, 0, 0, 0);
        c.roll(Calendar.DATE, 1);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        tomorrowDate = new Date(year - 1900, month, day, 0, 0, 0);
        c.roll(Calendar.DATE, 1);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        laterDate = new Date(year - 1900, month, day, 0, 0, 0);
        /*
        int day = 60*60*24;
        Date nowTime = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        long offset = (c.get(Calendar.ZONE_OFFSET) +
                c.get(Calendar.DST_OFFSET));
        long now = System.currentTimeMillis() + offset;
        long distance = now/1000 - (now/1000) / day * day;
        todayTime = System.currentTimeMillis()/1000-distance;
        tomorrowTime = todayTime + day;
        laterTime = tomorrowTime + day;*/
    }

    void initLists() {
        SchoolActivityModel.ActivityList activities = (SchoolActivityModel.ActivityList) CacheUtil.get(mContext).getAsObject(cacheKey);
        today.clear();
        tomorrow.clear();
        later.clear();
        if (activities != null) {
            List<SchoolActivityModel> content = activities.getContent();
            if (content != null) {
                int originalSize = content.size();
                for (int i = content.size() - 1; i >= 0; i--) {
                    SchoolActivityModel act = content.get(i);
                    if (act == null) continue;
                    if (!hasInit) act.setIsNewOne(false);
                    Date d;
                    try {
                        Timestamp ts = Timestamp.valueOf(act.getTime());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(ts);
                        d = calendar.getTime();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        continue;
                    }

                    if (d.before(todayDate)) {
                        DateFormat df;
                        content.remove(i);
                        deleteLogoCache(act.getLogoUrl(), act.getT());
                        deleteContentCache(act.getContent(), act.getT());
                    } else if (d.before(tomorrowDate)) {
                        today.add(act);
                    } else if (d.before(laterDate)) {
                        tomorrow.add(act);
                    } else {
                        later.add(act);
                    }
                }
                if (originalSize != content.size()) {
                    activities.setContent(content);
                    CacheUtil.get(mContext).put(cacheKey, activities);
                }
            }
        }
        hasInit = true;
    }

    public List<SchoolActivityModel> getToday() {
        return today;
    }

    public List<SchoolActivityModel> getTomorrow() {
        return tomorrow;
    }

    public List<SchoolActivityModel> getLater() {
        return later;
    }

    public synchronized void addSchoolActivity(SchoolActivityModel.ActivityList acts) {
        SchoolActivityModel.ActivityList activities = (SchoolActivityModel.ActivityList)
                CacheUtil.get(mContext).getAsObject(cacheKey);
        if (activities == null) {
            CacheUtil.get(mContext).put(cacheKey, acts);
            initLists();
            return;
        }
        List<SchoolActivityModel> lists = acts.getContent();
        if (lists != null && lists.size() != 0) {
            List<SchoolActivityModel> content = activities.getContent();
            try {
                for (int i = content.size() - 1; i >= 0; i--) {
                    for (int j = lists.size() - 1; j >= 0; j--)
                        if (content.get(i).getId() == lists.get(j).getId()) {
                            content.remove(i);
                            break;
                        }
                }
                content.addAll(0, lists);
                //activities.setContent(content);
                CacheUtil.get(mContext).put(cacheKey, activities);
                initLists();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void deleteLogoCache(String logourl, long time) {
        File logo = new File(mContext.getFilesDir() + "/" + getBitmapName(logourl, time));
        if (logo.exists()) logo.delete();
    }

    public Bitmap getBitmap(String logourl, long time) {
        if (new File(mContext.getFilesDir() + "/" + getBitmapName(logourl, time))
                .exists()) {
            FileInputStream fis = null;
            try {
                fis = mContext.openFileInput(getBitmapName(logourl, time));
                return BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    String getBitmapName(String logourl, long time) {
        String key = String.valueOf(logourl.hashCode() + time);
        return "schoolactivity_" + CryptUtil.base64_url_safe(key);
    }

    void deleteContentCache(String content, long time) {
        File cachefile = new File(mContext.getFilesDir() + "/" +
                getContentCacheName(content, time));
        if (cachefile.exists()) cachefile.delete();
    }

    public String getContentCacheName(String content, long time) {
        String key = String.valueOf(content.hashCode() + time);
        return "schoolactivitycontent_" + CryptUtil.base64_url_safe(key) +
                ".html";
    }

    public void download(String logourl, long time, OnDownloadStateChanged onDownloadStateChanged) {
        new DownloadImageThread(logourl, getBitmapName(logourl, time), onDownloadStateChanged)
                .start();
    }

    public long getLastUpdate() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        return sp.getLong("lastUpdate", 0);
    }

    public void setLastUpdate(long lastUpdate) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        sp.edit().putLong("lastUpdate", lastUpdate).commit();
    }

    public interface OnDownloadStateChanged {
        void onDownloadFinished(Bitmap bitmap);

        void onDownloadFailure();
    }

    class DownloadImageThread extends Thread {
        String ImageUrl, filename;
        OnDownloadStateChanged mOnDownloadStateChanged;

        DownloadImageThread(String url, String filename, OnDownloadStateChanged onDownloadStateChanged) {
            ImageUrl = url;
            this.filename = filename;
            mOnDownloadStateChanged = onDownloadStateChanged;
        }

        @Override
        public void run() {
            super.run();
            URL url = null;
            try {
                FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                url = new URL(ImageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                if (mOnDownloadStateChanged != null) {
                    mOnDownloadStateChanged.onDownloadFinished(bitmap);
                    mOnDownloadStateChanged = null;
                }
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos)) {
                    Toast.makeText(mContext, "Logo write to cache failed.", Toast.LENGTH_SHORT)
                            .show();
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                if (mOnDownloadStateChanged != null)
                    mOnDownloadStateChanged.onDownloadFailure();
            }
        }
    }

}
