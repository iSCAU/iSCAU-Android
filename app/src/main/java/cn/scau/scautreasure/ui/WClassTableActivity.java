package cn.scau.scautreasure.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.actionbarsherlock.app.ActionBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.ClassHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.util.DateUtil;

/**
 * 整周课程表，基本上使用callback来处理View与View之间，View与activity的通信
 * Created by stcdasqy on 14-4-20.
 */
@EFragment(R.layout.whole_class_table)
public class WClassTableActivity extends Common {
    @ViewById
    ClassTableLine iv;
    Bitmap bm;

    @ViewById
    LinearLayout tab_cell,cell_left,ll_tab;
    @ViewById
    ClassTableLinearLayout ll_out;

    @Bean
    ClassHelper classHelper;
    @Bean
    DateUtil dateUtil;

    @ViewById
    FrameLayout whole_class_table;

    @ViewById
    FrameLayout fl_in,fl_tab;

    private int cell_h,cell_w,cl_w,top_h,parentWidth;
    private ClassTableTabLine tabLine;

    @ViewById(R.id.Hscroll)
    HorizontalScrollView hScroll;
    @ViewById
    ScrollView sv;

    private Context mContext;
    private boolean hasClassInNight = false,hasClassInNoon = false,hasClassInWeekDay = false;
    private ClassButton buttonAdd;
    private int ClickX,ClickY;
    private Handler handler;
    /*在用户编辑课程信息之后，startClassInfoDialogAgain 来协助重启dialog以更新界面信息
      (重启也即先dismiss 后show)*/
    private boolean firstTimeToSetWidth = true,startClassInfoDialogAgain = false;
    private ClassButton nowLongTouch,nowClick;

    public int[] colorTable = new int[]{};
    private int goodColor = 12;

    private ClassTableLine.fitHeightCallBack fitHeightCallBack = new ClassTableLine.fitHeightCallBack() {
        @Override
        public void onChanged(int height) {
            setHeight(height);
        }
    };
    private ClassTableLinearLayout.WidthCallBack widthCallBack = new ClassTableLinearLayout.WidthCallBack() {
        @Override
        public void onSetWidth(int w) {
            parentWidth = w;
            afterSetWidth();
        }
    };

    private List<ClassModel> allLessons;
    void initCourse(boolean auto){
        if(auto) allLessons = classHelper.getAllLesson();
        colorTable = ColorTableDialog.getColor();
        for(int i=0;i<allLessons.size();i++){
            ClassModel lesson = allLessons.get(i);
            String classNode = lesson.getNode();
            if(classNode.indexOf("5")>=0 || classNode.indexOf("6")>=0){
                hasClassInNoon = true;
            }
            if(classNode.indexOf("11")>=0 || classNode.indexOf("12")>=0
                    || classNode.indexOf("13")>=0)
                hasClassInNight = true;
            String Day = allLessons.get(i).getDay();
            if(Day.indexOf("日")>=0 || Day.indexOf("六")>=0)
                hasClassInWeekDay = true;
            int color = lesson.getColor();
            if(color == 0){
                color = lesson.getClassname().charAt(0);
                color = colorTable[color % goodColor];
                lesson.setColor(color);
                classHelper.createOrUpdateLesson(lesson);
            }
        }
        ll_out.setHasClassInWeekDay(hasClassInWeekDay);
        iv.setHasClassInNoonAndNight(hasClassInNoon,hasClassInNight);

    }

    void initView(){
        /*
        tab_cell.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                Log.d("com.my", "chanced+ h=" + v.getHeight());
            }
        });*/
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.bluebackground);
        iv.setBackgroundBitmap(bm);
        iv.setScaleType(ImageView.ScaleType.FIT_START);
        iv.setAlpha(100);

