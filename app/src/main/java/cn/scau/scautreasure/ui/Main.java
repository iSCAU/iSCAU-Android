package cn.scau.scautreasure.ui;

import android.support.v4.app.Fragment;
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
        fragmentMenu = Menu_.builder().build();
        fragmentClassTable = ClassTable_.builder().build();
        fragmentSettings = Configuration_.builder().build();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == rd_features.getId()) {
                    UIHelper.startFragment(mContext, fragmentMenu, "menu_");
                    getSupportActionBar().setTitle(R.string.title_menu);
                }else if (i == rd_classtable.getId()) {
                    UIHelper.startFragment(mContext, fragmentClassTable, "classtable_");
                    getSupportActionBar().setTitle(R.string.title_classtable);
                }else if (i == rd_settings.getId()) {
                    UIHelper.startFragment(mContext, fragmentSettings, "settings_");
                    getSupportActionBar().setTitle(R.string.title_configuration);
                }
            }
        });
        UIHelper.startFragment(mContext, fragmentClassTable, "classtable_");
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
                UIHelper.startFragment(this, Notification_.builder().build(), "notification",notification);
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
}