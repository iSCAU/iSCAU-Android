package cn.scau.scautreasure.util;

import android.content.Context;
import android.content.Intent;

import cn.scau.scautreasure.service.AlertClassSerice_;

/**
 * Created by xillkey on 2014/9/2.
 */
public class AlertClassUtil {
    //开启提醒服务
    public static void startAlertClass(Context context){
        Intent toStartAlertClassIntent = new Intent(context,AlertClassSerice_.class);
        context.getApplicationContext().startService(toStartAlertClassIntent);

    }
}
