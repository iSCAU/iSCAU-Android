package cn.scau.scautreasure.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;


import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.CacheHelper;

import cn.scau.scautreasure.util.CryptUtil;
import cn.scau.scautreasure.widget.AppProgress;
import cn.scau.scautreasure.widget.AppToast;

/**
 * Created by macroyep on 15/1/17.
 * Time:11:45
 */
@EActivity
@OptionsMenu(R.menu.menu_next)
public class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
    }


    private String getSimpleName() {
        return ((Object) this).getClass().getSimpleName();
    }

    @App
    AppContext app;
    protected BaseAdapter adapter;
    protected String tips_empty = "没有找到你需要的信息";
    @Bean
    CryptUtil cryptUtil;

    @Bean
    CacheHelper cacheHelper;

    /**
     * 设置当查询的数据集为空的时候的提示语。
     *
     * @param tips_text
     */
    protected void setDataEmptyTips(String tips_text) {
        this.tips_empty = tips_text;
    }

    @AfterViews
    void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.ic_arrow_back_white_24dp);
    }

    @OptionsItem(android.R.id.home)
    void home() {
        this.finish();
    }


    /**
     * 设置页面标题
     *
     * @param text
     */
    void setTitleText(String text) {
        getSupportActionBar().setTitle(text);
    }


    /**
     * 处理各种请求失败问题， 例如：
     * 查询的数据集为空，用户密码错误等等。
     *
     * @param requestCode
     */
    @UiThread
    void showErrorResult(ActionBarActivity actionBarActivity, int requestCode) {

        if (requestCode == 404) {
            AppToast.show(actionBarActivity, tips_empty, 0);
        } else {
            AppContext.showError(requestCode, actionBarActivity);
        }
    }

    /**
     * 老版本是大多数fragment, 并且主要用这个函数获取context,
     * 为了减少点代码这函数继续保留了
     *
     * @return
     */
    protected ActionBarActivity getSherlockActivity() {
        return this;
    }

    /**
     * 处理没有网络连接的情况。
     */
    @UiThread
    void handleNoNetWorkError() {
        AppProgress.hide();
        AppToast.show(this, "网络错误，请检查您的网络状态", 0);

    }

    @UiThread
    void toast(String msg) {
        AppToast.show(this, msg, 0);
    }

    @OptionsMenuItem(R.id.menu_done)
    MenuItem menu_done;

    @OptionsItem(R.id.menu_done)
    void menu_done() {
        doMoreButtonAction();
    }


    void doMoreButtonAction() {

    }

    //更多按钮文字
    private String buttonText = "";

    private boolean moreButtonVisible = true;

    public MenuItem getMenu_done() {
        return menu_done;
    }

    public String getMoreButtonText() {
        return buttonText;
    }

    public void setMoreButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public boolean isMoreButtonVisible() {
        return moreButtonVisible;
    }

    public void setMoreButtonVisible(boolean moreButtonVisible) {
        this.moreButtonVisible = moreButtonVisible;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu_done.setTitle(buttonText);
        menu_done.setVisible(moreButtonVisible);

        return true;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getSimpleName());
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getSimpleName());
        MobclickAgent.onPause(this);
    }
}
