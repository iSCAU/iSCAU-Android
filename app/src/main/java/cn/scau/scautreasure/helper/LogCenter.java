package cn.scau.scautreasure.helper;

import android.util.Log;

/**
 * Created by macroyep on 15/1/20.
 * Time:14:16
 */
public class LogCenter {
    public static String getMethodNameByThread(Thread thread) {
        StackTraceElement[] stacktrace = thread.getStackTrace();
        StackTraceElement e = stacktrace[2];
        String methodName = e.getMethodName();
        return methodName;
    }

    public static void i(Class theClass, Thread thread, String msg) {
        Log.i(theClass.getName() + "->" + getMethodNameByThread(thread), msg);
    }

    public static void e(Class theClass, Thread thread, String msg) {
        Log.e(theClass.getName() + "->" + getMethodNameByThread(thread), msg);
    }

    public static void w(Class theClass, Thread thread, String msg) {
        Log.w(theClass.getName() + "->" + getMethodNameByThread(thread), msg);
    }
}
