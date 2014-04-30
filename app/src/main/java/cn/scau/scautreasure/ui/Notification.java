package cn.scau.scautreasure.ui;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.util.DateUtil;

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
    void home(){
        getSherlockActivity().getSupportFragmentManager().popBackStack();
        getResideMenu().openMenu();
    }

    private void getFragmentArguments(){
        notification = getArguments().getString("notification");
    }

}
