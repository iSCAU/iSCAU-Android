package cn.scau.scautreasure.ui;

import android.content.Context;
import android.media.AudioManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.RingerMode;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.util.ClassUtil;
import cn.scau.scautreasure.widget.ParamWidget;

/**
 * 设置界面吧.
 * <p/>
 * User: special
 * Date: 13-10-5
 * Time: 下午1:36
 * Mail: specialcyci@gmail.com
 */
@EFragment(R.layout.configuration)
public class Configuration extends CommonFragment implements OnTabSelectListener {

    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @ViewById
    ParamWidget  param_classTableAsFirstScreen,
            param_ringer_mode_during_class, param_ringer_mode_after_class;

    @StringArrayRes
    String[] server, ringer_mode;
    @ViewById(R.id.alert_before_class)
    CheckBox alertClass;
    @StringRes
    String listitem_lable_server, listitem_lable_classTableAsFirstScreen,
            listitem_label_ringerModeDuringClass, listitem_label_ringerModeAfterClass;
    private UmengUpdateListener umengUpdateListener = new UmengUpdateListener() {
        @Override
        public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
            switch (updateStatus) {
                case UpdateStatus.Yes: // has update
                    UmengUpdateAgent.showUpdateDialog(getSherlockActivity(), updateInfo);
                    break;
                case UpdateStatus.No: // has no update
                    Toast.makeText(getSherlockActivity(), "没有更新", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.NoneWifi: // none wifi
                    Toast.makeText(getSherlockActivity(), "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateStatus.Timeout: // time out
                    Toast.makeText(getSherlockActivity(), "超时", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @CheckedChange(R.id.alert_before_class)
    void setAlertClass(){
        config.isAlertClass().put(alertClass.isChecked());
    }
    @AfterViews
    void initViews() {

        alertClass.setChecked(config.isAlertClass().get());
        param_classTableAsFirstScreen.initViewWithYesOrNoOption(listitem_lable_classTableAsFirstScreen, 1);
        param_classTableAsFirstScreen.setYesOrNo(config.classTableAsFirstScreen().get());
        param_ringer_mode_during_class.initView(listitem_label_ringerModeDuringClass, ringer_mode, 2);
        int i = 0;
        for (RingerMode mode : RingerMode.values()) {
            if (mode.getValue() == config.duringClassRingerMode().get()) {
                param_ringer_mode_during_class.getWheel().setCurrentItem(i);
            }
            i++;
        }
        param_ringer_mode_after_class.initView(listitem_label_ringerModeAfterClass, ringer_mode, 3);
        i = 0;
        for (RingerMode mode : RingerMode.values()) {
            if (mode.getValue() == config.afterClassRingerMode().get()) {
                param_ringer_mode_after_class.getWheel().setCurrentItem(i);
            }
            i++;
        }
    }

    @Click
    void btn_notification_setting() {
        NotificationTiming_.intent(getSherlockActivity()).start();
    }

    @Click
    void btn_about() {
        About_.intent(getSherlockActivity()).start();
    }

    @Click
    void btn_update() {
        AppMsg.makeText(getSherlockActivity(),
                app.getString(R.string.tips_checking_for_update)
                , AppMsg.STYLE_INFO).show();
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(umengUpdateListener);
        UmengUpdateAgent.forceUpdate(getSherlockActivity());
    }

    @Click
    void btn_change_account() {
        Login_.intent(this).start();
    }

    @Click
    void btn_save() {

        boolean isFirstScreen = param_classTableAsFirstScreen.getYesOrNo();
        config.classTableAsFirstScreen().put(isFirstScreen);
        RingerMode[] modes = RingerMode.values();
        RingerMode duringMode = modes[param_ringer_mode_during_class.getWheel().getCurrentItem()];
        RingerMode afterMode = modes[param_ringer_mode_after_class.getWheel().getCurrentItem()];
        boolean needUpdateAlarm = false;
        if (RingerMode.isSet(config.duringClassRingerMode().get()) != RingerMode.isSet(duringMode.getValue())
                || RingerMode.isSet(config.afterClassRingerMode().get()) != RingerMode.isSet(afterMode.getValue())) {
            needUpdateAlarm = true;
        }
        config.duringClassRingerMode().put(duringMode.getValue());
        config.afterClassRingerMode().put(afterMode.getValue());
        if (needUpdateAlarm) {
            RingerMode.duringClassOn(getActivity(), duringMode, -1);
            RingerMode.afterClassOn(getActivity(), afterMode, 1);
        }
        if (RingerMode.isSet(duringMode.getValue()) || RingerMode.isSet(afterMode.getValue())) {
            RingerMode.setDateChangedAlarm(getActivity());
        } else {
            RingerMode.cancelDateChangedAlarm(getActivity());
        }
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (ClassUtil.isDuringClassNow(getActivity())) {
            audioManager.setRingerMode(duringMode.getValue());
        } else {
            audioManager.setRingerMode(afterMode.getValue());
        }
        AppMsg.makeText(parentActivity(), R.string.tips_save_successfully, AppMsg.STYLE_INFO).show();
    }

    @Override
    public void onTabSelect() {
        setTitle(R.string.title_configuration);
        setSubTitle(null);
    }
}
