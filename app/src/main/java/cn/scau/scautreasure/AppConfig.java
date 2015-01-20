package cn.scau.scautreasure;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import cn.scau.scautreasure.ui.FragmentClassTable;

/**
 * 应用程序配置文件;
 * User:  Special Leung
 * Date:  13-7-26
 * Time:  下午10:07
 * Mail:  specialcyci@gmail.com
 */

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface AppConfig {


    /*
    记录用户常用路线
     */
    @DefaultInt(1)
    //1号线,2号线
    int default_bus_line();

    /*
    单日模式和全周模式
    单日:0,全周:1
     */
    @DefaultInt(0)
    int day_week();

    /**
     * 智能课表/全部课表
     * false为显示全部,true为智能显示,默认是显示全部的.
     *
     * @return
     */
    @DefaultBoolean(false)
    boolean smart_class_table();

    @DefaultBoolean(false)
    boolean isFirstStartApp();

    @DefaultLong(0)
    long lastRedPoint();//上一次红点访问时间

    @DefaultBoolean(false)
    boolean hasUpdatedUsers();  //当前是否上传了用户的简略资料

    @DefaultString("")
    String lastOrderInfo();

    @DefaultBoolean(true)
    boolean isAlertClass();    //是否上课前提示


    @DefaultBoolean(false)
    boolean isThePad();  //判断设备类型,手机或者是平板

    @DefaultBoolean(false)
    boolean forceMobile();

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
    String stuName();

    @DefaultString("暂无")
    String major();              //用户专业

    @DefaultString("")
    String grade();             //年级

    @DefaultString("")
    String collage();             //学院

    @DefaultString("")
    String department();          //系

    @DefaultString("")
    String classes();             //班级

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
