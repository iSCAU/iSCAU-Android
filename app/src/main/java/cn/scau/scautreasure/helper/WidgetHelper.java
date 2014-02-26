package cn.scau.scautreasure.helper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.RemoteViews;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.WidgetProvider;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.util.DateUtil;

/**
 * 桌面小插件 辅助类;
 *
 * User: special
 * Date: 13-9-11
 * Time: 下午10:46
 * Mail: specialcyci@gmail.com
 */

@EBean
public class WidgetHelper {

    @App
    AppContext app;

    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    @Bean
    ClassHelper classHelper;

    @Bean
    DateUtil    dateUtil;

    @Bean
    CalendarHelper calendarHelper;

    @StringArrayRes
    String[] widget_background;

    private RemoteViews remoteViews;
    private ArrayList<RemoteViews> clsRemoteViews;
    private int[] tabViews = new int[]{
            R.id.tv_mon,R.id.tv_tue,R.id.tv_wed,R.id.tv_thu,
            R.id.tv_fri,R.id.tv_sat,R.id.tv_sun
    };
    private static int TYPE_TITLE = 1;
    private static int TYPE_ITEM  = 2;
    private static int TYPE_TAB   = 3;

    public void setUpViews(){
        int currentDay = dateUtil.getDayOfWeek();
        showDayClassTableView(currentDay);
    }

    public void showDayClassTableView(int day){
        buildRemoteViews();
        setBackground();
        setWeekTextAndTipsText();
        setUpRemoteViewsClickIntent();
        buildClassTableViews(day);
        updateClassRemoteViews(0,clsRemoteViews.size());
        setSelectedTabView(day);
        updateRemoteViews();
    }

    private void buildRemoteViews(){
        remoteViews = new RemoteViews(app.getPackageName(),R.layout.widget);
    }

    private void setWeekTextAndTipsText(){
        int week = classHelper.getSchoolWeek();
        String eventTips = calendarHelper.getTodayEventTitle();
        String weekTips  = app.getString(R.string.widget_week_start) + week
                + app.getString(R.string.widget_week_end);
        remoteViews.setTextViewText(R.id.tv_week,weekTips);
        remoteViews.setTextViewText(R.id.tv_calendar,eventTips);
        setTextViewFontColorWithConfig(R.id.tv_week, TYPE_TITLE);
        setTextViewFontColorWithConfig(R.id.tv_calendar, TYPE_TITLE);
    }

    /**
     * set background by user settings;
     */
    private void setBackground() {
        int index = getStringArrayMatchIndex(widget_background,config.widgetBackground().get());
        switch (index){

            case 0:
                setBackgroundResource(R.id.layout_widget, 0);
                remoteViews.setInt(R.id.layout_panel, "setBackgroundResource", 0);
                break;

            case 1:
                setBackgroundResource(R.id.layout_widget, R.drawable.widget_background);
                remoteViews.setInt(R.id.layout_panel, "setBackgroundColor", 0x4e000000);
                break;

        }
    }

    private int getStringArrayMatchIndex(String[] stringArray, String want){
        for (int i = 0; i < stringArray.length; i++){
            if (want.equals(stringArray[i]))
                return i;
        }
        return 0;
    }

    /**
     * set up the click event intent pending for button;
     */
    private void setUpRemoteViewsClickIntent(){
        buildAndSetPendingIntent(R.id.layout_classtable, AppConstant.INTENT_CONFIGURE);
        buildAndSetPendingIntent(R.id.iv_settings, AppConstant.INTENT_SETTINGS);
        buildAndSetPendingIntent(tabViews[0], AppConstant.INTENT_MONDAY);
        buildAndSetPendingIntent(tabViews[1], AppConstant.INTENT_TUESDAY);
        buildAndSetPendingIntent(tabViews[2], AppConstant.INTENT_WEDNESDAY);
        buildAndSetPendingIntent(tabViews[3], AppConstant.INTENT_THURDAY);
        buildAndSetPendingIntent(tabViews[4], AppConstant.INTENT_FRIDAY);
        buildAndSetPendingIntent(tabViews[5], AppConstant.INTENT_SATURDAY);
        buildAndSetPendingIntent(tabViews[6], AppConstant.INTENT_SUNDAY);
    }

