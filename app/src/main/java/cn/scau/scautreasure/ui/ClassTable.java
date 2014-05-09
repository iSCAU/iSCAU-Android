package cn.scau.scautreasure.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.ClassAdapter;
import cn.scau.scautreasure.adapter.ClassAdapter_;
import cn.scau.scautreasure.adapter.ClassTableAdapter;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.util.DateUtil;
import cn.scau.scautreasure.widget.ClassTabWidget;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_ALPHA;

/**
 * 课程表
 * User:  Special Leung
 * Date:  13-7-28
 * Time:  下午11:09
 * Mail:  specialcyci@gmail.com
 */
@EFragment( R.layout.classtable )
@OptionsMenu ( R.menu.menu_classtable )
public class ClassTable extends CommonFragment implements ServerOnChangeListener, OnTabSelectListener{

    @Pref        cn.scau.scautreasure.AppConfig_  config;
    @RestService EdusysApi   api;
    @ViewById    ViewPager   pager;
    @ViewById    cn.scau.scautreasure.widget.ClassTabWidget_ titles;
    @ViewById    SwipeRefreshLayout swipe_refresh;
    @ViewById    WebView week_classtable;
    @Bean        DateUtil    dateUtil;
    @Bean        ClassHelper classHelper;
    private ArrayList<View>       listViews;
    private ClassTableAdapter     adapter;
    /** 课程表筛选显示模式  */
    public  static final int      MODE_ALL    = 0;
    public  static final int      MODE_PARAMS = 1;

    private String getTitle(){
        StringBuilder builder = new StringBuilder();
        builder.append(dateUtil.getWeekOfDate());
        return builder.toString();
    }

