package cn.scau.scautreasure.ui;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.AppUIMeasure;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.BadgeView;
import cn.scau.scautreasure.widget.ItemButton;


@EFragment(R.layout.menu)
public class FragmentApp extends BaseFragment {

    @ViewById(R.id.menu_listLayout)
    LinearLayout menu_listLayout;

    //外卖
    @ViewById(R.id.menu_kfc)
    Button menu_kfc;

    @Click(R.id.menu_kfc)
    void kfc() {
        Food_.intent(getActivity()).start();
    }


    //地图
    @ViewById(R.id.menu_map)
    Button menu_map;

    //常用信息
    @ViewById(R.id.menu_info)
    Button menu_info;

    //空教室
    @ViewById(R.id.menu_room)
    Button menu_room;

    //成绩
    @ViewById(R.id.menu_grade)
    Button menu_grade;

    //考试
    @ViewById(R.id.menu_exam)
    Button menu_exam;

    //查书
    @ViewById(R.id.menu_search)
    Button menu_search;
    //当前借阅
    @ViewById(R.id.menu_book)
    Button menu_book;
    //历史借阅
    @ViewById(R.id.menu_record)
    Button menu_record;


    @Click(R.id.more)
    void settings() {
    }

    /**
     * 搜索图书
     */
    @Click(R.id.menu_search)
    void menu_search_book() {
        SearchBook_.intent(getActivity()).start();
    }

    /**
     * 当前借阅
     */
    @Click(R.id.menu_book)
    void menu_book() {
        BorrowedBook_.intent(getActivity()).target(UIHelper.TARGET_FOR_NOW_BORROW).start();
    }

    /**
     * 借书历史记录
     */
    @Click(R.id.menu_record)
    void menu_record() {
        BorrowedBook_.intent(getActivity()).target(UIHelper.TARGET_FOR_PAST_BORROW).start();

    }

    /**
     * 空余教室
     */
    @Click(R.id.menu_room)
    void menu_room() {
        Param_.intent(this)
                .target("emptyClassRoom")._title("查空教室")
                .targetActivity(EmptyClassRoom_.class.getName())
                .start();
    }

    /**
     * 查成绩
     */
    @Click(R.id.menu_grade)
    void menu_grade() {
        Param_.intent(this)
                .target("goal")._title("查成绩")
                .targetActivity(Goal_.class.getName())
                .start();
    }

    /**
     * 考试安排
     */
    @Click(R.id.menu_exam)
    void menu_exam() {
        Exam_.intent(this).start();
    }

//    /**
//     * 查选修
//     */
//    @Click(R.id.menu_subject)
//    void setMenu_subject() {
//        PickClassInfo_.intent(this).start();
//
//    }

    /**
     * 地图
     */
    @Click(R.id.menu_map)
    void menu_map() {
        Map_.intent(this).start();

    }

//    /**
//     * 校历
//     */
//    @Click(R.id.menu_calendar)
//    void menu_calendar() {
////        Calendar_.intent(this).start();
//        BaseBrowser_.intent(getActivity()).browser_title("校历").url("http://www.huanongbao.com").start();
//    }

    /**
     * 常用信息
     */
    @Click(R.id.menu_info)
    void menu_info() {
        BaseBrowser_.intent(getActivity()).browser_title("常用信息").url("http://www.huanongbao.com").start();

    }
//
//    /**
//     * 校园卡
//     */
//    @Click(R.id.menu_card)
//    void menu_card() {
//        AppToast.show(getActivity(), "一百块都不给我,不用查了,你校园卡没钱", AppUIMeasure.getHeight());
//    }


    @AfterViews
    void initViews() {
        if (!isAfterViews) {
            isAfterViews = true;
            System.out.println("应用");
            initButton();
            initListLayout();
        }
    }

    List<ItemButton> list;

    void initListLayout() {
        list = new ArrayList<ItemButton>();
        list.add(new ItemButton(getActivity(), "挂科榜单", "看看这学期有多少人挂科了", "http://www.baidu.com/img/baidu_jgylogo3.gif", ""));
        list.add(new ItemButton(getActivity(), "逃课达人", "你逃课频率是多少", "http://www.baidu.com/img/baidu_jgylogo3.gif", ""));
        list.add(new ItemButton(getActivity(), "学霸排行", "愿得一学霸,考试坐我旁", "http://www.baidu.com/img/baidu_jgylogo3.gif", ""));
        list.add(new ItemButton(getActivity(), "挂科榜单", "看看这学期有多少人挂科了", "http://www.baidu.com/img/baidu_jgylogo3.gif", ""));
        list.add(new ItemButton(getActivity(), "逃课达人", "你逃课频率是多少", "http://www.baidu.com/img/baidu_jgylogo3.gif", ""));
        list.add(new ItemButton(getActivity(), "学霸排行", "愿得一学霸,考试坐我旁", "http://www.baidu.com/img/baidu_jgylogo3.gif", ""));
        menu_listLayout.removeAllViews();
        for (ItemButton itemButton : list) {
            menu_listLayout.addView(itemButton);
            Log.i(getClass().getName(), itemButton.getTv_title().getText().toString());
        }


    }

    /**
     * 按钮图标的控制
     */
    void initButton() {
        Button[] menu_button = {menu_kfc, menu_map, menu_info, menu_room, menu_grade, menu_exam, menu_search, menu_book, menu_record};
        for (Button bt : menu_button) {
            Drawable[] drawables = bt.getCompoundDrawables();
            drawables[1].setBounds(0, 0, 80, 80);
            bt.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        }

    }

}
