package cn.scau.scautreasure.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.umeng.fb.push.FBMessage;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;
import com.umeng.update.UmengUpdateAgent;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.*;
import java.util.Map;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.HttpLoader;


import cn.scau.scautreasure.widget.AppOKCancelDialog;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;


/**
 * 入口Activity
 */
@EActivity
public class Main extends MaterialNavigationDrawer {

    MaterialSection classTableSection, activitySection, busSection, appSection;
    TextView tv_title;
    TextView tv_year_month;
    TextView tv_day;
    LinearLayout tv_container;

    @Override
    public void init(Bundle bundle) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_header_drawer, null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_year_month = (TextView) view.findViewById(R.id.tv_year_month);
        tv_day = (TextView) view.findViewById(R.id.tv_day);
        tv_container = (LinearLayout) view.findViewById(R.id.tv_container);
        tv_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("菜单", "打开日历");
            }
        });

        tv_year_month.setText(app.dateUtil.getCurrentYearMonth() + "\n" + app.dateUtil.getWeekOfDate());
        tv_day.setText(app.dateUtil.getCurrentDay());
        tv_title.setText("今天没啥事,吃屎吧");

        setDrawerHeaderCustom(view);
        // create sections
        classTableSection = newSection("课程表", R.drawable.icon_classtale_md, new FragmentClassTable_());
        activitySection = newSection("活动圈", R.drawable.icon_activity_md, new FragmentActivity_());
        busSection = newSection("实时校巴", R.drawable.icon_bus_md, new FragmentBus_());
        appSection = newSection("应用中心", R.drawable.icon_app_md, new FragmentApp_());

        this.setBackPattern(BACKPATTERN_BACK_TO_FIRST);

        this.addSection(classTableSection);
        this.addSection(activitySection);
        this.addSection(busSection);
        this.addSection(appSection);

        this.disableLearningPattern();
        // create bottom section
        this.addBottomSection(newSection("设置", R.drawable.icon_setting_md, new Intent(this, Settings_.class)));
        //初始化友盟
        initMobclickAgent();

        //刷新红点
        refreshActivityRedPoint();
    }

    @Bean
    HttpLoader httpLoader;
    @App
    AppContext app;


    /**
     * 初始化友盟
     */
    private void initMobclickAgent() {
        //自动更新
        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.openActivityDurationTrack(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        PushAgent.getInstance(this).onAppStart();
        String device_token = UmengRegistrar.getRegistrationId(this);
        System.out.println("token:" + device_token);

        // 检查反馈消息;
        final FeedbackAgent agent = new FeedbackAgent(this);

        agent.openFeedbackPush();
        PushAgent.getInstance(this).enable();
        PushAgent.getInstance(this).setNotificationClickHandler(new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                System.out.println("处理通知栏推送:" + uMessage.custom.toString());
//                super.dealWithCustomAction(context, uMessage);
            }
        });
        PushAgent.getInstance(this).setMessageHandler(new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(Context context, UMessage uMessage) {


                try {
                    JSONObject json = new JSONObject(uMessage.custom);
                    String type = json.getString("type");
                    if (type.equals("dev_reply")) {
                        showReplyFeedback(agent);
                        System.out.println("反馈回复推送:" + uMessage.custom.toString());
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showCustomPush(uMessage);
                System.out.println("处理自定义推送:" + uMessage.custom.toString() + "|" + uMessage.extra);

            }
        });
        System.out.println("token:" + UmengRegistrar.getRegistrationId(this));


        Conversation conversation = agent.getDefaultConversation();
        conversation.sync(new SyncListener() {
            @Override
            public void onReceiveDevReply(List<Reply> replies) {
                if (replies.size() > 0) {
                    showReplyFeedback(agent);
                }
                System.out.println("onReceiveDevReply\n");

                for (Reply r : replies)
                    System.out.println("onReceiveDevReply\n" + r.toString());
            }

            @Override
            public void onSendUserReply(List<Reply> replies) {
                System.out.println("onSendUserReply\n");

                for (Reply r : replies)
                    System.out.println("onSendUserReply\n" + r.toString());

            }
        });
        showNotification();
    }

    /**
     * 回复反馈
     */
    @UiThread(delay = 100)
    void showReplyFeedback(final FeedbackAgent agent) {
        AppOKCancelDialog.show(Main.this, "宝宝君", "我给你发了一条消息,请注意查看", "查看", "忽略", new AppOKCancelDialog.Callback() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
//                agent.startFeedbackActivity();
                FeedBackActivity_.intent(Main.this).start();
            }

        });

    }

    /**
     * 自定义消息推送
     */
    @UiThread
    void showCustomPush(UMessage uMessage) {
        String title = "";//标题
        String text = uMessage.custom;//要显示的东西
        String intent = "";//网址

        for (Map.Entry<String, String> entry : uMessage.extra.entrySet()) {
            if (entry.getKey().equals("intent")) {
                intent = entry.getValue();
            }
            if (entry.getKey().equals("title")) {
                title = entry.getValue();
            }
        }
        final String url = intent;
        final String browser_title = title;
        AppOKCancelDialog.show(this, title.equals("") ? "提示" : title, text, "去看看", "忽略", new AppOKCancelDialog.Callback() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                //do somethings
                BaseBrowser_.intent(Main.this).url(url).browser_title(browser_title).start();
            }
        });


    }

    /**
     * 显示公告消息推送
     */
    @UiThread(delay = 4000)
    void showNotification() {
        String notification = MobclickAgent.getConfigParams(this, "notification");
        if (!notification.trim().equals("0") && !notification.trim().equals("")) {
            // 今天显示过就不显示了
            if (app.config.lastSeeNotificationDate().get().equals(app.dateUtil.getCurrentDateString())) {
                AppOKCancelDialog.show(Main.this, "宝宝君提醒你", notification, "朕已阅", "忽略", new AppOKCancelDialog.Callback() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onOk() {
                        app.config.lastSeeNotificationDate().put(app.dateUtil.getCurrentDateString());
                    }
                });


            }
        }
    }


    /**
     * 从服务端刷新消息,判断是否显示红点
     */
    @Background(delay = 500)
    protected void refreshActivityRedPoint() {
        httpLoader.updateActivityFlags(new HttpLoader.NormalCallBack() {
            @Override
            public void onSuccess(Object obj) {
                if ((int) obj != 0)
                    showActivityRedPoint((int) obj);
            }

            @Override
            public void onError(Object obj) {
                activitySection.setNotificationsText("");
            }

            @Override
            public void onNetworkError(Object obj) {
                activitySection.setNotificationsText("");

            }
        });

    }

    /**
     * 显示活动圈的红点
     */
    @UiThread
    protected void showActivityRedPoint(int num) {
        activitySection.setNotifications(num);
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
