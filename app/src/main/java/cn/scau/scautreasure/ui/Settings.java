package cn.scau.scautreasure.ui;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.widgets.SnackBar;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.w3c.dom.Text;


import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.RingerMode;
import cn.scau.scautreasure.helper.NetworkHelper;
import cn.scau.scautreasure.service.AlertClassSerice_;

import cn.scau.scautreasure.util.ClassUtil;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.AppViewDrawable;
import cn.scau.scautreasure.widget.ToggleButton;

/**
 * Created by apple on 14-9-3.
 */
@EActivity((R.layout.configuration))
public class Settings extends BaseActivity {


    //情景模式数组
    @StringArrayRes
    String[] ringer_mode;
    /**
     * 课前提醒开关
     */
    @ViewById(R.id.alert_before_class)
    Switch alert_before_class;

    /**
     * 上课情景模式
     */
    @ViewById(R.id.during_class_mode_text)
    TextView during_class_mode_text;

    /**
     * 下课情景模式
     */
    @ViewById(R.id.after_class_mode_text)
    TextView after_class_mode_text;

    @ViewById(R.id.next_0)
    TextView next_0;

    @ViewById(R.id.next_1)
    TextView next_1;

    @ViewById(R.id.next_2)
    TextView next_2;

    /**
     * 检查更新
     */
    @Click(R.id.btn_update)
    void update() {
        if (app.net.getCurrentNetType(this) == NetworkHelper.NetworkType.NONE) {//无网络
            AppToast.show(this, "无网络连接", 0);
        } else {
            UmengUpdateAgent.setUpdateOnlyWifi(false);
            UmengUpdateAgent.update(this);
            UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                @Override
                public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
                    switch (updateStatus) {
                        case UpdateStatus.Yes: // has update
                            UmengUpdateAgent.showUpdateDialog(Settings.this, updateResponse);
                            break;
                        case UpdateStatus.No: // has no update
                            AppToast.show(Settings.this, "已是最新版本", 0);
                            break;
                        case UpdateStatus.NoneWifi: // none wifi
                            break;
                        case UpdateStatus.Timeout: // time out
                            AppToast.show(Settings.this, "连接超时,请稍候再试", 0);
                            break;
                    }
                }
            });
        }
    }


    /**
     * 关于我们
     */
    @Click(R.id.btn_about)
    void about() {
        About_.intent(this).start();

    }

    /**
     * 课前提醒
     */
    void setAlertClass() {
        if (app.config.isAlertClass().get()) {
            alert_before_class.setChecked(true);
        } else {
            alert_before_class.setChecked(false);
        }
        alert_before_class.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean check) {
                if (check) {
                    app.config.isAlertClass().put(true);
                    Intent sevice = new Intent(Settings.this, AlertClassSerice_.class);
                    Settings.this.startService(sevice);
                    AppToast.show(Settings.this, "已开启课前提醒", 0);

                } else {
                    app.config.isAlertClass().put(false);
                    AlertClassSerice_.intent(Settings.this).stop();
                    AppToast.show(Settings.this, "已关闭课前提醒", 0);

                }
            }
        });


    }

    //显示上课时情景模式
    void setDuringClassMode() {
        int i = 0;
        for (RingerMode mode : RingerMode.values()) {
            if (mode.getValue() == app.config.duringClassRingerMode().get()) {
                during_class_mode_text.setText(ringer_mode[i]);
            }
            i++;
        }
    }

    //显示下课时情景模式
    void setAfterClassMode() {
        int i = 0;
        for (RingerMode mode : RingerMode.values()) {
            if (mode.getValue() == app.config.afterClassRingerMode().get()) {
                after_class_mode_text.setText(ringer_mode[i]);
            }
            i++;
        }
    }

    /**
     * 设置上课时情景模式
     */
    @Click(R.id.during_class)
    void during_class() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_Notitle);
        View view = (View) LayoutInflater.from(this).inflate(
                R.layout.layout_listview, null);
        ListView lang_listView = (ListView) view.findViewById(R.id.listView);
        final List<RingModeModel> data = new ArrayList<RingModeModel>();
        for (int i = 0; i < ringer_mode.length; i++) {
            data.add(new RingModeModel(ringer_mode[i], i));
        }
        lang_listView.setAdapter(new RingModeListViewAdapter(this, data));
        lang_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                during_class_mode_text.setText(data.get(i).getModeName());

                RingerMode[] modes = RingerMode.values();

                RingerMode duringMode = modes[i];
                boolean needUpdateAlarm = false;
                if (RingerMode.isSet(app.config.duringClassRingerMode().get()) != RingerMode.isSet(duringMode.getValue())) {
                    needUpdateAlarm = true;
                }
                app.config.duringClassRingerMode().put(duringMode.getValue());
                if (needUpdateAlarm) {
                    RingerMode.duringClassOn(Settings.this, duringMode, 1);
                }
                if (RingerMode.isSet(duringMode.getValue())) {
                    RingerMode.setDateChangedAlarm(Settings.this);
                } else {
                    RingerMode.cancelDateChangedAlarm(Settings.this);
                }
                AudioManager audioManager = (AudioManager) Settings.this.getSystemService(Context.AUDIO_SERVICE);

                audioManager.setRingerMode(duringMode.getValue());

                dialog.dismiss();
            }
        });
        dialog.setContentView(view);

        dialog.show();

    }

    /**
     * 设置下课时情景模式
     */
    @Click(R.id.after_class)
    void after_class() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_Notitle);
        View view = (View) LayoutInflater.from(this).inflate(
                R.layout.layout_listview, null);
        ListView lang_listView = (ListView) view.findViewById(R.id.listView);
        final List<RingModeModel> data = new ArrayList<RingModeModel>();
        for (int i = 0; i < ringer_mode.length; i++) {
            data.add(new RingModeModel(ringer_mode[i], i));
        }
        lang_listView.setAdapter(new RingModeListViewAdapter(this, data));
        lang_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                after_class_mode_text.setText(data.get(i).getModeName());

                RingerMode[] modes = RingerMode.values();

                RingerMode afterMode = modes[i];
                boolean needUpdateAlarm = false;
                if (RingerMode.isSet(app.config.afterClassRingerMode().get()) != RingerMode.isSet(afterMode.getValue())) {
                    needUpdateAlarm = true;
                }
                app.config.afterClassRingerMode().put(afterMode.getValue());
                if (needUpdateAlarm) {
                    RingerMode.afterClassOn(Settings.this, afterMode, 1);
                }
                if (RingerMode.isSet(afterMode.getValue())) {
                    RingerMode.setDateChangedAlarm(Settings.this);
                } else {
                    RingerMode.cancelDateChangedAlarm(Settings.this);
                }
                AudioManager audioManager = (AudioManager) Settings.this.getSystemService(Context.AUDIO_SERVICE);

                audioManager.setRingerMode(afterMode.getValue());

                dialog.dismiss();

            }


        });
        dialog.setContentView(view);

        dialog.show();

    }

    @Click(R.id.btn_contact)
    void btn_contact() {
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.startFeedbackActivity();
    }

    @AfterViews
    void initViews() {
        setTitleText("设置");
        setAlertClass();
        setDuringClassMode();
        setAfterClassMode();

        TextView[] textViews = {next_0, next_1, next_2, during_class_mode_text, after_class_mode_text, btn_change_accoun_text};
        for (TextView tv : textViews) {
            AppViewDrawable.build(tv, 2, 0, 0, 20, 20);
        }
        btn_change_accoun_text.setText(app.config.userName().get());
    }

   /* @Click
    void btn_notification_setting() {
        NotificationTiming_.intent(getSherlockActivity()).start();
    }*/

    /**
     * 更改帐号设置
     */
    @Click(R.id.btn_change_account)
    void btn_change_account() {
        Login_.intent(this).start();
    }

    @ViewById(R.id.btn_change_accoun_text)
    TextView btn_change_accoun_text;

    class RingModeModel {
        private String modeName;
        private int index;

        RingModeModel(String modeName, int index) {
            this.modeName = modeName;
            this.index = index;
        }

        public String getModeName() {
            return modeName;
        }

        public void setModeName(String modeName) {
            this.modeName = modeName;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    class RingModeListViewAdapter extends BaseAdapter {

        private Context context;
        private List<RingModeModel> data;

        RingModeListViewAdapter(Context context, List<RingModeModel> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.text_list_item, null);
                holder.textView = (TextView) view.findViewById(R.id.text);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.textView.setText(data.get(i).getModeName());
            return view;
        }


        class ViewHolder {
            public TextView textView;

        }
    }
}



