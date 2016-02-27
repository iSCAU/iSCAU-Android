package cn.scau.scautreasure.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.avos.avoscloud.LogUtil;
import com.devspark.appmsg.AppMsg;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.HttpStatusCodeException;

import java.sql.Time;
import java.util.ArrayList;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.api.FunctionApi;
import cn.scau.scautreasure.api.SchoolActivityApi;
import cn.scau.scautreasure.helper.CacheHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.model.ActivityCountModel;
import cn.scau.scautreasure.model.FunctionModel;
import cn.scau.scautreasure.util.DateUtil;
import cn.scau.scautreasure.widget.BadgeView;

/**
 * 主页面;
 * <p/>
 * User:  Special Leung
 * Date:  13-7-28
 * Time:  下午9:11
 * Mail:  specialcyci@gmail.com
 */
@EActivity(R.layout.main)
public class Main extends ActionBarActivity {

    private static final String MENU_TAG = "menu_";
    private static final String CLASSTABLE_TAG = "classtable_";
    private static final String SETTINGS_TAG = "settings_";

    @Pref
    cn.scau.scautreasure.AppConfig_ config;
    @App
    AppContext app;
    @Bean
    DateUtil dateUtil;
    @ViewById
    RadioGroup radioGroup;
    @ViewById
    RadioButton rd_classtable, rd_features, rd_activity;
    @ViewById
    Button bt_classtable, bt_features, bt_activity;
    @RestService
    SchoolActivityApi api;

    @RestService
    FunctionApi functionApi;


    Fragment fragmentMenu;
    Fragment fragmentClassTable;
    Fragment FoundFragment;


    private ActionBarActivity mContext;
    private int checkedId;
    private long exitTime = 0;

    @AfterInject
    void init() {
        mContext = this;
    }

    @AfterViews
    void initView() {
        setUpTab();
        initMobclickAgent();
        checkForUpdate();
        showNotification();

        loadFunction();
    }

    //加载动态功能
    @Background
    void loadFunction() {
        try {
            FunctionModel.FunctionList functionList = functionApi.functionList();
            ArrayList<FunctionModel> list = functionList.getResult();
            if (list != null) {
                CacheHelper.writeList(this, "function", list);
            }

        } catch (Exception e) {

        }
    }






    private void setUpTab() {
        if (fragmentMenu == null) {
            fragmentMenu = Menu_.builder().build();
        }
        if (fragmentClassTable == null) {
            fragmentClassTable = ClassTable_.builder().build();
        }
        if (FoundFragment == null) {
            FoundFragment = FoundFragment_.builder().build();
        }
        if (checkedId != 0) {
            //恢复到Activity被杀前的选中状态
            radioGroup.check(checkedId);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

                checkedId = i;
                if (i == rd_features.getId()) {
                    UIHelper.startFragment(mContext, fragmentMenu, MENU_TAG);
                    ((OnTabSelectListener) fragmentMenu).onTabSelect();


                } else if (i == rd_classtable.getId()) {
                    UIHelper.startFragment(mContext, fragmentClassTable, CLASSTABLE_TAG);
                    ((OnTabSelectListener) fragmentClassTable).onTabSelect();

                } else if (i == rd_activity.getId()) {

                    UIHelper.startFragment(mContext, FoundFragment, SETTINGS_TAG);
                    ((OnTabSelectListener) FoundFragment).onTabSelect();

                }
            }
        });

        UIHelper.startFragment(mContext, fragmentClassTable, CLASSTABLE_TAG);
        rd_classtable.setChecked(true);


    }

    boolean checkTime() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);

        if ((hour >= 11) && (hour <= 12) || ((hour >= 17) && (hour <= 18))) {
            Log.i("当前时间:", String.valueOf(hour) + "时,切换外卖");
            return true;
        }
        Log.i("当前时间:", String.valueOf(hour) + "时,切换课表");
        return false;

    }

    private void initMobclickAgent() {
        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.openActivityDurationTrack(false);
        // 检查反馈消息;
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();
    }

    private void checkForUpdate() {
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
    }

    @UiThread(delay = 4000)
    void showNotification() {
        String notification = MobclickAgent.getConfigParams(this, "notification");
        if (isConfigAble(notification)) {
            // 今天显示过就不显示了
            if (!config.lastSeeNotificationDate().get().equals(dateUtil.getCurrentDateString()))
                Notification_.intent(this).notification(notification).start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getGroupId()) {
            case R.id.group_edusys:
                return ensureExisted(AppContext.eduSysPassword, R.string.tips_main_edusyspassword_not_existed);

            case R.id.group_lib:
                if (item.getItemId() != R.id.menu_searchBook)
                    return ensureExisted(AppContext.libPassword, R.string.tips_main_libpassword_not_existed);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean ensureExisted(String targetString, int notExistedTipsString) {
        if (targetString == null || targetString.equals("")) {
            AppMsg.makeText(this, notExistedTipsString, AppMsg.STYLE_ALERT).show();
            return true;
        }
        return false;
    }

    @OnActivityResult(UIHelper.QUERY_FOR_EDIT_ACCOUNT)
    void onEditAccountResult() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            checkedId = savedInstanceState.getInt("checkedId");
            restoreFragments();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存当前选中的页面
        outState.putInt("checkedId", checkedId);
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

    private boolean isConfigAble(String config) {
        return !config.trim().equals("0") && !config.trim().equals("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MobclickAgent.onKillProcess(this);
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 尝试恢复程序在后台被杀前的fragments
     */
    private void restoreFragments() {
        FragmentManager fm = getSupportFragmentManager();
        fragmentMenu = fm.findFragmentByTag(MENU_TAG);
        fragmentClassTable = fm.findFragmentByTag(CLASSTABLE_TAG);
        FoundFragment = fm.findFragmentByTag(SETTINGS_TAG);

    }


}