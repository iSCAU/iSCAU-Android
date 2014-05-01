package cn.scau.scautreasure.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.util.DateUtil;
import cn.scau.scautreasure.widget.ResideMenu;
import cn.scau.scautreasure.widget.ResideMenuItem;

/**
 * 软件启动的首页, 就是目前的菜单页
 *
 * User:  Special Leung
 * Date:  13-7-28
 * Time:  下午9:11
 * Mail:  specialcyci@gmail.com
 */
@EActivity(R.layout.menu)
public class Main extends ActionBarActivity implements View.OnClickListener{

    @Pref cn.scau.scautreasure.AppConfig_ config;
    @App AppContext app;
    @Bean DateUtil dateUtil;
    private Context context;
    private ResideMenu resideMenu;
    private ResideMenuItem resideMenuItemHome;
    private ResideMenuItem resideMenuItemProfile;
    private ResideMenuItem resideMenuItemSettings;

    @AfterViews
    void hideActionBar(){
        context = this;
        getSupportActionBar().hide();
    }

    @AfterViews
    void initView(){
        initMobclickAgent();
        setUpMenu();
        checkForUpdate();
        showWelcome();
        showNotification();
    }

    private void initMobclickAgent(){
        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.openActivityDurationTrack(false);
    }

    private void setUpMenu(){
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        resideMenuItemHome     = new ResideMenuItem(this, R.drawable.menu_icon_home, R.string.menu_home);
        resideMenuItemProfile  = new ResideMenuItem(this, R.drawable.menu_icon_user, R.string.menu_profile);
        resideMenuItemSettings = new ResideMenuItem(this, R.drawable.menu_icon_settings, R.string.menu_settings);
        resideMenuItemHome.setOnClickListener(this);
        resideMenuItemProfile.setOnClickListener(this);
        resideMenuItemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(resideMenuItemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(resideMenuItemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(resideMenuItemSettings, ResideMenu.DIRECTION_LEFT);

        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void showWelcome(){
        menu_about();
        showMenu();
    }

    private void checkForUpdate() {
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
    }

    @UiThread(delay = 2000)
    void showMenu(){
        // if user has set the class table as the first screen.
        if (config.classTableAsFirstScreen().get()){
            UIHelper.startFragment(this, ClassTable_.builder().build());
        }else{
            getResideMenu().openMenu(ResideMenu.DIRECTION_LEFT);
        }
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.onInterceptTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!isKeyBack(event))
            return super.onKeyDown(keyCode,event);
        if(isDialogShowing() || !isEmptyBackStackEntry())
            return super.onKeyDown(keyCode,event);

        if(!resideMenu.isOpened()){
            resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
        }else{
            menu_exit();
        }
        return true;
    }

    @OptionsItem(android.R.id.home)
    public void home(){
        if(isEmptyBackStackEntry()){
            resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
        }else{
            getSupportFragmentManager().popBackStack();
        }
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
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
        getResideMenu().openMenu(ResideMenu.DIRECTION_LEFT);
    }

    private UmengUpdateListener umengUpdateListener = new UmengUpdateListener() {
        @Override
        public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
            switch (updateStatus) {
                case UpdateStatus.Yes: // has update
                    UmengUpdateAgent.showUpdateDialog(context, updateInfo);
                    break;
                case UpdateStatus.No: // has no update
                    Toast.makeText(context, "没有更新", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.NoneWifi: // none wifi
                    Toast.makeText(context, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.Timeout: // time out
                    Toast.makeText(context, "超时", Toast.LENGTH_SHORT).show();
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

    /**
     * check if the developer call to force update;
     */
//    private void checkForceUpdateConfig(){
//        String enforceupdate = MobclickAgent.getConfigParams(this, "enforceupdate");
//        if (isConfigAble(enforceupdate))
//            Toast.makeText(this,R.string.tips_forceupdate,Toast.LENGTH_LONG).show();
//    }

    private boolean isConfigAble(String config){
        return !config.trim().equals("0") && !config.trim().equals("");
    }

    @OptionsItem
    void menu_classtable(){
        UIHelper.startFragment(this, ClassTable_.builder().build());
    }

    @OptionsItem
    void menu_goal(){
        UIHelper.startFragment(this, Param_.builder().build(),"target","goal","targetFragment",Goal_.class.getName());
    }

    @OptionsItem
    void menu_exam(){
        UIHelper.startFragment(this, Exam_.builder().build());
    }

    @OptionsItem
    void menu_pickCourseInfo(){
        UIHelper.startFragment(this, PickClassInfo_.builder().build());
    }

    @OptionsItem
    void menu_emptyClassRoom(){
        UIHelper.startFragment(this, Param_.builder().build(),"target","emptyclassroom","targetFragment",EmptyClassRoom_.class.getName());
    }

    @OptionsItem
    void menu_searchBook(){
        UIHelper.startFragment(this, SearchBook_.builder().build());
    }

    @OptionsItem
    void menu_nowBorrowedBook(){
        UIHelper.startFragment(this, BorrowedBook_.builder().build(),"target",UIHelper.TARGET_FOR_NOW_BORROW);
    }

    @OptionsItem
    void menu_pastBorrowedBook(){
        UIHelper.startFragment(this, BorrowedBook_.builder().build(),"target",UIHelper.TARGET_FOR_PAST_BORROW);
    }

    @OptionsItem
    void menu_lifeinformation(){
        UIHelper.startFragment(this, Introduction_.builder().build(),"target","LifeInformation","title",R.string.menu_lifeinformation);
    }

    @OptionsItem
    void menu_communityinformation(){
        UIHelper.startFragment(this, Introduction_.builder().build(),"target","CommunityInformation","title",R.string.menu_communityinformation);
    }

    @OptionsItem
    void menu_guardianserves(){
        UIHelper.startFragment(this, Introduction_.builder().build(),"target","GuardianServes","title",R.string.menu_guardianserves);
    }

    @OptionsItem
    void menu_studyinformation(){
        UIHelper.startFragment(this, Introduction_.builder().build(),"target","StudyInformation","title",R.string.menu_studyinformation);
    }

    @OptionsItem
    void menu_busandtelphone(){
        UIHelper.startFragment(this, Introduction_.builder().build(),"target","Bus&Telphone","title",R.string.menu_busandtelphone);
    }

    @OptionsItem
    void menu_calendar(){
        UIHelper.startFragment(this, Calendar_.builder().build());
    }

    @OptionsItem
    void menu_notice(){
        UIHelper.startFragment(this, Notice_.builder().build());
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
    void menu_about(){
        UIHelper.startFragment(this,Welcome_.builder().build());
    }

    @OptionsItem
    void menu_exit(){
        MobclickAgent.onKillProcess(this);
        System.exit(0);
    }

    @Override
    public void onClick(View view) {
        if (view == resideMenuItemHome){
            Toast.makeText(this, "yes", Toast.LENGTH_LONG).show();
        }
    }
}