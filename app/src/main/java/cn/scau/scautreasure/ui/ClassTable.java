package cn.scau.scautreasure.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.avos.avoscloud.LogUtil;
import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.AppContext_;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.ClassAdapter;
import cn.scau.scautreasure.adapter.ClassAdapter_;
import cn.scau.scautreasure.adapter.ClassTableAdapter;

import cn.scau.scautreasure.api.CookieApi;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.ActionBarHelper;
import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.helper.WebWeekClasstableHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.impl.ServerOnChangeListener;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.model.CookieModel;
import cn.scau.scautreasure.model.LoginModel;
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
@EFragment(R.layout.classtable)
@OptionsMenu(R.menu.menu_classtable)
public class ClassTable extends CommonFragment implements ServerOnChangeListener, OnTabSelectListener {
    @App
    protected AppContext app;

    public static int school_week = 0;

    //加载周课表完成是否
    private boolean LOAD_WEEK_DONE = false;

    /**
     * 课程表筛选显示模式
     */
    public static final int MODE_ALL = 0;
    public static final int MODE_PARAMS = 1;
    @Pref
    cn.scau.scautreasure.AppConfig_ config;
    @RestService
    EdusysApi api;
    @ViewById
    ViewPager pager;
    @ViewById
    cn.scau.scautreasure.widget.ClassTabWidget_ titles;
    @ViewById
    SwipeRefreshLayout swipe_refresh;
    @ViewById
    WebView week_classtable;
    @ViewById
    LinearLayout day_classtable_container;
    @Bean
    DateUtil dateUtil;
    @Bean
    ClassHelper classHelper;
    private ArrayList<View> listViews;
    private ClassTableAdapter adapter;
    private WebWeekClasstableHelper webWeekClasstableHelper;
    private boolean first = true;
    ActionBar actionBar;
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


    private String getTitle() {
        StringBuilder builder = new StringBuilder();
        builder.append(dateUtil.getWeekOfDate());
        return builder.toString();
    }

    private String getSubTitle() {
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.widget_week_start));
        builder.append(classHelper.getSchoolWeek());
        builder.append(getString(R.string.widget_week_end));
        return builder.toString();
    }

    @AfterViews
    void initView() {
        school_week = classHelper.getSchoolWeek();
        //school_week = (school_week == 0 ? 1 : school_week);
        System.out.println("第"+school_week);
        listViews = new ArrayList<View>();
        adapter = new ClassTableAdapter();

        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(onPageChangeListener);
        titles.setListener(onTabChangeListener);

        showClassTable();
        showTab();
        setSwipeRefresh();


        // 给 Action Bar 增加 "单日", "全周" 的切换 Tab。

        webWeekClasstableHelper = new WebWeekClasstableHelper(week_classtable, config, dateUtil, classHelper);
        week_classtable.getSettings().setJavaScriptEnabled(true);
        week_classtable.addJavascriptInterface(webWeekClasstableHelper, "Android");
        week_classtable.getSettings().setSupportZoom(true);
        week_classtable.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        isSelectedDay = config.classTableSelectedTab().get() == 0;
        if (isSelectedDay) {
            onSelectDayMode();
        } else {
            onSelectWeekMode();
        }
    }

    boolean isSelectedDay;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar = getSherlockActivity().getSupportActionBar();
        ActionBarHelper.enableEmbeddedTabs(actionBar);
        // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