    private String getSubTitle(){
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.widget_week_start));
        builder.append(classHelper.getSchoolWeek());
        builder.append(getString(R.string.widget_week_end));
        return builder.toString();
    }

    @AfterViews
    void initView(){

//        week_classtable.getSettings().setJavaScriptEnabled(true);
//        week_classtable.loadUrl("file:///android_asset/weekclasstable/weekclasstable.html");
//        week_classtable.addJavascriptInterface(new WebWeekClasstableHelper(week_classtable), "Android");

        listViews = new ArrayList<View>();
        adapter   = new ClassTableAdapter();

        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(onPageChangeListener);
        titles.setListener(onTabChangeListener);

        showClassTable();
        showTab();
        setSwipeRefresh();
    }

    private void setSwipeRefresh() {
        swipe_refresh.setEnabled(false);
        // 顶部刷新的样式
        swipe_refresh.setColorScheme(R.color.swipe_refresh_1,
                R.color.swipe_refresh_2,
                R.color.swipe_refresh_3,
                R.color.swipe_refresh_4);
    }

    /**
     * 添加课程;
     */
    @OptionsItem
    void menu_add_class(){
        startActivityForResult(ClassEditor_.intent(getSherlockActivity())
                .isNewClass(true)
                .model(new ClassModel()).get(), UIHelper.QUERY_FOR_EDIT_CLASS);
    }

    /**
     * 刷新课程菜单点击
     */
    @OptionsItem
    void menu_refresh_classtable() {
        if (app.eduSysPassword == null || app.eduSysPassword.equals("")){
            Login_.intent(this).startTips(getString(R.string.start_tips_edusys)).start();
        } else {
            swipe_refresh.setRefreshing(true);
            loadData();
        }
    }

    /**
     * 切换到加载所有课程模式;
     */
    @OptionsItem
    void menu_load_classtable_all(){
        config.classTableShowMode().put(MODE_ALL);
        showClassTable();
    }

    /**
     * 切换到智能加载课程模式;
     */
    @OptionsItem
    void menu_load_classtable_with_params(){
        config.classTableShowMode().put(MODE_PARAMS);
        showClassTable();
    }

    /**
     * 星期标签的点击,同时viewPager设置到相应位置；
     */
    private ClassTabWidget.onTabChangeListener onTabChangeListener = new ClassTabWidget.onTabChangeListener() {
        @Override
        public void change(int posistion) {
            pager.setCurrentItem(posistion);
        }
    };

    /**
     * viewPager滑动监听,同时同步课程表上方的tab位置;
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            titles.changeWeekDay(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    /**
     * 修改课程表后，接收来自修改课程表activiy回传的model,写入数据库，并且更新到界面；
     * @param resultCode
     * @param data
     */
    @OnActivityResult(UIHelper.QUERY_FOR_EDIT_CLASS)
    void modifyClassOnResult(int resultCode,Intent data){
        if(resultCode == getSherlockActivity().RESULT_OK){
            ClassModel model = (ClassModel) data.getSerializableExtra("class");
            createOrUpdateClassInformation(model);
        }

    }

    /**
     * 线程添加或修改课程表信息
     * @param model
     */
    @Background
    void createOrUpdateClassInformation(ClassModel model){
        classHelper.createOrUpdateLesson(model);
        showClassTable();
    }

    /**
     * 线程删除课程;
     * @param model
     */
    @Background
    public void deleteClass(ClassModel model){
        classHelper.deleteLesson(model);
        showClassTable();
    }

    /**
     * 使课程表上方的TAB移动到今天的位置，由于太早执行可能控件尚未绘制完成，导致getWidth() = 0,
     * 引发一系列BUG，这里延迟500ms执行；
     */
    @UiThread(delay = 500)
    void showTab(){
        int currentDay = dateUtil.getDayOfWeek() - 1;
        // 这句不能去掉，当日期为星期一时，就不重绘了，只有移动下它才重绘
        if(currentDay == 0)
            titles.changeWeekDay(1);
        pager.setCurrentItem(currentDay);
        titles.changeWeekDay(currentDay);
    }

    /**
     * 展示网络加载异常结果
     * @param ctx
     * @param requestCode
     */
    @Override
    @UiThread
    void showErrorResult(ActionBarActivity ctx, int requestCode){
        swipe_refresh.setRefreshing(false);
        if(requestCode == 404){
            AppMsg.makeText(ctx, R.string.tips_classtable_null, AppMsg.STYLE_ALERT).show();
        }else{
            app.showError(requestCode,ctx);
        }
    }

    @UiThread
    void showSuccess(){
        AppMsg.makeText(getSherlockActivity(), "课表更新完成" , AppMsg.STYLE_INFO).show();
    }

    @UiThread
    void closeSwipeRefresh(){
        swipe_refresh.setRefreshing(false);
    }

    /**
     * 展示课程表,同时将课程表切换到今天.
     */
    @UiThread()
    void showClassTable(){

        int prevPosition = pager.getCurrentItem();

        UIHelper.getDialog().dismiss();
        listViews.clear();

        for (int i = 1; i <= 7; i++) {
            List<ClassModel> dayClassList = null;
            String chineseDay = dateUtil.numDayToChinese(i);

            if(config.classTableShowMode().get() == MODE_ALL){
                dayClassList = classHelper.getDayLesson(chineseDay);
            }else{
                dayClassList = classHelper.getDayLessonWithParams(chineseDay);
            }

            buildDayClassTableAdapter(dayClassList);
        }

        adapter.setViewList(listViews);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        pager.setCurrentItem(prevPosition);
        onTabSelect();
    }

    private void buildDayClassTableAdapter(List<ClassModel> dayClassList){
        ListView classListView = UIHelper.buildClassListView(getSherlockActivity());
        ClassAdapter  cAdapter = ClassAdapter_.getInstance_(getSherlockActivity());
        BaseAdapter   _adapter = UIHelper.buildEffectAdapter(cAdapter, (AbsListView) classListView,EXPANDABLE_ALPHA);

        cAdapter.addAll(dayClassList);
        cAdapter.setFragment(this);
        classListView.setAdapter(_adapter);
        listViews.add(classListView);

    }
    /**
     * 线程从服务器加载课程表，同时保存在本地数据库，再行操作;
     */
    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {

        try{
            ClassModel.ClassList l = api.getClassTable(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server);
            config.termStartDate().put(l.getTermStartDate());
            // save data in sqlite;
            classHelper.replaceAllLesson(l.getClasses());
            showSuccess();
            showClassTable();
            showTab();
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(), this);
        }catch (Exception e){
            handleNoNetWorkError(getSherlockActivity());
        }
        closeSwipeRefresh();
    }

    @Override
    public void onChangeServer() {
        menu_refresh_classtable();
    }

    // 横竖屏切换到额时候，重新绘制课程表日期下划线.
    @UiThread(delay = 300)
    void updateTabOnOrientationChange(){
        titles.changeWeekDay(pager.getCurrentItem());
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateTabOnOrientationChange();
    }

    /**
     * Tab 切换到当前页。
     */
    @Override
    public void onTabSelect() {
        setTitle(getTitle());
        setSubTitle(getSubTitle());
    }
}