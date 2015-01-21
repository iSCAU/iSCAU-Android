package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

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
public class BaseActivity extends Activity {
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getSimpleName());
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

    /**
     * 默认返回
     */
    @Click
    void back() {
        finish();
    }

    //标题栏文本框
    @ViewById
    TextView title_text;

    //标题栏右边按钮
    @ViewById
    Button more;


    /**
     * 设置页面标题
     *
     * @param text
     */
    void setTitleText(String text) {
        title_text.setText(text);
    }

    /**
     * 设置更多按钮文字
     *
     * @param text
     */
    void setMoreButtonText(String text) {
        more.setText(text);
    }

    void setMoreButtonOnClick(View.OnClickListener l) {
        more.setOnClickListener(l);
    }

    @AfterViews
    void initView() {

    }

    /**
     * 处理各种请求失败问题， 例如：
     * 查询的数据集为空，用户密码错误等等。
     *
     * @param requestCode
     */
    @UiThread
    void showErrorResult(int requestCode) {

        if (requestCode == 404) {
            AppToast.show(this, tips_empty, 0);
        } else {
            AppContext.showError(requestCode, this);
        }
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

}