/*        if(isSelectedDay){
            change_mode.setIcon(R.drawable.action_week_mod);
            onSelectDayMode();
        }else{
            change_mode.setIcon(R.drawable.action_day_mod);
            onSelectWeekMode();
        }*/
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem change_mode = menu.findItem(R.id.table_change_mode);
        isSelectedDay = config.classTableSelectedTab().get() == 0;
        if (isSelectedDay) {
            change_mode.setIcon(R.drawable.action_week_mod);
            change_mode.setTitle(R.string.actionbar_week);
            // onSelectDayMode();
        } else {
            change_mode.setIcon(R.drawable.action_day_mod);
            change_mode.setTitle(R.string.actionbar_day);
            // onSelectWeekMode();
        }
    }

    @UiThread
    void showWeekClass() {
        week_classtable.reload();
    }

    /**
     * 按周查看
     */
    /*void selectWeek(){

        NumberPicker mPicker = new NumberPicker(getSherlockActivity());
        mPicker.setMinValue(1);
        mPicker.setMaxValue(23);
        mPicker.setOnValueChangedListener(new android.widget.NumberPicker.OnValueChangeListener() {


            @Override
            public void onValueChange(android.widget.NumberPicker numberPicker, int i, int i2) {

            }
        });

        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        builder.setTitle("当前第"+classHelper.getSchoolWeek()+"周");
        builder.setView(mPicker);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("修改当前周",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UpdateCurrentWeek_.intent(getSherlockActivity()).current(String.valueOf(classHelper.getSchoolWeek())).startForResult(12345);
            }
        });
        builder.create();
        builder.show();

    }*/
    @OptionsItem
    void table_change_mode(MenuItem menuItem) {
        if (isSelectedDay) { //原来是日模式
            menuItem.setIcon(R.drawable.action_day_mod);
            menuItem.setTitle(R.string.actionbar_day);
            onSelectWeekMode();//转换成周
            config.classTableSelectedTab().put(1);
        } else {
            menuItem.setIcon(R.drawable.action_week_mod);
            menuItem.setTitle(R.string.actionbar_week);
            onSelectDayMode();//转换成日
            config.classTableSelectedTab().put(0);
        }
        isSelectedDay = !isSelectedDay;

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
    void menu_add_class() {
        startActivityForResult(ClassEditor_.intent(getSherlockActivity())
                .isNewClass(true)
                .model(new ClassModel()).get(), UIHelper.QUERY_FOR_EDIT_CLASS);
    }

    /**
     * 刷新课程菜单点击
     */
    @OptionsItem
    void menu_refresh_classtable() {
        if (app.eduSysPassword == null || app.eduSysPassword.equals("")) {
            Login_.intent(this).startTips(getString(R.string.start_tips_edusys)).start();
        } else {
            //swipe_refresh.setRefreshing(true);
            //loadData();
            beforeLoading();
        }
    }

    /**
     * 切换到加载所有课程模式;
     */
    @OptionsItem
    void menu_load_classtable_all() {
        config.classTableShowMode().put(MODE_ALL);
        showClassTable();
    }

    /**
     * 切换到智能加载课程模式;
     */
    @OptionsItem
    void menu_load_classtable_with_params(MenuItem item) {
        config.classTableShowMode().put(MODE_PARAMS);
        showClassTable();
    }

    @OptionsItem(R.id.menu_class_table_select_week)
    void menu_class_table_select_week() {
        startActivityForResult(UpdateCurrentWeek_.intent(getActivity()).current(String.valueOf(classHelper.getSchoolWeek())).get(), 10);
       // LogUtil.log.i("选择了按周查看");

    }

    /**
     * 修改课程表后，接收来自修改课程表activiy回传的model,写入数据库，并且更新到界面；
     *
     * @param resultCode
     * @param data
     */
    @OnActivityResult(UIHelper.QUERY_FOR_EDIT_CLASS)
    void modifyClassOnResult(int resultCode, Intent data) {
        if (resultCode == getSherlockActivity().RESULT_OK) {
            ClassModel model = (ClassModel) data.getSerializableExtra("class");
            createOrUpdateClassInformation(model);
        }

    }

    /**
     * 线程添加或修改课程表信息
     *
     * @param model
     */
    @Background
    void createOrUpdateClassInformation(ClassModel model) {
        classHelper.createOrUpdateLesson(model);
        showClassTable();
    }

    /**
     * 线程删除课程;
     *
     * @param model
     */
    @Background
    public void deleteClass(ClassModel model) {
        classHelper.deleteLesson(model);
        showClassTable();
    }

    /**
     * 使课程表上方的TAB移动到今天的位置，由于太早执行可能控件尚未绘制完成，导致getWidth() = 0,
     * 引发一系列BUG，这里延迟500ms执行；
     */
    @UiThread(delay = 500)
    void showTab() {
        int currentDay = dateUtil.getDayOfWeek() - 1;
        // 这句不能去掉，当日期为星期一时，就不重绘了，只有移动下它才重绘
        if (currentDay == 0)
            titles.changeWeekDay(1);
        pager.setCurrentItem(currentDay);
        titles.changeWeekDay(currentDay);
    }

    /**
     * 展示网络加载异常结果
     *
     * @param ctx
     * @param requestCode
     */
    @UiThread
    void showErrorResult(ActionBarActivity ctx, int requestCode) {
        swipe_refresh.setRefreshing(false);
        if (requestCode == 404) {
            AppMsg.makeText(ctx, R.string.tips_classtable_null, AppMsg.STYLE_ALERT).show();
        } else {
            app.showError(requestCode, ctx);
        }
    }

    @UiThread
    void showSuccess() {
        AppMsg.makeText(getSherlockActivity(), "课表更新完成", AppMsg.STYLE_INFO).show();
    }

    @UiThread
    void closeSwipeRefresh() {
        swipe_refresh.setRefreshing(false);
    }

    @UiThread
    void showMsg(String msg) {
        AppMsg.makeText(getSherlockActivity(), msg, AppMsg.STYLE_INFO).show();
    }

    @UiThread
    void showErrorMsg(String msg) {
        AppMsg.makeText(getSherlockActivity(), msg, AppMsg.STYLE_ALERT).show();
    }

    /**
     * 展示课程表,同时将课程表切换到今天.
     * 已经包含刷新全周课表的逻辑
     */
    @UiThread()
    void showClassTable() {

        // 以下是刷新日课表;
        int prevPosition = pager.getCurrentItem();

        UIHelper.getDialog().dismiss();
        listViews.clear();

        for (int i = 1; i <= 7; i++) {
            List<ClassModel> dayClassList = null;
            String chineseDay = dateUtil.numDayToChinese(i);

            if (config.classTableShowMode().get() == MODE_ALL) {
                dayClassList = classHelper.getDayLesson(chineseDay);
            } else {
                //原来的智能显示
                //dayClassList = classHelper.getDayLessonWithParams(chineseDay);
                //新的智能显示
                dayClassList = classHelper.getDayLessonByWeek(chineseDay, school_week);
            }

            buildDayClassTableAdapter(dayClassList);
        }

        adapter.setViewList(listViews);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        pager.setCurrentItem(prevPosition);
        onTabSelect();
        //刷新全周课表
        webWeekClasstableHelper.refreshClassTable();
        showWeekClass();

    }

    private void buildDayClassTableAdapter(List<ClassModel> dayClassList) {
        ListView classListView = UIHelper.buildClassListView(getSherlockActivity());
        ClassAdapter cAdapter = ClassAdapter_.getInstance_(getSherlockActivity());
        BaseAdapter _adapter = UIHelper.buildEffectAdapter(cAdapter, (AbsListView) classListView, EXPANDABLE_ALPHA);

        cAdapter.addAll(dayClassList);
        cAdapter.setFragment(this);
        classListView.setAdapter(_adapter);
        listViews.add(classListView);

    }

    /**
     * 线程从服务器加载课程表，同时保存在本地数据库，再行操作;
     */
    //@Background(id = UIHelper.CANCEL_FLAG)
    @Background
    void loadData(Object... params) {

        try {
            ClassModel.ClassList l = api.getClassTable(AppContext.userName, app.getEncodeEduSysPassword(), AppContext.server);
            config.termStartDate().put(l.getTermStartDate());
            // save data in sqlite;
            classHelper.replaceAllLesson(l.getClasses());
            showSuccess();
            showClassTable();
            showTab();
        } catch (HttpStatusCodeException e) {
            showErrorResult(getSherlockActivity(), e.getStatusCode().value(), this);
        } catch (Exception e) {
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
    void updateTabOnOrientationChange() {
        titles.changeWeekDay(pager.getCurrentItem());
        ActionBarHelper.enableEmbeddedTabs(getSherlockActivity().getSupportActionBar());
        if (config.classTableSelectedTab().get() == 1) {
            week_classtable.reload();
        }
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateTabOnOrientationChange();
    }

    /*
    * ---------------------------------------------------------
    * 底部 Tab 选择切换区域;
    * ---------------------------------------------------------
    */
    @Override
    public void onTabSelect() {
        setTitle(getTitle());
        setSubTitle(getSubTitle());
        /*getSherlockActivity().getSupportActionBar()
                .setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);*/
    }

    /*
     * ---------------------------------------------------------
     * Action Bar Tab 选择切换区域;
     * ---------------------------------------------------------
     */
  /*  @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (tab.getPosition() == 0) {
            week_classtable.setVisibility(View.GONE);
            day_classtable_container.setVisibility(View.VISIBLE);
            showTab();
        } else if (tab.getPosition() == 1) {
            day_classtable_container.setVisibility(View.GONE);
            if(first) {
                week_classtable.loadUrl("file:///android_asset/weekclasstable/weekclasstable.html");
                week_classtable.setVisibility(View.VISIBLE);
                first=false;
            }else{
                week_classtable.setVisibility(View.VISIBLE);
            }
        }
        // 储存用户当前选择的 Tab ；
        config.classTableSelectedTab().put(tab.getPosition());
    }
*/

    /**
     * 当选择周模式
     */
    void onSelectWeekMode() {
        day_classtable_container.setVisibility(View.GONE);
        if (first) {
            week_classtable.loadUrl("file:///android_asset/weekclasstable/weekclasstable.html");
            week_classtable.setVisibility(View.VISIBLE);
            first = false;
        } else {
            week_classtable.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 当选择日模式
     */
    void onSelectDayMode() {
        week_classtable.setVisibility(View.GONE);
        day_classtable_container.setVisibility(View.VISIBLE);
        showTab();
    }

/*    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12345) {
            //接受设置第几周
            showClassTable();
        }
    }

    @OnActivityResult(10)
    void show_custom_week(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            System.out.println("用户选择了第:" + data.getExtras().getInt("week"));
            school_week = data.getExtras().getInt("week");
            config.classTableShowMode().put(MODE_PARAMS);
            showClassTable();
            setCustomTitle("正在显示第" + school_week + "周课程");
        }
    }

    @UiThread
    void setCustomTitle(String title){
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    /*2015-11-12 zzb添加，暂时未优化*/
    @UiThread
    void showCheckcode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入验证码");
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.checkcode_dialog, null);
        final EditText editText = (EditText) view.findViewById(R.id.input_checkcode);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                code = editText.getText().toString();
                showProgressDialog(getActivity());
                loginServer();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        Dialog dialog = builder.create();
        dialog.show();
        ImageView imageView = (ImageView) view.findViewById(R.id.checkcode_img);
        AppContext.loadImage(cookieModel.getImg(), imageView, null);
    }

    @Click(R.id.checkcode_img)
    void changeCheckCode() {
        showProgressDialog(getActivity());
        getCheckCode();
    }

    @UiThread
    void beforeLoading() {
        getCheckCode();
    }

    private ProgressDialog progressDialog;
    @RestService
    CookieApi loginApi;

    CookieModel cookieModel;
    LoginModel loginModel;
    String code;

    @UiThread
    void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("正在加载中...");
        progressDialog.show();
    }

    void colseProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Background
    void getCheckCode() {
        try {
            cookieModel = loginApi.getCookie();
            showCheckcode();
        } catch (Exception e) {
            LogUtil.log.i("classtable：" + e.toString());
        } finally {
            colseProgressDialog();
        }

    }


    @Background
    void loginServer() {
        try {
            loginModel = loginApi.loginCookie(AppContext.userName, app.getEncodeEduSysPassword(), cookieModel.getCookie(), code);
            if (loginModel.getStatus() == 1) {
                //swipe_refresh.setRefreshing(true);
                loadData();
                LogUtil.log.i("classtable：" + loginModel.getMsg());
            } else {
                showErrorMsg(loginModel.getMsg());
                LogUtil.log.i("classtable：" + loginModel.getMsg());

            }

        } catch (Exception e) {
            LogUtil.log.i("获取课表时出错：" + e.toString());
        }
        colseProgressDialog();
    }


}