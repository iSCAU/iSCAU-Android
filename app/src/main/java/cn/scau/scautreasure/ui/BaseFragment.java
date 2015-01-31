package cn.scau.scautreasure.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;


import com.umeng.analytics.MobclickAgent;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.helper.HttpLoader;
import cn.scau.scautreasure.widget.AppToast;

/**
 * Created by macroyep on 15/1/17.
 * Time:01:51
 */
@EFragment
public class BaseFragment extends Fragment {

    protected boolean isAfterViews = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAfterViews = false;
    }

    @App
    AppContext app;

    @Bean
    HttpLoader httpLoader;

    ActionBar getTheActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }


    /**
     * 根据网络返回显示错误类型
     *
     * @param requestCode
     */
    @UiThread
    void showError(int requestCode) {
        if (requestCode == 404) {
            AppToast.show(getActivity(), "没有找到你需要的信息", 0);
        } else {
            AppContext.showError(requestCode, getActivity());
        }
    }

    @UiThread
    void showNetWorkError(Object obj) {
        AppToast.show(getActivity(), "网络错误,请检查网络状态", 0);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getSimpleName()); //统计页面
        MobclickAgent.onResume(getActivity());          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getSimpleName()); // 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(getActivity());
    }


    private String getSimpleName() {
        return getClass().getSimpleName();
    }

}