        iv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                ClickX = (int)event.getX();
                ClickY = (int)event.getY();
                return false;
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ClickX > 0 && ClickY > 0) {
                    if (ClickX <= cell_w) return;
                    if (buttonAdd != null) {
                        updateButton(buttonAdd);
                        int marginTop = ClickY / cell_h * cell_h;
                        int marginLeft = cl_w + ((ClickX - cl_w) / cell_w) * cell_w;
                        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(cell_w, cell_h);
                        flp.setMargins(marginLeft, marginTop, 0, 0);
                        flp.gravity = Gravity.TOP | Gravity.LEFT;
                        buttonAdd.setLayoutParams(flp);
                        buttonAdd.setVisibility(View.VISIBLE);
                        buttonAdd.requestLayout();
                    }
                }
            }
        });
        buttonAdd = new ClassButton(getActivity());
        buttonAdd.setText("+");
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_add_class();
            }
        });
        buttonAdd.setVisibility(View.INVISIBLE);
        fl_in.addView(buttonAdd);
        /*我需要得到出表头（也即 星期一 到 星期日）的高度，需要手动调用measure*/
        {
            int max = 200;
            int w = View.MeasureSpec.makeMeasureSpec(max, View.MeasureSpec.AT_MOST);
            int h = View.MeasureSpec.makeMeasureSpec(max, View.MeasureSpec.AT_MOST);
            tab_cell.measure(w, h);
            cell_h = tab_cell.getMeasuredHeight();
            Log.d("com.my","First Measure cell_h="+cell_h);
        }
    }

    void calcCellWidth(){
        int width = parentWidth;
        if(hasClassInWeekDay){
            cell_w = (int)(width * (10f / 11 / 7));
            cl_w = (int)(width * 1f / 11);
        }else{
            cell_w = (int)(width * 10f/7 / (1+5f*10/7));
            cl_w = (int)(width /(1+5f*10/7));
        }
    }
    void afterSetWidth(){
        calcCellWidth();
        if(firstTimeToSetWidth){
            firstTimeToSetWidth = false;
            tabLine = new ClassTableTabLine(getActivity());
            tabLine.setWidth(cell_w, cl_w);
            tabLine.setHeight(cell_h);
            fl_tab.addView(tabLine,1);

            cell_h = 70;
            iv.setCallback(fitHeightCallBack);
            iv.setWidth(cell_w, cl_w);
            iv.setHeight(cell_h);

            addCourseButton();
        }
        else {
            tabLine.setWidth(cell_w, cl_w);
            iv.setWidth(cell_w, cl_w);
            updateCourseButton();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //android回收机制搞的不太清楚，反正，如果此处不这么做的话，多开几次，就会outOfMemeroy
        if(!bm.isRecycled())
            bm.recycle();
        Log.d("onDestroy","onDestroy");
    }
    private ColorTableDialog.ColorTableCallBack colorTableCallBack = new ColorTableDialog.ColorTableCallBack() {
        @Override
        public void onColorChoose(int color) {
            if(nowLongTouch != null){
                nowLongTouch.setColor(color);
                nowLongTouch.requestLayout();
                ClassModel classModel = nowLongTouch.getClassModel();
                classModel.setColor(color);
                classHelper.createOrUpdateLesson(classModel);
            }
        }
    };
    private ArrayList<ClassButton> course = new ArrayList<ClassButton>();
    public void addCourseButton(){
        //updateClassTableCommon();
        ll_out.setClickXYCallBack(new ClassTableLinearLayout.ClickXYCallBack() {
            @Override
            public void onXYChanged(int x, int y) {
                ClickX = x; ClickY = y;
            }
        });
        for(int i=0;i<allLessons.size();i++){
            ClassModel classModel = allLessons.get(i);
            ClassButton a = buildButtonFromCourse(classModel);
            course.add(a);
            fl_in.addView(a);
        }
        //updateCourseButton();
    }

    private ClassButton buildButtonFromCourse(ClassModel classModel){
        String node = classModel.getNode();
        ClassButton a = new ClassButton(getActivity());
        if(!classModel.getLocation().equals(""))
            a.setText(classModel.getClassname()+"\n"+classModel.getLocation());
        else
            a.setText(classModel.getClassname());
        int day = dateUtil.chineseToNumDay(classModel.getDay());
        String nodeList[] = node.split(",");
        int startNode = Integer.valueOf(nodeList[0]);
        int endNode = Integer.valueOf(nodeList[nodeList.length-1]);
        int node_num = endNode - startNode + 1;
        a.setClassModel(classModel);
        a.setDay(day);
        a.setStartNode(startNode);
        a.setNode_num(node_num);
        int color = classModel.getColor();
        a.setColor(color);
        a.setTextColor(Color.WHITE);

        updateButton(a);
        //这只是默认大小罢了，会有自适应屏幕分辨率大小的字体设置处理的。
        a.setTextSize(15);
        //提高一下button上文字空间的利用率
        a.setPadding(1, 1, 1, 1);
        a.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                nowClick = (ClassButton)v;
                ClassModel classModel = nowClick.getClassModel();
                Bundle b = new Bundle();
                b.putSerializable("ClassModel",classModel);
                ClassInfoDialog classInfoDialog = new ClassInfoDialog(mContext,b);
                classInfoDialog.setCanceledOnTouchOutside(false);
                classInfoDialog.setCallBack(new ClassInfoDialog.onDeleteClick() {
                    @Override
                    public void onClick() {
                        allLessons.remove(nowClick.getClassModel());
                        classHelper.deleteLesson(nowClick.getClassModel());
                        fl_in.removeView(nowClick);
                        initCourse(false);
                    }
                },new ClassInfoDialog.onEditClick() {
                    @Override
                    public void onClick() {
                        startActivityForResult(ClassEditor_.intent(getSherlockActivity())
                                .model(nowClick.getClassModel()).get(), UIHelper.QUERY_FOR_EDIT_CLASS);
                        startClassInfoDialogAgain = true;
                    }
                });
                classInfoDialog.show();
            }
        });
        a.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nowLongTouch = (ClassButton)v;
                ColorTableDialog colorTableDialog = new ColorTableDialog(mContext,R.style.ColorTableDialog);//创建Dialog并设置样式主题
                colorTableDialog.setOnColorChoose(colorTableCallBack);
                Window win = colorTableDialog.getWindow();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                int height = hScroll.getHeight();
                int width = hScroll.getWidth();
                if(ClickX < width /2)
                    params.x = ClickX + cell_w*2 - width/2;
                else params.x = ClickX - cell_w*2 - width/2;
                params.y = ClickY - height/2;
                win.setAttributes(params);
                colorTableDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
                colorTableDialog.show();
                return false;
            }
        });
        return a;
    }

    public void setHeight(int h){
        cell_h = h;
        updateCourseButton();
    }

    private void updateCourseButton(){
        //updateClassTableCommon();
        int i;
        for(i=0;i<course.size();i++){
            updateButton(course.get(i));
        }
    }
    private void updateButton(ClassButton b){
        int node_num = b.getNode_num();
        int startNode = b.getStartNode();
        if(!hasClassInNight && startNode>10) startNode--;
        if(!hasClassInNoon && startNode>4) startNode--;
        int day = b.getDay();
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(cell_w,cell_h*node_num);
        flp.setMargins(cl_w + day * cell_w, (startNode - 1) * cell_h, 0, 0);
        flp.gravity = Gravity.TOP | Gravity.LEFT;
        b.setLayoutParams(flp);
    }

    /*@Override 默认情况下 onWindowFocusChanged 可以把大多数控件长度和宽度测量好
    public void onWindowFocusChanged(boolean hasFocus) {
        //super.onWindowFocusChanged(hasFocus);
        Log.v("com.my","Focus Changed :"+hasFocus);
        //if(!hasFocus) return;
        if(!hasFocus){
            return;
        }
        calcCellWidth();
        afterView();
    }*/

    @AfterViews
    void afterView(){
        mContext = getActivity();
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(R.string.menu_whole_classtable);
        addIgnoredView(whole_class_table);
        initCourse(true);
        initView();
        ll_out.setWidthCallBack(widthCallBack);
        ll_out.setParentView(whole_class_table);
    }

    @OptionsItem
    void menu_add_class(){
        ClassModel classModel = new ClassModel();
        int node = ClickY / cell_h;
        if(!hasClassInNoon && node>5) node++;
        classModel.setNode(""+node);
        int day = (ClickX - cl_w) / cell_w + 1;
        classModel.setDay(dateUtil.numDayToChinese(day));
        startActivityForResult(ClassEditor_.intent(getSherlockActivity())
                .model(classModel).get(), UIHelper.QUERY_FOR_EDIT_CLASS);
    }
    @OnActivityResult(UIHelper.QUERY_FOR_EDIT_CLASS)
    void modifyClassOnResult(int resultCode,Intent data){
        if(resultCode == getSherlockActivity().RESULT_OK){
            ClassModel classModel = (ClassModel) data.getSerializableExtra("class");
            allLessons.add(0,classModel);
            initCourse(false);
            ClassButton a = buildButtonFromCourse(classModel);
            course.add(a);
            fl_in.addView(a);
            //classHelper.createOrUpdateLesson(classModel);
            updateLesson(classModel);
            if(startClassInfoDialogAgain){
                startClassInfoDialogAgain = false;
                nowClick.performClick();
            }
        }
    }
    @Background
    void updateLesson(ClassModel classModel){
        classHelper.createOrUpdateLesson(classModel);
    }



    public void logout(String tab,View v){
        Log.v(tab,tab + " Height : "+v.getHeight()+" Width :"+v.getWidth());
    }
}
