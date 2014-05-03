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
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.util.DateUtil;

/**
 * 整个软件主体的Activity
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
                if(i == rd_features.getId())
                    UIHelper.startFragment(mContext, fragmentMenu, "menu_");
                else if( i == rd_classtable.getId())
                    UIHelper.startFragment(mContext, fragmentClassTable, "classtable_");
                else if( i == rd_settings.getId())
                    UIHelper.startFragment(mContext, fragmentSettings, "settings_");
            }
        });
        UIHelper.startFragment(mContext, fragmentClassTable, "classtable_");
    }

    private void initMobclickAgent(){
        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.openActivityDurationTrack(false);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!isKeyBack(event))
            return super.onKeyDown(keyCode,event);
        if(isDialogShowing() || !isEmptyBackStackEntry())
            return super.onKeyDown(keyCode,event);

        menu_exit();
        return true;
    }

    @OptionsItem(android.R.id.home)
    public void home(){
    }

    private boolean isEmptyBackStackEntry(){
        getSupportFragmentManager().executePendingTransactions();
        return getSupportFragmentManager().getBackStackEntryCount() == 0;
    }

    private boolean isDialogShowing() {
        return UIHelper.getDialog() != null && UIHelper.getDialog().isShowing();
    }

    private boolean isKeyBack(KeyEvent event){
        return event.getKeyCode() == KeyEvent.KEYCODE_BACK;
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

    private UmengUpdateListener umengUpdateListener = new UmengUpdateListener() {
        @Override
        public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
            switch (updateStatus) {
                case UpdateStatus.Yes: // has update
                    UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
                    break;
                case UpdateStatus.No: // has no update
                    Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.NoneWifi: // none wifi
                    Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.Timeout: // time out
                    Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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

    @OptionsItem
    void menu_account(){
        Login_.intent(this).isStartFormMenu(true).startForResult(UIHelper.QUERY_FOR_EDIT_ACCOUNT);
    }

    @OptionsItem
    void menu_configuration(){
        UIHelper.startFragment(this, Configuration_.builder().build());
    }

    @OptionsItem
    void menu_update(){
        Toast.makeText(this,R.string.loading_default,Toast.LENGTH_LONG).show();
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(umengUpdateListener);
        UmengUpdateAgent.forceUpdate(this);
    }


    @OptionsItem
    void menu_exit(){
        MobclickAgent.onKillProcess(this);
        System.exit(0);
    }

}