package cn.scau.scautreasure;

import com.j256.ormlite.field.DatabaseField;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import cn.scau.scautreasure.ui.ClassTable;

/**
 * 应用程序配置文件;
 * User:  Special Leung
 * Date:  13-7-26
 * Time:  下午10:07
 * Mail:  specialcyci@gmail.com
 */

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface AppConfig {

    @DefaultBoolean(true)
    boolean isAlertClass();

   @DefaultBoolean(false)
   boolean isThePad();


    @DefaultLong(0)
    long lastUpdateFood();  //上一次更新外卖列表的时间记录

    @DefaultInt(0)
    int block();            //外卖地址校区:五山区,华山区,启林区

    @DefaultString("")
    String address();     //外卖详细地址

    @DefaultInt(0)
    int versionCode();           // 当前的versionCode

    @DefaultInt(4)
    int eduServer();             // 当前选择的教务系统服务器;

    @DefaultString("")
    String userName();           // 用户学号

    @DefaultString("")
    String eduSysPassword();     // 教务系统密码

    @DefaultString("")
    String libPassword();        // 图书馆密码

    @DefaultString("")
    String cardPassword();       // 校园卡密码

    @DefaultString("")
    String termStartDate();      // 学期开始时间

    @DefaultString("")
    String lastSeeNotificationDate();      // 上次显示通知的时间

    @DefaultInt(ClassTable.MODE_PARAMS)
    int classTableShowMode(); // 课程表显示模式;

    @DefaultInt(0xffffffff)
    int widgetFontColor();

    @DefaultString("1.0")
    String widgetFontSize();

    @DefaultString("无")
    String widgetBackground();

    // 已过期 from v2.4.+ .
    @DefaultBoolean(false)
    boolean classTableAsFirstScreen();

    @DefaultInt(0)
    int classTableSelectedTab(); // 记录用户当前选择的 Tab （单日 or 全周）;

    @DefaultInt(-1)
    int duringClassRingerMode();//上课时的情景模式，默认不设置

    @DefaultInt(-1)
    int afterClassRingerMode(); //下课后的情景模式，默认不设置

    @DefaultLong(0)
    long lastUpdated();

}
