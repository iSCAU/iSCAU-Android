package cn.scau.scautreasure.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

/**接收NotificationTiming广播后
 * 打开NotificationService
 * 避免直接操作Service会在应用管理程序中有显示
 * */

public class NotificationReceiver extends BroadcastReceiver
{
//    String date1="2014-05-21";
//    int year=2014;
//    int mon=5;
//    int day=23;
    private String time[]=new String[10];
    String now;

    public void onReceive(Context context, Intent intent) {

        Log.v("receiver","heheheaaa");
        SharedPreferences share_date=context.getSharedPreferences("no_book",Activity.MODE_PRIVATE);
        int N=share_date.getInt("date",1);

        Calendar ca = Calendar.getInstance();
        int y = ca.get(Calendar.YEAR);//获取年份
        int m=ca.get(Calendar.MONTH)+1;//获取月份（android月份是从0开始的）
        int dy=ca.get(Calendar.DATE)+N;//获取日

        String sm=m+"";
        if(m<10)
            sm="0"+sm;

        String sdy=dy+"";
        if(dy<10)
            sdy="0"+sdy;

       now=y+"-"+sm+"-"+sdy;


        Log.v("receiver",now);

        SharedPreferences share=context.getSharedPreferences("no_book", Activity.MODE_PRIVATE);
        int c=share.getInt("count",0);
        for(int i=0;i<c&&i!=9;i++){

            time[i]=share.getString("time"+i,"0000-00-00");
            time[i]=time[i].trim();

            if(time[i].equals(now)) {
                Log.v("nimabi","你麻痹为什么false");
                context.startService(new Intent(context, cn.scau.scautreasure.service.NotificationService.class));
                break;
            }
        }

    }
}
