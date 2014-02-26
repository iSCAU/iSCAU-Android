package cn.scau.scautreasure;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.util.CryptUtil;
import cn.scau.scautreasure.util.StringUtil;
import cn.scau.scautreasure.util.TextManager;

/**
* 兼容升级低版本数据
* User:  Special Leung
* Date:  13-7-26
* Time:  下午8:17
* Mail:  specialcyci@gmail.com
*/
@EBean
public class AppCompatible {

    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @RootContext
    Context ctx;

    @Bean
    ClassHelper classHelper;

    public void upgrade(){

        int lastVersionCode    = config.versionCode().get();
        int currentVersionCode = getVersionCode();
        if (currentVersionCode == lastVersionCode) return;
        if (lastVersionCode == 0){
            System.out.println("update from version 7");
            // dealwith the old data
            upgrade_version_7();

        }

//        //  when next version releases
//        //  we can type "else if ( lastVersionCode = xx)"..
        config.versionCode().put(currentVersionCode);
    }

    /**
     * upgrade the old data of version code low than 7
     */
    private void upgrade_version_7(){

        // upgrade the username and password;
        SharedPreferences sp_edu    = ctx.getSharedPreferences("jwxt", ctx.MODE_APPEND);
        SharedPreferences sp_lib    = ctx.getSharedPreferences("lib", ctx.MODE_APPEND);
        SharedPreferences sp_card   = ctx.getSharedPreferences("xycard", ctx.MODE_APPEND);

        String edu_username         = sp_edu.getString("jwxt_user", "");
        String edu_password         = sp_edu.getString("jwxt_password", "");
        String lib_username         = sp_lib.getString("lib_user", "");
        String lib_password         = sp_lib.getString("lib_password", "");
        String card_username        = sp_card.getString("xycard_user", "");
        String card_password        = sp_card.getString("xycard_password", "");


        if(!StringUtil.isEmpty(edu_username)){

            CryptUtil cryptUtil = new CryptUtil();
            edu_password        = cryptUtil.encrypt(edu_password);
            config.userName().put(edu_username);
            config.eduSysPassword().put(edu_password);

            if (!StringUtil.isEmpty(lib_username) && edu_username.equals(lib_username)){
                lib_password        = cryptUtil.encrypt(lib_password);
                config.libPassword().put(lib_password);
            }

            if (!StringUtil.isEmpty(card_username) && edu_username.equals(card_username)){
                card_password       = cryptUtil.encrypt(card_password);
                config.cardPassword().put(card_password);
            }

        }

        // upgrade the class table data;
        TextManager textManager = new TextManager(ctx, "kcb_json.dat");
        if (textManager.isExist()){
            try {
                List<ClassModel> classList = parseFromOldVersionJson(textManager.readAll());
                classHelper.replaceAllLesson(classList);
                config.termStartDate().put("2013-09-02");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<ClassModel> parseFromOldVersionJson(String jsonString) throws JSONException {
        List<ClassModel> classModelList = new ArrayList<ClassModel>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i ++){
            JSONObject cls = jsonArray.getJSONObject(i);
            ClassModel classModel = new ClassModel();
            classModel.setClassname(cls.getString("classname"));
            classModel.setTeacher(cls.getString("teacher"));
            classModel.setDay(cls.getString("day"));
            classModel.setNode(cls.getString("note"));
            classModel.setStrWeek(Integer.valueOf(cls.getString("strWeek")));
            classModel.setEndWeek(Integer.valueOf(cls.getString("endWeek")));
            classModel.setDsz(cls.getString("dsz"));
            classModel.setLocation(cls.getString("location"));
            classModelList.add(classModel);
        }
        return classModelList;
    }
    /**
     * get the currentVersion Code;
     * @return versionCode;
     */
    private int getVersionCode(){

        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packInfo = null;

        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            int version = packInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // if error
        return 0;
    }
}
