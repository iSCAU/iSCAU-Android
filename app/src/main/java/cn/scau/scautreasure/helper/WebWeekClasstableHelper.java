package cn.scau.scautreasure.helper;

import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.ui.ClassTable;
import cn.scau.scautreasure.util.DateUtil;

/**
 * Created by blankyum on 2014/5/6.
 */
public class WebWeekClasstableHelper {


    private WebView webView;
    private ViewTreeObserver vto;
    private cn.scau.scautreasure.AppConfig_ config;
    private DateUtil dateUtil;
    private ClassHelper classHelper;
    private ArrayList<String> lessons = new ArrayList<String>(7);

    public WebWeekClasstableHelper(WebView webView, cn.scau.scautreasure.AppConfig_ config, DateUtil dateUtil, ClassHelper classHelper) {
        this.webView = webView;
        this.vto = webView.getViewTreeObserver();
        this.config = config;
        this.dateUtil = dateUtil;
        this.classHelper = classHelper;
        initLesson();
    }


    private void initLesson(){
        lessons.clear();
        for (int i=0;i<7;i++) {
            List<ClassModel> dayClassList = null;
            String chineseDay = dateUtil.numDayToChinese(i+1);
            if (config.classTableShowMode().get() == ClassTable.MODE_ALL) {
                dayClassList = classHelper.getDayLesson(chineseDay);
            } else {
                dayClassList = classHelper.getDayLessonWithParams(chineseDay);
            }

            JSONArray ja = new JSONArray();
            for (ClassModel classModel : dayClassList) {
                ja.put(JSONHelper.toJSON(classModel));
            }
            JSONObject jo = new JSONObject();
            try {
                jo.put("count", dayClassList.size());
                jo.put("day_class", ja);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(jo.toString());
            lessons.add(jo.toString());
            //allLession[i]=jo.toString();
//            System.out.println(allLession[i]);
            // return jo.toString();
        }
    }

    @JavascriptInterface
    public String getDayLesson(int day) {
       /* List<ClassModel> dayClassList = null;
        String chineseDay = dateUtil.numDayToChinese(day);
        if (config.classTableShowMode().get() == ClassTable.MODE_ALL) {
            dayClassList = classHelper.getDayLesson(chineseDay);
        } else {
            dayClassList = classHelper.getDayLessonWithParams(chineseDay);
        }

        JSONArray ja = new JSONArray();
        for (ClassModel classModel : dayClassList) {
            ja.put(JSONHelper.toJSON(classModel));
        }
        JSONObject jo = new JSONObject();
        try {
            jo.put("count", dayClassList.size());
            jo.put("day_class", ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

       // System.out.println("js:"+appContext.getAllLession()[day-1]);
        return lessons.get(day-1);
    }

    @JavascriptInterface
    public void debug(String message) {
        System.out.println(message);
    }

    public void refreshClassTable(){
        initLesson();
    }
    @JavascriptInterface
    public int getWidth(){
        return webView.getWidth();
    }
    @JavascriptInterface
    public int getHeight(){
        return webView.getBottom();
    }
}
