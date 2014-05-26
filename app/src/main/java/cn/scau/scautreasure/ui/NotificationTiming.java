package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import org.androidannotations.annotations.EActivity;

import java.util.Calendar;
import cn.scau.scautreasure.R;

/**负责每天通知时间的设置
 * 通知时间写进@FILENAME="alarmtime"
 * */
@EActivity
public class NotificationTiming extends Activity {

    private static final String FILENAME="alarmtime";
    public static final long dailytime=24*60*60*1000;
    private long howlong=0;
    private int mHour=0;
    private int mMin=0;

    private Button btn_time_setting;
    private ToggleButton btn_on_off;
    AlarmManager am;
    PendingIntent pi;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.notification_setting);

        this.btn_time_setting=(Button)super.findViewById(R.id.btn_time_setting);
        this.btn_on_off=(ToggleButton)super.findViewById(R.id.btn_on_off);

        howlong=count();

        if(howlong!=-1) {

            setNotification();
            Log.v("set_succeed","heheh");
        }

        this.btn_time_setting.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                    Dialog dialog = new TimePickerDialog(NotificationTiming.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    mHour = hourOfDay;
                                    mMin = minute;

                                    btn_time_setting.setText("每日提醒（前一天" + formate(mHour, mMin) + ")");
                                    setShare(mHour, mMin);
                                    am.cancel(pi);
                                    howlong = count();
                                    setNotification();

                                   btn_on_off.setChecked(false);
                                }
                            }, 20, 0, true
                    );
                    dialog.show();
            }
        });

        this.btn_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(btn_on_off.isChecked()){
                    mHour=-1;
                    mMin=-1;

                    setShare(mHour,mMin);
                    am.cancel(pi);

                    btn_time_setting.setText("每日提醒");
                }else{
                    mHour=20;
                    mMin=0;

                    setShare(mHour,mMin);
                    howlong=count();
                    setNotification();

                    btn_time_setting.setText("每日提醒（前一天" +formate(mHour,mMin)+")");
                }
            }
        });
    }

    public void setShare(int mHour,int mMin){
        SharedPreferences share=NotificationTiming.super.getSharedPreferences(FILENAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=share.edit();
        editor.putInt("hour",mHour);
        editor.putInt("min",mMin);
        editor.putInt("date",1);//提前几天通知，留着这东西在，还没完善
        editor.commit();
        Log.v("share","s");
        Log.v("wtime",mHour+":"+mMin);
    }

    public long count(){
        SharedPreferences shared=NotificationTiming.super.getSharedPreferences(FILENAME,Activity.MODE_PRIVATE);
        int hour=shared.getInt("hour", 20);
        int min=shared.getInt("min", 0);

        if(hour==-1){
            return -1;
        }

        btn_time_setting.setText("每日提醒（前一天" +formate(hour,min)+")");

        Calendar ca = Calendar.getInstance();
        int h=ca.get(Calendar.HOUR_OF_DAY);//24小时制小时
        /*为何不是24进制啊*/
        int m=ca.get(Calendar.MINUTE);//分

        if((howlong=(hour-h)*60*60*1000+(min-m)*60*1000)<0)
            howlong+=dailytime;

        Log.v("now",h+" "+m);
        Log.v("count_succeed",howlong+"");

        return howlong;
    }

    public void setNotification(){



        am = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,cn.scau.scautreasure.receiver.NotificationReceiver.class);
        pi = PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + howlong, dailytime, pi);

        Log.v("wait_succeed","heheh");
    }
    public String formate(int hour,int min){
        String sHour=hour+"";
        String sMin=min+"";
        if(hour<10)
            sHour="0"+sHour;
        if(min<10)
            sMin="0"+sMin;
        return sHour+":"+sMin;
    }
}
