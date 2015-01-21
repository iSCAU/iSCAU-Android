package cn.scau.scautreasure.helper;

import org.androidannotations.annotations.EBean;

import cn.scau.scautreasure.AppContext;

/**
 * Created by macroyep on 15/1/21.
 * Time:20:17
 */

public class AppHelper {

    private static AppHelper ourInstance = new AppHelper();

    public static AppHelper getInstance() {
        return ourInstance;
    }

    public AppHelper() {
    }


    public static boolean hasSetAccount(AppContext app) {
        if (app.config.userName().get().equals("") || app.config.eduSysPassword().get().equals("")) {
            return false;
        }
        return true;
    }


}
