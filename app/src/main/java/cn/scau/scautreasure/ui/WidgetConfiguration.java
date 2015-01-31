package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.WidgetHelper;
import cn.scau.scautreasure.widget.ParamWidget;

/**
 * User: special
 * Date: 13-9-15
 * Time: 下午3:16
 * Mail: specialcyci@gmail.com
 */
@EActivity(R.layout.widget_configuration)
public class WidgetConfiguration extends BaseActivity {

    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @Bean
    WidgetHelper widgetHelper;

    @ViewById
    com.larswerkman.holocolorpicker.ColorPicker picker;

    @ViewById
    SVBar svbar;

    @ViewById
    SaturationBar saturationbar;

    @ViewById
    ValueBar valuebar;

    @ViewById
    ParamWidget param_background, param_fontSize;

    @StringArrayRes
    String[] widget_background, widget_fontSize;

    @StringRes
    String listitem_lable_widget_backgroud;

    @StringRes
    String listitem_lable_widget_fontsize;

    private int mAppWidgetId;

    @AfterViews
    void init() {
        getAppWidgetParams();
        setTitleText("课程插件设置");
        setMoreButtonText("保存");

        initColorPicker();
        initParam();
    }

    private void initColorPicker() {
        picker.addSVBar(svbar);
        picker.addSaturationBar(saturationbar);
        picker.addValueBar(valuebar);
        picker.setOldCenterColor(config.widgetFontColor().get());
        picker.setNewCenterColor(config.widgetFontColor().get());
    }

    private void initParam() {
        String fontSize = config.widgetFontSize().get();
        String background = config.widgetBackground().get();
        param_fontSize.initView(listitem_lable_widget_fontsize, widget_fontSize, 0);
        param_background.initView(listitem_lable_widget_backgroud, widget_background, 1);
        param_fontSize.getWheel().setCurrentItem(findMatchIndex(widget_fontSize, fontSize));
        param_background.getWheel().setCurrentItem(findMatchIndex(widget_background, background));
    }

    private int findMatchIndex(String[] stringArray, String want) {
        for (int i = 0; i < stringArray.length; i++)
            if (stringArray[i].equals(want))
                return i;
        return 0;
    }

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
        int color = picker.getColor();
        String fontSize = param_fontSize.getSelectedParam();
        String background = param_background.getSelectedParam();
        config.widgetFontColor().put(color);
        config.widgetFontSize().put(fontSize);
        config.widgetBackground().put(background);
        widgetHelper.setUpViews();
        returnAppWidget();
    }


    private void getAppWidgetParams() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    private void returnAppWidget() {
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}