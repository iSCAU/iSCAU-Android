package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.webkit.WebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.helper.WebWeekClasstableHelper;
import cn.scau.scautreasure.util.DateUtil;

/**
 * Created by macroyep on 14/9/29.
 */
@EActivity(R.layout.weektable)
public class WeekClassTable  extends Activity{
    @ViewById(R.id.weekclasstable)
    WebView contentWebView;
    @Bean
    DateUtil dateUtil;
    @Bean
    ClassHelper classHelper;
    @Pref
    cn.scau.scautreasure.AppConfig_ config;
    @AfterViews
    void init(){
        getActionBar().setTitle("全周课表");
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.loadUrl("file:///android_asset/weekclasstable/weekclasstable.html");

        // 无参数调用
        contentWebView.loadUrl("javascript:javacalljs()");
        contentWebView.addJavascriptInterface(new WebWeekClasstableHelper(contentWebView, config, dateUtil, classHelper), "Android");
        contentWebView.getSettings().setSupportZoom(true);
    }

}
