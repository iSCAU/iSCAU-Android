package cn.scau.scautreasure.helper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.ui.FragmentClassTable;
import cn.scau.scautreasure.util.ClassUtil;
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
    private Context mContext;
    private boolean hasMeasured;

    public WebWeekClasstableHelper(Context context,WebView webView, cn.scau.scautreasure.AppConfig_ config, DateUtil dateUtil, ClassHelper classHelper) {
        this.webView = webView;
        this.vto = webView.getViewTreeObserver();
        this.config = config;
        this.dateUtil = dateUtil;
        this.classHelper = classHelper;
        this.mContext = context;
        initLesson();
    }


    private void initLesson(){
        lessons.clear();
        for (int i=0;i<7;i++) {
            List<ClassModel> dayClassList = null;
            String chineseDay = dateUtil.numDayToChinese(i+1);
            if (config.smart_class_table().get()) {
                dayClassList = classHelper.getDayLessonWithParams(chineseDay);
            } else {
                dayClassList = classHelper.getDayLesson(chineseDay);
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

        DisplayMetrics dm =mContext.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        /*int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        webView.measure(w, h);
        int width =webView.getMeasuredWidth();
        int height =webView.getMeasuredHeight();
        return width;*/
        Log.d("web width:", "" + w_screen);
        return w_screen;


    }
    @JavascriptInterface
    public int getHeight(){
        return webView.getBottom();
    }
    @JavascriptInterface
    public String getStartTime(int node){
        return ClassUtil.genClassBeginTime(node);
    }
    @JavascriptInterface
    public String getEndTime(int node){
        return ClassUtil.genClassOverTime(node);
    }
}
