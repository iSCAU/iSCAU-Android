package cn.scau.scautreasure.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.scau.scautreasure.model.SchoolActivityModel;
import cn.scau.scautreasure.model.ShopMenuDBModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.util.CryptUtil;

/**
 * Created by stcdasqy on 2014-08-11.
 */
@EBean
public class SchoolActivityHelper {

    private final static String SP_Name = "school_activity";

    @OrmLiteDao(helper = DatabaseHelper.class, model = SchoolActivityModel.class)
    RuntimeExceptionDao<SchoolActivityModel, Integer> modelDao;

    private Context mContext;
    private Date todayDate;
    private Date tomorrowDate;
    private Date laterDate;
    private List<SchoolActivityModel> today = new ArrayList<SchoolActivityModel>();
    private List<SchoolActivityModel> tomorrow = new ArrayList<SchoolActivityModel>();
    private List<SchoolActivityModel> later = new ArrayList<SchoolActivityModel>();

    public void initHelper(Context ctx){
        mContext = ctx;
        calcTime();
        try {
            initLists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    }

    void initLists() throws SQLException{
        List<SchoolActivityModel> content = null;
        content = modelDao.queryForAll();

        modelDao.setAutoCommit(false);
        today.clear();
        tomorrow.clear();
        later.clear();
            if (content != null) {
                for (int i = content.size() - 1; i >= 0; i--) {
                    SchoolActivityModel act = content.get(i);
                    if (act == null) continue;
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
                        modelDao.delete(act);
                    } else if (d.before(tomorrowDate)||act.getLevel()==9) {
                        today.add(act);
                    } else if (d.before(laterDate)) {
                        tomorrow.add(act);
                    } else {
                        later.add(act);
                    }
                }
            }
        modelDao.commit(modelDao.getConnectionSource().getReadWriteConnection());
        Collections.sort(today);
        Collections.sort(tomorrow);
        Collections.sort(later);
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

    public synchronized void addSchoolActivity(SchoolActivityModel.ActivityList acts) throws SQLException{
            List<SchoolActivityModel> lists = acts.getContent();
            modelDao.setAutoCommit(false);
            for (int j = lists.size() - 1; j >= 0; j--)
            modelDao.createOrUpdate(lists.get(j));
            modelDao.commit(modelDao.getConnectionSource().getReadWriteConnection());
        initLists();
    }

    public long getLastUpdate() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        return sp.getLong("lastUpdate", 0);
    }

    public void setLastUpdate(long lastUpdate) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_Name, Context.MODE_PRIVATE);
        sp.edit().putLong("lastUpdate", lastUpdate).commit();
    }
}
