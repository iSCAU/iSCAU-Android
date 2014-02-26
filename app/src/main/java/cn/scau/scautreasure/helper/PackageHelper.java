package cn.scau.scautreasure.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import cn.scau.scautreasure.AppContext;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

/**
 * User: special
 * Date: 13-9-28
 * Time: 下午12:35
 * Mail: specialcyci@gmail.com
 */
@EBean
public class PackageHelper {
    @App
    AppContext app;

    /**
     * 返回当前程序版本名称
     */
    public String getAppVersionName() {
        String versionName = "";
        try {
            // Get the package info
            PackageManager pm = app.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(app.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.equals("")) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }
}
