package cn.scau.scautreasure.ui;

import android.widget.TextView;
import cn.scau.scautreasure.AppConfig_;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.util.DateUtil;
import cn.scau.scautreasure.widget.ResideMenu;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 软件公告通知.
 *
 * User: special
 * Date: 13-10-5
 * Time: 下午1:36
 * Mail: specialcyci@gmail.com
 */
@EFragment(R.layout.notification)
public class Notification extends Common{

    @Pref cn.scau.scautreasure.AppConfig_ config;
    @ViewById TextView tv_content;
    @Bean  DateUtil dateUtil;
    private String notification;

    @AfterViews
    void initViews(){
        closeMenu();
        setTitle(R.string.title_notification);
        getFragmentArguments();
        tv_content.setText(notification);
        config.lastSeeNotificationDate().put(dateUtil.getCurrentDateString());
    }

    @Click
    void abs__home(){
        getSherlockActivity().getSupportFragmentManager().popBackStack();
        getResideMenu().openMenu();
    }

    private void getFragmentArguments(){
        notification = getArguments().getString("notification");
    }

}
