package cn.scau.scautreasure.ui;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Click;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;


@EFragment(R.layout.menu)
public class Menu extends CommonFragment implements OnTabSelectListener{

    @Click
    void menu_goal(){
        Param_.intent(this)
              .target("goal")
              .targetActivity(Goal_.class.getName())
              .start();
    }

    @Click
    void menu_exam(){
       Exam_.intent(this).start();
    }

    @Click
    void menu_pickCourseInfo(){
        PickClassInfo_.intent(this).start();
    }

    @Click
    void menu_emptyClassRoom(){
        Param_.intent(this)
              .target("emptyClassRoom")
              .targetActivity(EmptyClassRoom_.class.getName())
              .start();
    }

    @Click
    void menu_searchBook(){
        SearchBook_.intent(this).start();
    }

    @Click
    void menu_nowBorrowedBook(){
        BorrowedBook_.intent(this)
                     .target(UIHelper.TARGET_FOR_NOW_BORROW)
                     .start();
    }

    @Click
    void menu_pastBorrowedBook(){
        BorrowedBook_.intent(this)
             .target(UIHelper.TARGET_FOR_PAST_BORROW)
             .start();
    }

    @Click
    void menu_lifeinformation(){
        Introduction_.intent(this)
                     .target("LifeInformation")
                     .title(R.string.menu_lifeinformation)
                     .start();
    }

    @Click
    void menu_communityinformation(){
        Introduction_.intent(this)
                    .target("CommunityInformation")
                    .title(R.string.menu_communityinformation)
                    .start();
    }

    @Click
    void menu_guardianserves(){
        Introduction_.intent(this)
                    .target("GuardianServes")
                    .title(R.string.menu_guardianserves)
                    .start();
    }

    @Click
    void menu_studyinformation(){
        Introduction_.intent(this)
                    .target("StudyInformation")
                    .title(R.string.menu_studyinformation)
                    .start();
    }

    @Click
    void menu_busandtelphone(){
        Introduction_.intent(this)
                    .target("Bus&Telphone")
                    .title(R.string.menu_busandtelphone)
                    .start();
    }

    @Click
    void menu_calendar(){
        Calendar_.intent(this).start();
    }

    @Click
    void menu_notice(){
        Notice_.intent(this).start();
    }

    @Override
    public void onTabSelect() {
        setTitle(R.string.title_menu);
        setSubTitle(null);
    }
}
