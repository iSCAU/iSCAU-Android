package cn.scau.scautreasure.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.scau.scautreasure.AppConstant;

/**
 * Created by robust on 14-4-15.
 */
public class LogUtil {

    private static final SimpleDateFormat SDF = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static void writeLog(String log){
        Log.d(AppConstant.LOG_TAG, log);
        PrintWriter writer = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + AppConstant.LOG_FILE_NAME);
            if(!file.exists()){
                File parent = file.getParentFile();
                if(!parent.exists()){
                    parent.mkdirs();
                }
                file.createNewFile();
            }
            writer = new PrintWriter(new FileWriter(file, true));
            writer.append(SDF.format(new Date()));
            writer.append(":\n");
            writer.append(log);
            writer.append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
