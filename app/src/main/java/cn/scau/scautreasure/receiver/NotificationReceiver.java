package cn.scau.scautreasure.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import cn.scau.scautreasure.helper.CacheHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BookModel;

/**接收NotificationTiming广播后
 * 打开NotificationService
 * 避免直接操作Service会在应用管理程序中有显示
 * */
public class NotificationReceiver extends BroadcastReceiver{
     public void onReceive(Context context, Intent intent) {
        SharedPreferences shareDate =
                context.getSharedPreferences("no_book",Activity.MODE_PRIVATE);
        int date = shareDate.getInt("date",1);
        String str = getNowDate(context, date);
        boolean flag = matchRecode(context, str);
        Log.v("test_dick",flag+"");
        if(flag){
          context.startService(new Intent(context, cn.scau.scautreasure.service.NotificationService.class));
        }
     }

     public String getNowDate(Context context, int date){
        Calendar ca = Calendar.getInstance();
        int y = ca.get(Calendar.YEAR);//获取年份
        int m=ca.get(Calendar.MONTH)+1;//获取月份（android月份是从0开始的）
        int dy=ca.get(Calendar.DATE)+date;//获取日

        String sm=m+"";
        if(m<10)
            sm="0"+sm;

        String sdy=dy+"";
        if(dy<10)
          sdy="0"+sdy;

        String now=y+"-"+sm+"-"+sdy;
        Log.v("receiver",now);

        return  now;
     }


     public boolean matchRecode(Context context, String now){
        boolean flag=false;
        CacheHelper cacheHelper = new CacheHelper(context);
        cacheHelper.setCacheKey("borrowedBook_" + UIHelper.TARGET_FOR_NOW_BORROW);
        ArrayList list = cacheHelper.loadListFromCache();
        if(list!=null) {
          for (int i = list.size() - 1; i >= 0; i--) {
               Log.v("list",  ((BookModel)list.get(i)).getShould_return_date());
               String recode=((BookModel)list.get(i)).getShould_return_date().trim();
               if(now.equals(recode)){
                    flag=true;
                    break;
               }
          }
        }else{
          flag=false;
        }
        return flag;
     }
}
