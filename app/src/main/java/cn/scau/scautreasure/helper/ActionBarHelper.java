package cn.scau.scautreasure.helper;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarImplJB;
import android.support.v7.app.ActionBarImplJBMR2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by special on 14-5-11.
 */
public class ActionBarHelper {


    /**
     *
     * 本方法用于强制把 Action Bar Tab 强制放在 Action Bar。
     *
     *   原理：通过 Java 反射功能调用私有函数 setHasEmbeddedTabs
     *        实现。具体可以参考源代码 ->
     *        https://github.com/android/platform_frameworks_support/tree/81b3862db1f0e0c24f0991e212ba43a74cf25fc2/v7/appcompat/src/android/support/v7/app
     *
     *   支持版本： AppCompat-v7:19.+
     *
     * @param actionBar
     */
    public static void enableEmbeddedTabs(ActionBar actionBar){
        if (actionBar instanceof ActionBarImplJBMR2) {
            enableEmbeddedTabs(actionBar);
        } else if (actionBar instanceof ActionBarImplJB) {
            try {
                Field actionBarField = actionBar.getClass().getSuperclass().getDeclaredField("mActionBar");
                actionBarField.setAccessible(true);
                enableEmbeddedTabs(actionBarField.get(actionBar));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void enableEmbeddedTabs(Object actionBar) {
        try {
            Method setHasEmbeddedTabsMethod = actionBar.getClass().getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
            setHasEmbeddedTabsMethod.setAccessible(true);
            setHasEmbeddedTabsMethod.invoke(actionBar, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
