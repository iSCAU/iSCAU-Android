package cn.scau.scautreasure.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.*;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.PopupWindow;

import android.widget.TextView;


import org.androidannotations.annotations.AfterViews;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;

import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;


import java.util.ArrayList;
import java.util.List;


import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.ClassAdapter;
import cn.scau.scautreasure.adapter.ClassAdapter_;
import cn.scau.scautreasure.adapter.ClassTableAdapter;


import cn.scau.scautreasure.helper.AppUIMeasure;
import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.helper.HttpLoader;

import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.helper.WebWeekClasstableHelper;

import cn.scau.scautreasure.model.ClassModel;

import cn.scau.scautreasure.widget.AppProgress;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.ClassTabWidget;


import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_ALPHA;

/**
 * 课程表
 * Date:  15-1-17
 * Time:  23:11:09
 */
@EFragment(R.layout.classtable)
public class FragmentClassTable extends BaseFragment {

    @ViewById(R.id.tab_bar_layout)
    View tab_bar_layout;
    //日/周切换
    @ViewById(R.id.class_table_day_week)
    Button class_table_day_week;

    //更多
    @ViewById(R.id.class_table_more)
    ImageButton class_table_more;

    //标题
    @ViewById(R.id.class_table_title)
    TextView class_table_title;

    //周课表
    @ViewById(R.id.week_classtable)
    WebView week_classtable;

    //日课表
    @ViewById(R.id.day_classtable_container)
    View day_classtable_container;

    //每一页课程
    @ViewById(R.id.pager)
    ViewPager pager;

    //日课程一节课的标题
    @ViewById
    cn.scau.scautreasure.widget.ClassTabWidget_ titles;

    private int school_week = 0;


    //加载周课表完成是否
    private boolean LOAD_WEEK_DONE = false;

    @Bean
    ClassHelper classHelper;

    private ArrayList<View> listViews;

    private ClassTableAdapter adapter;

    private WebWeekClasstableHelper webWeekClasstableHelper;

    //pop more menu
    private PopupWindow popupWindow;

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
     * 日/周视图点击事件
     */
    @Click(R.id.class_table_day_week)
    void day_week_onclick() {
        if (app.config.day_week().get() == 0) {
            app.config.day_week().put(1);
        } else if (app.config.day_week().get() == 1) {
            app.config.day_week().put(0);
        }
        updateClassView(app.config.day_week().get());
    }

