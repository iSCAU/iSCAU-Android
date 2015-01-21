package cn.scau.scautreasure.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.web.client.HttpStatusCodeException;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.HttpLoader;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.model.ActivityCountModel;
import cn.scau.scautreasure.widget.AppNotification;
import cn.scau.scautreasure.widget.AppViewDrawable;
import cn.scau.scautreasure.widget.BadgeView;


/**
 * 入口Activity
 */
@EActivity
public class Main extends FragmentActivity {
    @Bean
    HttpLoader httpLoader;
    @App
    AppContext app;

    private FragmentManager fragmentManager;

    //课表,应用,活动圈,校巴
    private Fragment fragment_class_table, fragment_app, fragment_activity, fragment_bus;

    //红点
    BadgeView bv_class_table, bv_app, bv_bus, bv_activity;
    @ViewById(R.id.bt_classtable)
    Button bt_class_table;
    @ViewById(R.id.bt_app)
    Button bt_app;
    @ViewById(R.id.bt_bus)
    Button bt_bus;
    @ViewById(R.id.bt_activity)
    Button bt_activity;

    @ViewById(R.id.rd_classtable)
    RadioButton rd_class_table;

    @ViewById(R.id.rd_app)
    RadioButton rd_app;

    @ViewById(R.id.rd_bus)
    RadioButton rd_bus;

    @ViewById(R.id.rd_activity)
    RadioButton rd_activity;

    public enum TabName {
        CLASS_TABLE("class_table"), APP("app"), ACTIVITY("activity"), BUS("bus");
        private String name;

        TabName(String _name) {
            this.name = _name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        fragmentManager = getSupportFragmentManager();

        //初始化友盟
        initMobclickAgent();

        initTabButton();
        //刷新红点
        updateTabRedPoint();
    }

    /**
     * 初始化tabButton
     */

    void initTabButton() {

        RadioButton[] rb = {rd_class_table, rd_activity, rd_bus, rd_app};
        for (RadioButton bt : rb) {
            AppViewDrawable.build(bt, 1, 0, 7, 60, 60);
        }

        //切换到课表页面
        tabRadio(rd_class_table);
        rd_class_table.setChecked(true);


    }

    /**
     * 切换页面
     *
     * @param v
     */
    @Click({R.id.rd_classtable, R.id.rd_app, R.id.rd_bus, R.id.rd_activity})
    void tabRadio(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.rd_classtable:
                if (fragment_class_table == null) {
                    fragment_class_table = new FragmentClassTable_();
                    transaction.add(R.id.container, fragment_class_table);
                }
                if (fragment_app != null)
                    transaction.hide(fragment_app);
                if (fragment_bus != null)
                    transaction.hide(fragment_bus);
                if (fragment_activity != null)
                    transaction.hide(fragment_activity);
                transaction.show(fragment_class_table);
//                ((OnTabSelectListener) fragment_class_table).onTabSelect();
                break;
            case R.id.rd_app:

                if (fragment_app == null) {
                    fragment_app = new FragmentApp_();
                    transaction.add(R.id.container, fragment_app);
                }
                if (fragment_class_table != null)
                    transaction.hide(fragment_class_table);
                if (fragment_bus != null)
                    transaction.hide(fragment_bus);
                if (fragment_activity != null)
                    transaction.hide(fragment_activity);
                transaction.show(fragment_app);


                //     ((OnTabSelectListener) fragment_app).onTabSelect();

                break;
            case R.id.rd_bus:
                if (fragment_bus == null) {
                    fragment_bus = new FragmentBus_();
                    transaction.add(R.id.container, fragment_bus);
                }
                if (fragment_app != null)
                    transaction.hide(fragment_app);
                if (fragment_class_table != null)
                    transaction.hide(fragment_class_table);
                if (fragment_activity != null)
                    transaction.hide(fragment_activity);
                transaction.show(fragment_bus);
                break;

            case R.id.rd_activity:
                if (fragment_activity == null) {
                    fragment_activity = new FragmentActivity_();
                    transaction.add(R.id.container, fragment_activity);
                }
                if (fragment_app != null)
                    transaction.hide(fragment_app);
                if (fragment_bus != null)
                    transaction.hide(fragment_bus);
                if (fragment_class_table != null)
                    transaction.hide(fragment_class_table);
                transaction.show(fragment_activity);
                hideActivityRedPoint();
                //((OnTabSelectListener) fragment_activity).onTabSelect();
                break;
        }
        transaction.commit();

    }

    /**
     * 初始化友盟
     */
    private void initMobclickAgent() {
        //自动更新
        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.openActivityDurationTrack(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        // 检查反馈消息;
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();
        showNotification();
    }

    /**
     * 显示消息推送
     */
    @UiThread(delay = 4000)
    void showNotification() {
        String notification = MobclickAgent.getConfigParams(this, "notification");
        if (!notification.trim().equals("0") && !notification.trim().equals("")) {
            // 今天显示过就不显示了
            if (!app.config.lastSeeNotificationDate().get().equals(app.dateUtil.getCurrentDateString())) {
                AppNotification.show(this, "软件公告", notification, "关闭", new AppNotification.Callback() {
                    @Override
                    public void onCancel() {

                    }
                });
            }
        }
    }

    /**
     * 初始化红点控件,因为直接用RadioButton会变形,就套一个Button
     */
    private void updateTabRedPoint() {
        bv_class_table = AppContext.setTabRedPoint(this, bt_class_table);
        bv_app = AppContext.setTabRedPoint(this, bt_app);
        bv_bus = AppContext.setTabRedPoint(this, bt_bus);
        bv_activity = AppContext.setTabRedPoint(this, bt_activity);
        refreshActivityRedPoint();
    }

    /**
     * 从服务端刷新消息,判断是否显示红点
     */
    @Background(delay = 500)
    protected void refreshActivityRedPoint() {
        httpLoader.updateActivityFlags(new HttpLoader.NormalCallBack() {
            @Override
            public void onSuccess(Object obj) {
                if (obj.toString().equals("yes"))
                    showActivityRedPoint();
            }

            @Override
            public void onError(Object obj) {
                hideActivityRedPoint();
            }

            @Override
            public void onNetworkError(Object obj) {
                hideActivityRedPoint();
            }
        });

    }

    /**
     * 显示活动圈的红点
     */
    @UiThread
    protected void showActivityRedPoint() {
        bv_activity.show();
    }

    /**
     * 隐藏活动圈红点
     */
    @UiThread
    void hideActivityRedPoint() {
        bv_activity.hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
