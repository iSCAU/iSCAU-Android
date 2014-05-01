package cn.scau.scautreasure.ui;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Click;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;


@EFragment(R.layout.menu)
public class Menu extends Common{


    @Click
    void menu_classtable(){
        UIHelper.startFragment(getSherlockActivity(), ClassTable_.builder().build());
    }

    @Click
    void menu_goal(){
        UIHelper.startFragment(getSherlockActivity(), Param_.builder().build(),"target","goal","targetFragment",Goal_.class.getName());
    }

    @Click
    void menu_exam(){
        UIHelper.startFragment(getSherlockActivity(), Exam_.builder().build());
    }

    @Click
    void menu_pickCourseInfo(){
        UIHelper.startFragment(getSherlockActivity(), PickClassInfo_.builder().build());
    }

    @Click
    void menu_emptyClassRoom(){
        UIHelper.startFragment(getSherlockActivity(), Param_.builder().build(),"target","emptyclassroom","targetFragment",EmptyClassRoom_.class.getName());
    }

    @Click
    void menu_searchBook(){
        UIHelper.startFragment(getSherlockActivity(), SearchBook_.builder().build());
    }

    @Click
    void menu_nowBorrowedBook(){
        UIHelper.startFragment(getSherlockActivity(), BorrowedBook_.builder().build(),"target",UIHelper.TARGET_FOR_NOW_BORROW);
    }

    @Click
    void menu_pastBorrowedBook(){
        UIHelper.startFragment(getSherlockActivity(), BorrowedBook_.builder().build(),"target",UIHelper.TARGET_FOR_PAST_BORROW);
    }

    @Click
    void menu_lifeinformation(){
        UIHelper.startFragment(getSherlockActivity(), Introduction_.builder().build(),"target","LifeInformation","title",R.string.menu_lifeinformation);
    }

    @Click
    void menu_communityinformation(){
        UIHelper.startFragment(getSherlockActivity(), Introduction_.builder().build(),"target","CommunityInformation","title",R.string.menu_communityinformation);
    }

    @Click
    void menu_guardianserves(){
        UIHelper.startFragment(getSherlockActivity(), Introduction_.builder().build(),"target","GuardianServes","title",R.string.menu_guardianserves);
    }

    @Click
    void menu_studyinformation(){
        UIHelper.startFragment(getSherlockActivity(), Introduction_.builder().build(),"target","StudyInformation","title",R.string.menu_studyinformation);
    }

    @Click
    void menu_busandtelphone(){
        UIHelper.startFragment(getSherlockActivity(), Introduction_.builder().build(),"target","Bus&Telphone","title",R.string.menu_busandtelphone);
    }

    @Click
    void menu_calendar(){
        UIHelper.startFragment(getSherlockActivity(), Calendar_.builder().build());
    }

    @Click
    void menu_notice(){
        UIHelper.startFragment(getSherlockActivity(), Notice_.builder().build());
    }
}
