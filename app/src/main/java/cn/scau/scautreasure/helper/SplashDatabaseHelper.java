package cn.scau.scautreasure.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cn.scau.scautreasure.BuildConfig;
import cn.scau.scautreasure.model.SplashModel;

public class SplashDatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "db_splash";
    private final static int DATABASE_VERSION = BuildConfig.VERSION_CODE;
    private final static String TABLE_NAME = "splash_list";
    private final static String FIELD_ID = "ID";
    private final static String FIELD_TITLE = "TITLE";
    private final static String FIELD_URL = "URL";
    private final static String FIELD_START_TIME = "START_TIME";
    private final static String FIELD_END_TIME = "END_TIME";
    private final static String FIELD_EDIT_TIME = "EDIT_TIME";

    public SplashDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static ContentValues toContentValues(SplashModel splashModel) {
        ContentValues cv = new ContentValues();
        cv.put(FIELD_ID, splashModel.getId());
        cv.put(FIELD_TITLE, splashModel.getTitle());
        cv.put(FIELD_URL, splashModel.getUrl());
        cv.put(FIELD_START_TIME, splashModel.getStart_time());
        cv.put(FIELD_END_TIME, splashModel.getEdit_time());
        cv.put(FIELD_EDIT_TIME, splashModel.getEdit_time());
        return cv;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table %s (%s integer primary key unique,%s text,%s text,%s integer,%s integer,%s integer);";
        sql = String.format(sql, TABLE_NAME, FIELD_ID, FIELD_TITLE, FIELD_URL, FIELD_START_TIME, FIELD_END_TIME, FIELD_EDIT_TIME);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    private Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
                FIELD_ID);
        return cursor;
    }

    public ArrayList<SplashModel> getSplashList() {
        ArrayList<SplashModel> spm = new ArrayList<SplashModel>();
        Cursor cursor = select();
        while (cursor.moveToNext()) {
            SplashModel sm = new SplashModel();
            sm.setId(cursor.getInt(0));
            sm.setTitle(cursor.getString(1));
            sm.setUrl(cursor.getString(2));
            sm.setStart_time(cursor.getInt(3));
            sm.setEnd_time(cursor.getInt(4));
            sm.setEdit_time(cursor.getInt(5));
            spm.add(sm);
        }
        cursor.close();
        return spm;
    }

    private void insert(int id, String title, String url, int start_time, int end_time, int edit_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_ID, id);
        cv.put(FIELD_TITLE, title);
        cv.put(FIELD_URL, url);
        cv.put(FIELD_START_TIME, start_time);
        cv.put(FIELD_END_TIME, end_time);
        cv.put(FIELD_EDIT_TIME, edit_time);
        db.insert(TABLE_NAME, null, cv);
    }

    private void insert(SplashModel splashModel) {
        int id;
        if ((id = query(splashModel)) != -1) {
            update(id, splashModel);
        } else {
            insert(splashModel.getId(), splashModel.getTitle(), splashModel.getUrl(),
                    splashModel.getStart_time(), splashModel.getEnd_time(), splashModel.getEdit_time());
        }
    }

    public void insert(SplashModel.SplashList splist) {
        ArrayList<SplashModel> list = splist.getCourses();
        if (list != null) for (int i = 0; i < list.size(); i++) insert(list.get(i));
    }

    public int query(SplashModel splashModel) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, FIELD_ID + "=?", new String[]{Integer.toString(splashModel.getId())},
                null, null, null, null);
        int ret = -1;
        if (cursor.moveToNext()) {
            ret = cursor.getInt(cursor.getColumnIndex(FIELD_ID));
        }
        cursor.close();
        return ret;
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_ID + "= ?";
        String[] value = {Integer.toString(id)};
        db.delete(TABLE_NAME, where, value);
    }

    private void update(int id, SplashModel splashModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = FIELD_ID + "= ?";
        String[] value = {Integer.toString(id)};
        ContentValues cv = toContentValues(splashModel);
        db.update(TABLE_NAME, cv, where, value);
    }
}