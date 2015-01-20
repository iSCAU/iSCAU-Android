package cn.scau.scautreasure.ui;

import android.support.v4.app.Fragment;

import com.devspark.appmsg.AppMsg;

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
    @App
    AppContext app;

    @Bean
    HttpLoader httpLoader;

    /**
     * 根据网络返回显示错误类型
     *
     * @param requestCode
     */
    @UiThread
    void showError(int requestCode) {
        if (requestCode == 404) {
            AppToast.info(getActivity(), "没有找到你需要的信息");
        } else {
            AppContext.showError(requestCode, getActivity());
        }
    }

    @UiThread
    void showNetWorkError(Object obj) {
        AppToast.error(getActivity(), "网络错误,请检查网络状态");
    }

}
