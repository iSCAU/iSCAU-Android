package cn.scau.scautreasure.util;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * User: special
 * Date: 13-8-27
 * Time: 上午11:30
 * Mail: specialcyci@gmail.com
 */
@EBean
public class TextUtil {

    @RootContext
    Context ctx;

    public String getFromAssets(String filename) {

        try {

            String line = "";
            String Result = "";

            InputStream file = ctx.getResources().getAssets().open(filename);
            InputStreamReader inputReader = new InputStreamReader(file);
            BufferedReader bufReader = new BufferedReader(inputReader);

            while ((line = bufReader.readLine()) != null) Result += line;

            return Result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public String toDBC(String input) {

        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);

    }
}
