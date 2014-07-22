package cn.scau.scautreasure.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.util.DateUtil;

/**
 * 主页面;
 *
 * User:  Special Leung
 * Date:  13-7-28
 * Time:  下午9:11
 * Mail:  specialcyci@gmail.com
 */
@EActivity(R.layout.main)
public class Main extends ActionBarActivity{

    @Pref
    cn.scau.scautreasure.AppConfig_ config;
    @App
    AppContext app;
    @Bean
    DateUtil dateUtil;
    @ViewById
    RadioGroup radioGroup;
    @ViewById
    RadioButton rd_classtable, rd_features, rd_settings;
    private ActionBarActivity mContext;
    Fragment fragmentMenu;
    Fragment fragmentClassTable;
    Fragment fragmentSettings;
    private int checkedId;
    private static final String MENU_TAG = "menu_";
    private static final String CLASSTABLE_TAG = "classtable_";
    private static final String SETTINGS_TAG = "settings_";

    @AfterInject
    void init(){
        mContext = this;
    }

    @AfterViews
    void initView(){
        setUpTab();
        initMobclickAgent();
        checkForUpdate();
        showNotification();
    }

    private void setUpTab() {
        if(fragmentMenu == null){
            fragmentMenu = Menu_.builder().build();
        }
        if(fragmentClassTable == null){
            fragmentClassTable = ClassTable_.builder().build();
        }
        if(fragmentSettings == null){
            fragmentSettings = Configuration_.builder().build();
        }
        if(checkedId != 0){
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
                    ( (OnTabSelectListener) fragmentMenu).onTabSelect();
                }else if (i == rd_classtable.getId()) {
                    UIHelper.startFragment(mContext, fragmentClassTable, CLASSTABLE_TAG);
                    ( (OnTabSelectListener) fragmentClassTable).onTabSelect();
                }else if (i == rd_settings.getId()) {
                    UIHelper.startFragment(mContext, fragmentSettings, SETTINGS_TAG);
                    ( (OnTabSelectListener) fragmentSettings).onTabSelect();
                }
            }
        });
        if(checkedId == 0){
            UIHelper.startFragment(mContext, fragmentClassTable, CLASSTABLE_TAG);
        }
    }

    private void initMobclickAgent(){
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
    void showNotification(){
        String notification = MobclickAgent.getConfigParams(this, "notification");
        if(isConfigAble(notification)){
            // 今天显示过就不显示了
            if(!config.lastSeeNotificationDate().get().equals(dateUtil.getCurrentDateString()))
                Notification_.intent(this).notification(notification).start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getGroupId()){
            case R.id.group_edusys:
                return ensureExisted(AppContext.eduSysPassword, R.string.tips_main_edusyspassword_not_existed);

            case R.id.group_lib:
                if(item.getItemId() != R.id.menu_searchBook)
                    return ensureExisted(AppContext.libPassword, R.string.tips_main_libpassword_not_existed);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean ensureExisted(String targetString, int notExistedTipsString) {
        if (targetString == null || targetString.equals("")){
            AppMsg.makeText(this,notExistedTipsString,AppMsg.STYLE_ALERT).show();
            return true;
        }
        return false;
    }

    @OnActivityResult(UIHelper.QUERY_FOR_EDIT_ACCOUNT)
    void onEditAccountResult(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
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

    private boolean isConfigAble(String config){
        return !config.trim().equals("0") && !config.trim().equals("");
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else{
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
    private void restoreFragments(){
        FragmentManager fm = getSupportFragmentManager();
        fragmentMenu = fm.findFragmentByTag(MENU_TAG);
        fragmentClassTable = fm.findFragmentByTag(CLASSTABLE_TAG);
        fragmentSettings = fm.findFragmentByTag(SETTINGS_TAG);
    }
}