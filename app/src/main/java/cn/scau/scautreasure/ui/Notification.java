package cn.scau.scautreasure.ui;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
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
@EActivity(R.layout.notification)
public class Notification extends CommonActivity {

    @Pref cn.scau.scautreasure.AppConfig_ config;
    @ViewById TextView tv_content;
    @Bean  DateUtil dateUtil;
    @Extra
    String notification;

    @AfterViews
    void initViews(){
        setTitle(R.string.title_notification);
        tv_content.setText(notification);
        config.lastSeeNotificationDate().put(dateUtil.getCurrentDateString());
    }

    @Click
    void btn_back(){
       this.finish();
    }

}
