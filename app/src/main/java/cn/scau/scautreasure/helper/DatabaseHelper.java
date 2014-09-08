package cn.scau.scautreasure.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.model.FoodShopDBModel;
import cn.scau.scautreasure.model.ShopMenuDBModel;

/**
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午9:11
 * Mail:  specialcyci@gmail.com
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "db_iscau.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ClassModel.class);
            Log.d("food","foodShop建表");
            TableUtils.createTable(connectionSource, FoodShopDBModel.class);
            TableUtils.createTable(connectionSource, ShopMenuDBModel.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.d("food","foodShop建表");
            TableUtils.createTable(connectionSource, FoodShopDBModel.class);
            TableUtils.createTable(connectionSource, ShopMenuDBModel.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

}