    /**
     * 视图改变
     */
    @UiThread
    void updateClassView(int mode) {
        if (mode == 0) {//日视图
            week_classtable.setVisibility(View.GONE);
            day_classtable_container.setVisibility(View.VISIBLE);
            class_table_day_week.setText("周");
            showTab();
        } else if (mode == 1) {//周视图
            day_classtable_container.setVisibility(View.GONE);
            class_table_day_week.setText("日");
            if (!LOAD_WEEK_DONE) {
                week_classtable.setVisibility(View.VISIBLE);
                week_classtable.loadUrl("file:///android_asset/weekclasstable/weekclasstable.html");
                LOAD_WEEK_DONE = true;
            } else {
                week_classtable.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 右侧菜单:more
     */
    @Click(R.id.class_table_more)
    void more_onclick() {
        class_table_more.setImageResource(R.drawable.icon_show_menu_off);
        View popupWindow_view = getLayoutInflater(getArguments()).inflate(R.layout.class_table_popmenu, null, false);
        //导入课程表

        popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        Button bt_import_class_table = (Button) popupWindow_view.findViewById(R.id.import_class_table);
        bt_import_class_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.eduSysPassword == null || app.eduSysPassword.equals("")) {

                    //弹出登录窗口
//                      Login_.intent(getActivity()).startTips(getString(R.string.start_tips_edusys)).start();
                } else {
                    loadData();
                }
                class_table_more.setImageResource(R.drawable.icon_show_menu);

                popupWindow.dismiss();


            }
        });
        //添加自定义课程
        Button bt_custom_class = (Button) popupWindow_view.findViewById(R.id.add_custom_class);
        bt_custom_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(ClassEditor_.intent(getActivity())
                        .isNewClass(true)
                        .model(new ClassModel()).get(), UIHelper.QUERY_FOR_EDIT_CLASS);
                class_table_more.setImageResource(R.drawable.icon_show_menu);
                popupWindow.dismiss();

            }
        });
        //智能显示
        Button bt_smart_class_table = (Button) popupWindow_view.findViewById(R.id.smart_class_table);
        bt_smart_class_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.config.smart_class_table().put(true);
                popupWindow.dismiss();
                class_table_more.setImageResource(R.drawable.icon_show_menu);
                AppToast.show(getActivity(), "智能显示课程", AppUIMeasure.build(tab_bar_layout)[1]);
                showClassTable();

            }
        });
        //显示所有
        Button bt_default_class_table = (Button) popupWindow_view.findViewById(R.id.default_class_table);
        bt_default_class_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.config.smart_class_table().put(false);
                popupWindow.dismiss();
                class_table_more.setImageResource(R.drawable.icon_show_menu);
                AppToast.show(getActivity(), "显示所有课程", AppUIMeasure.build(tab_bar_layout)[1]);
                showClassTable();
            }
        });

        Button[] menu_button = {bt_import_class_table, bt_custom_class, bt_smart_class_table, bt_default_class_table};
        for (Button bt : menu_button) {
            Drawable[] drawables = bt.getCompoundDrawables();
            drawables[0].setBounds(10, 10, 70, 70);
            bt.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        }
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        popupWindow.showAsDropDown(class_table_more, 0, 0);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                class_table_more.setImageResource(R.drawable.icon_show_menu);
                return false;
            }
        });
    }

    @AfterViews
    void initView() {
        school_week = classHelper.getSchoolWeek();
        school_week = (school_week == 0 ? 1 : school_week);
        listViews = new ArrayList<View>();
        adapter = new ClassTableAdapter();

        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(onPageChangeListener);
        titles.setListener(onTabChangeListener);

        showClassTable();
        showTab();

        webWeekClasstableHelper = new WebWeekClasstableHelper(getActivity(), week_classtable, app.config, app.dateUtil, classHelper);
        week_classtable.getSettings().setJavaScriptEnabled(true);
        week_classtable.addJavascriptInterface(webWeekClasstableHelper, "Android");
        week_classtable.getSettings().setSupportZoom(true);
        week_classtable.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //以下代码是为了修正部分机型显示课表不正常的bug
        LinearLayout.LayoutParams webLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
       /* DisplayMetrics dm =getResources().getDisplayMetrics();
        webLayoutParams.width = dm.widthPixels;*/
        float scale = getResources().getDisplayMetrics().density;
        Log.d("density is ", scale + "");
        week_classtable.setLayoutParams(webLayoutParams);

        //更新页面标题
        class_table_title.setText(getTitle());
        updateClassView(app.config.day_week().get());
    }

    /**
     * 改变周
     */
    @Click(R.id.title)
    void change_week() {
        startActivityForResult(UpdateCurrentWeek_.intent(getActivity()).current(String.valueOf(classHelper.getSchoolWeek())).get(), 10);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @UiThread
    void showWeekClass() {
        week_classtable.loadUrl("javascript:main()");
    }


    /**
     * 修改课程表后，接收来自修改课程表activiy回传的model,写入数据库，并且更新到界面；
     *
     * @param resultCode
     * @param data
     */
    @OnActivityResult(UIHelper.QUERY_FOR_EDIT_CLASS)
    void modifyClassOnResult(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            ClassModel model = (ClassModel) data.getSerializableExtra("class");
            createOrUpdateClassInformation(model);
            AppToast.show(getActivity(), "操作成功", AppUIMeasure.build(tab_bar_layout)[1]);

        }

    }

    @OnActivityResult(10)
    void show_custom_week(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            System.out.println("用户选择了第:" + data.getExtras().getInt("week"));
            school_week = data.getExtras().getInt("week");
            class_table_title.setText("第" + school_week + "周");
            showClassTable();
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
        int currentDay = app.dateUtil.getDayOfWeek() - 1;
        // 这句不能去掉，当日期为星期一时，就不重绘了，只有移动下它才重绘
        if (currentDay == 0)
            titles.changeWeekDay(1);
        pager.setCurrentItem(currentDay);
        titles.changeWeekDay(currentDay);
    }

    /**
     * 导入课表成功
     */
    @UiThread
    void showSuccess() {
        AppToast.show(getActivity(), "课表导入成功", AppUIMeasure.build(tab_bar_layout)[1]);
    }


    /**
     * 展示课程表,同时将课程表切换到今天.
     * 已经包含刷新全周课表的逻辑
     */
    @UiThread()
    void showClassTable() {
        // 以下是刷新日课表;
        int prevPosition = pager.getCurrentItem();
        listViews.clear();

        for (int i = 1; i <= 7; i++) {
            List<ClassModel> dayClassList = null;
            String chineseDay = app.dateUtil.numDayToChinese(i);
            if (app.config.smart_class_table().get()) {
                Log.i(getClass().getName(), "刷新课表:显示智能课表");
                dayClassList = classHelper.getDayLessonByWeek(chineseDay, school_week);

            } else {
                Log.i(getClass().getName(), "刷新课表:显示所有课表");
                dayClassList = classHelper.getDayLesson(chineseDay);
            }

            buildDayClassTableAdapter(dayClassList);
        }

        adapter.setViewList(listViews);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        pager.setCurrentItem(prevPosition);
        //刷新全周课表
        webWeekClasstableHelper.refreshClassTable();
        showWeekClass();

    }

    private void buildDayClassTableAdapter(List<ClassModel> dayClassList) {
        ListView classListView = UIHelper.buildClassListView(getActivity());
        ClassAdapter cAdapter = ClassAdapter_.getInstance_(getActivity());
        BaseAdapter _adapter = UIHelper.buildEffectAdapter(cAdapter, (AbsListView) classListView, EXPANDABLE_ALPHA);

        cAdapter.addAll(dayClassList);
        cAdapter.setFragment(this);
        classListView.setAdapter(_adapter);
        listViews.add(classListView);

    }

    /**
     * 线程从服务器加载课程表，同时保存在本地数据库，再行操作;
     */
    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        showUpdateClassProgressBar();
        httpLoader.updateClassTable(new HttpLoader.NormalCallBack() {
            @Override
            public void onSuccess(Object obj) {
                showSuccess();
                showClassTable();
                showTab();
                Log.i(getClass().getName(), "更新课表成功");
                hideUpdateClassProgressBar();
            }

            @Override
            public void onError(Object obj) {
                int requestCode = Integer.parseInt(obj.toString());
                showError(requestCode);
                Log.e(getClass().getName(), "更新课表失败:" + requestCode);
                hideUpdateClassProgressBar();
            }

            @Override
            public void onNetworkError(Object obj) {
                showNetWorkError(obj);
                Log.e(getClass().getName(), "网络错误");
                hideUpdateClassProgressBar();

            }
        });

    }

    @UiThread
    void showUpdateClassProgressBar() {
        AppProgress.show(getActivity(), "导入课表", "正在连接正方导入课表", "取消", new AppProgress.Callback() {
            @Override
            public void onCancel() {
                Log.i(getClass().getName(), "点击了取消更新课表");
                BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);//取消进程
            }
        });

    }

    @UiThread
    void hideUpdateClassProgressBar() {
        AppProgress.hide();
    }

//    private String getTitle() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(app.dateUtil.getWeekOfDate());
//        return builder.toString();
//    }

    private String getTitle() {
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.widget_week_start));
        builder.append(school_week);
        builder.append(getString(R.string.widget_week_end));
        return builder.toString();
    }


}