    /**
     * help to build the peding intent;
     * @param viewId
     * @param actionName
     */
    private void buildAndSetPendingIntent(int viewId,String actionName){
        Intent intent = new Intent(actionName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(app, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(viewId, pendingIntent);
    }

    /**
     * build up a class table views accroding the classlist;
     * @param day
     */
    private void buildClassTableViews(int day){
        clsRemoteViews = new ArrayList<RemoteViews>();
        List<ClassModel> clsList = classHelper.getDayLessonWithParams(day);
        for (ClassModel cls : clsList){
            RemoteViews clsRemoteViews = buildClassRemoteViews(cls);
            this.clsRemoteViews.add(clsRemoteViews);
        }
    }

    private RemoteViews buildClassRemoteViews(ClassModel cls){
        RemoteViews clsRemoteViews = new RemoteViews(app.getPackageName(),R.layout.widget_item);
        clsRemoteViews.setTextViewText(R.id.tv_classname,cls.getClassname());
        clsRemoteViews.setTextViewText(R.id.tv_nodes,cls.getNode());
        clsRemoteViews.setTextViewText(R.id.tv_place,cls.getLocation());
        setTextViewFontColorWithConfig(clsRemoteViews,R.id.tv_classname, TYPE_ITEM);
        setTextViewFontColorWithConfig(clsRemoteViews,R.id.tv_nodes, TYPE_ITEM);
        setTextViewFontColorWithConfig(clsRemoteViews,R.id.tv_place, TYPE_ITEM);
        return clsRemoteViews;
    }

    /**
     * add the class remoteviews to appwidget;
     *
     * @param startClassIndex   start index in the clsRemoteViews;
     * @param endClassIndex     end   index in the clsRemoteViews;
     */
    private void updateClassRemoteViews(int startClassIndex,int endClassIndex){
        remoteViews.removeAllViews(R.id.layout_classtable);
        List<RemoteViews> wantRemoteViews = clsRemoteViews.subList(startClassIndex,endClassIndex);
        for (RemoteViews rv : wantRemoteViews)
            remoteViews.addView(R.id.layout_classtable,rv);
    }

    private void setSelectedTabView(int selectedIndex){
        selectedIndex-- ;
        for (int currentIndex = 0 ; currentIndex < tabViews.length; currentIndex++){
            setTextViewBottomDrawableBy(currentIndex,selectedIndex);
            setTextViewFontColorWithConfig(tabViews[currentIndex], TYPE_TAB);
        }
    }

    private void setTextViewBottomDrawableBy(int currentIndex,int selectedIndex){
        int currentTabView = tabViews[currentIndex];
        if (isCurrentSelectTabView(currentIndex,selectedIndex)){
            setBackgroundResource(currentTabView, R.drawable.widget_tab_selector);
        }else{
            setBackgroundResource(currentTabView, 0);
        }
    }

    private boolean isCurrentSelectTabView(int currentIndex, int selectedIndex){
        return currentIndex ==  selectedIndex;
    }

    /**
     * set the textView color by user settings;
     * @param viewId
     */
    private void setTextViewFontColorWithConfig(int viewId, int type){
        setTextViewFontColorWithConfig(remoteViews, viewId, type);
    }

    private void setTextViewFontColorWithConfig(RemoteViews parentView,int viewId,int type){
        parentView.setFloat(viewId,"setTextSize",getTextFontSizeBy(type));
        parentView.setTextColor(viewId,config.widgetFontColor().get());
    }

    private float getTextFontSizeBy(int type){
        float multiple = Float.valueOf(config.widgetFontSize().get());
        int   resource = R.dimen.widget_item_textSize;
        if(type == TYPE_TITLE){
            resource = R.dimen.widget_title_textSize;
        }else if (type == TYPE_ITEM){
            resource = R.dimen.widget_item_textSize;
        }else if (type == TYPE_TAB){
            resource = R.dimen.widget_tab_textSize;
        }
        float textSize = app.getResources().getDimension(resource);
        return getDimenSpVaule(textSize) * multiple;
    }

    private float getDimenSpVaule(float value){
        float scaleRatio = app.getResources().getDisplayMetrics().density;
        return value/scaleRatio;
    }

    private void setBackgroundResource(int viewId, int drawableResourceId){
        remoteViews.setInt(viewId,"setBackgroundResource",drawableResourceId);
    }

    private void updateRemoteViews() {
        ComponentName componentName = new ComponentName(app, WidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(app);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
    }

}

