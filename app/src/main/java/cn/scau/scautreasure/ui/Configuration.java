package cn.scau.scautreasure.ui;

import android.widget.TextView;
import cn.scau.scautreasure.AppConfig_;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.widget.ParamWidget;
import com.devspark.appmsg.AppMsg;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * 软件公告通知.
 *
 * User: special
 * Date: 13-10-5
 * Time: 下午1:36
 * Mail: specialcyci@gmail.com
 */
@EFragment(R.layout.configuration)
public class Configuration extends Common{

    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @ViewById
    ParamWidget param_server, param_classTableAsFirstScreen;

    @StringArrayRes
    String[] server;

    @StringRes
    String listitem_lable_server, listitem_lable_classTableAsFirstScreen;

    @AfterViews
    void initViews(){
        setTitle(R.string.title_configuration);
        param_server.initView(listitem_lable_server, server, 0);
        param_server.getWheel().setCurrentItem(AppContext.server - 1);
        param_classTableAsFirstScreen.initViewWithYesOrNoOption(listitem_lable_classTableAsFirstScreen,1);
        param_classTableAsFirstScreen.setYesOrNo(config.classTableAsFirstScreen().get());
        addIgnoredView(param_server);
        addIgnoredView(param_classTableAsFirstScreen);
    }

    @Click
    void btn_save(){
        int server = Integer.valueOf(param_server.getSelectedParam());
        boolean isFirstScreen = param_classTableAsFirstScreen.getYesOrNo();
        AppContext.server = server;
        config.eduServer().put(server);
        config.classTableAsFirstScreen().put(isFirstScreen);
        AppMsg.makeText(parentActivity(),R.string.tips_save_successfully,AppMsg.STYLE_INFO).show();
    }


}
