package cn.scau.scautreasure.ui;

import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.umeng.fb.FeedbackAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.AppUIMeasure;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.BadgeView;


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

    //校历
    @ViewById(R.id.menu_calendar)
    Button menu_calendar;

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
    //选课
    @ViewById(R.id.menu_subject)
    Button menu_subject;
    //查书
    @ViewById(R.id.menu_search)
    Button menu_search;
    //当前借阅
    @ViewById(R.id.menu_book)
    Button menu_book;
    //历史借阅
    @ViewById(R.id.menu_record)
    Button menu_record;

    @ViewById(R.id.menu_card)
    Button menu_card;

    /**
     * 设置页面
     */
    @Click(R.id.more)
    void settings() {
        Settings_.intent(getActivity()).start();
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

    /**
     * 查选修
     */
    @Click(R.id.menu_subject)
    void setMenu_subject() {
        PickClassInfo_.intent(this).start();

    }

    /**
     * 地图
     */
    @Click(R.id.menu_map)
    void menu_map() {
        Map_.intent(this).start();

    }

    /**
     * 校历
     */
    @Click(R.id.menu_calendar)
    void menu_calendar() {
//        Calendar_.intent(this).start();
        BaseBrowser_.intent(getActivity()).browser_title("校历").url("http://www.huanongbao.com").start();
    }

    /**
     * 常用信息
     */
    @Click(R.id.menu_info)
    void menu_info() {
        BaseBrowser_.intent(getActivity()).browser_title("常用信息").url("http://www.huanongbao.com").start();

    }

    /**
     * 校园卡
     */
    @Click(R.id.menu_card)
    void menu_card() {
        AppToast.show(getActivity(), "一百块都不给我,不用查了,你校园卡没钱", AppUIMeasure.getHeight());
    }


    @AfterViews
    void initView() {
        initButton();
        initListLayout();
    }

    void initListLayout() {

    }

    /**
     * 按钮图标的控制
     */
    void initButton() {
        Button[] menu_button = {menu_kfc, menu_card, menu_calendar, menu_map, menu_info, menu_room, menu_grade, menu_exam, menu_subject, menu_search, menu_book, menu_record};
        for (Button bt : menu_button) {
            Drawable[] drawables = bt.getCompoundDrawables();
            drawables[1].setBounds(0, 0, 55, 55);
            bt.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        }

    }

}
