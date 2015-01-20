package cn.scau.scautreasure.ui;

import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.umeng.fb.FeedbackAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.widget.BadgeView;


@EFragment(R.layout.menu)
public class FragmentApp extends BaseFragment {

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

    @Click(R.id.menu_calendar)
    void menu_calendar() {
//        Calendar_.intent(this).start();
        BaseBrowser_.intent(getActivity()).browser_title("校历").url("http://www.btbaba.com").start();
    }

    @Click(R.id.menu_info)
    void menu_info() {
        BaseBrowser_.intent(getActivity()).browser_title("常用信息").url("http://www.btbaba.com").start();

    }

    @AfterViews
    void initView() {
        initButton();
    }

    void initButton() {
        Button[] menu_button = {menu_kfc, menu_calendar, menu_map, menu_info, menu_room, menu_grade, menu_exam, menu_subject, menu_search, menu_book, menu_record};
        for (Button bt : menu_button) {
            Drawable[] drawables = bt.getCompoundDrawables();
            drawables[1].setBounds(0, 0, 55, 55);
            bt.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        }

    }


//
//    @Click
//    void menu_exam() {
//        Exam_.intent(this).start();
//    }
//
//    @Click
//    void menu_settings() {
//        Settings_.intent(this).start();
//    }
//
//    @Click
//    void menu_pickCourseInfo() {
//        PickClassInfo_.intent(this).start();
//    }
//
//    @Click
//    void menu_emptyClassRoom() {
//        Param_.intent(this)
//                .target("emptyClassRoom")
//                .targetActivity(EmptyClassRoom_.class.getName())
//                .start();
//    }
//
//    @Click
//    void menu_searchBook() {
//        SearchBook_.intent(this).start();
//    }
//
//    @Click
//    void menu_nowBorrowedBook() {
//        BorrowedBook_.intent(this)
//                .target(UIHelper.TARGET_FOR_NOW_BORROW)
//                .start();
//    }
//
//    @Click
//    void menu_pastBorrowedBook() {
//        BorrowedBook_.intent(this)
//                .target(UIHelper.TARGET_FOR_PAST_BORROW)
//                .start();
//    }
//
//    @Click
//    void menu_lifeinformation() {
//        Introduction_.intent(this)
//                .target("LifeInformation")
//                .title(R.string.menu_lifeinformation)
//                .start();
//    }
//
//    @Click
//    void menu_communityinformation() {
//        Introduction_.intent(this)
//                .target("CommunityInformation")
//                .title(R.string.menu_communityinformation)
//                .start();
//    }
//
//    @Click
//    void menu_guardianserves() {
//        Introduction_.intent(this)
//                .target("GuardianServes")
//                .title(R.string.menu_guardianserves)
//                .start();
//    }
//
//    @Click
//    void menu_studyinformation() {
//        Introduction_.intent(this)
//                .target("StudyInformation")
//                .title(R.string.menu_studyinformation)
//                .start();
//    }
//
//    @Click
//    void menu_busandtelphone() {
//        Introduction_.intent(this)
//                .target("Bus&Telphone")
//                .title(R.string.menu_busandtelphone)
//                .start();
//    }
//
//
//    @Click
//    void menu_calendar() {
//        Calendar_.intent(this).start();
//    }
//
//    @Click
//    void menu_notice() {
//        Notice_.intent(this).start();
//    }
//
//    @Click
//    void menu_english() {
//        //此频道建设中
//        English_.intent(this).start();
//
//    }
//
//    @Click
//    void menu_contact() {
//
//        FeedbackAgent agent = new FeedbackAgent(getActivity());
//        agent.startFeedbackActivity();
//    }
//
//    @Click
//    void menu_map() {
//        Map_.intent(this).start();
//    }
//
//


}
