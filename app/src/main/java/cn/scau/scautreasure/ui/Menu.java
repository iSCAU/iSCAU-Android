package cn.scau.scautreasure.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.LogUtil;
import com.devspark.appmsg.AppMsg;
import com.umeng.fb.FeedbackAgent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.AppContext_;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.api.CookieApi;
import cn.scau.scautreasure.api.EdusysApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.impl.OnTabSelectListener;
import cn.scau.scautreasure.model.CookieModel;
import cn.scau.scautreasure.model.LoginModel;
import cn.scau.scautreasure.util.CacheUtil;
import cn.scau.scautreasure.widget.BadgeView;


@EFragment(R.layout.menu)
public class Menu extends CommonFragment implements OnTabSelectListener {


    @RestService
    CookieApi api;

    CookieModel cookieModel;
    LoginModel loginModel;
    String code;

    int actionMark = 1;

    private ProgressDialog progressDialog;

    @AfterViews
    void initView() {
//        BadgeView badgeView=new BadgeView(getSherlockActivity(),activityInfo);
//        badgeView.setBackgroundResource(R.drawable.redpoin);
//        badgeView.show();


    }

    private void setCheckCode(ImageView imageView, String url) {
        AppContext.loadImage(url, imageView, null);

    }


    @Background
    void getCheckCode() {

        try {
            cookieModel = api.getCookie();
            showCheckcode();

        } catch (Exception e) {
            LogUtil.log.i("zzb_log:" + e.toString());
        } finally {
            colseProgressDialog();
        }

    }

    @Background
    void loginServer() {

        try {

            loginModel = api.loginCookie(AppContext.userName, app.getEncodeEduSysPassword(), cookieModel.getCookie(), code);
            if (loginModel.getStatus() == 1) {
                LogUtil.log.i("zzb_log" + loginModel.getMsg());
                goAction(actionMark);
            } else {
                AppContext_.islogin = false;
                LogUtil.log.i("zzb_log" + loginModel.getMsg());
                showErrorMsg(loginModel.getMsg());

            }


        } catch (Exception e) {
            LogUtil.log.i(e.toString());
        } finally {
            colseProgressDialog();
        }
    }

    @UiThread
    void showErrorMsg(String msg){
        AppMsg.makeText(getSherlockActivity(),msg, AppMsg.STYLE_ALERT).show();
    }

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
        setCheckCode(imageView, cookieModel.getImg());
    }

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

    @Click
    void menu_goal() {
        showProgressDialog(getActivity());
        actionMark = 1;
        getCheckCode();

    }

    void goAction(int n) {
        switch (n) {
            case 1:
                Param_.intent(this)
                        .target("goal")
                        .targetActivity(Goal_.class.getName())
                        .start();
                ;
                break;
            case 2:
                Param_.intent(this)
                        .target("emptyClassRoom")
                        .targetActivity(EmptyClassRoom_.class.getName())
                        .start();
                break;
            case 3:
                Exam_.intent(this).start();
                break;
            case 4:
                PickClassInfo_.intent(this).start();
                break;
            default:
                break;
        }
    }

    private boolean isLoginCookie(Context context) {
        CacheUtil cacheUtil = CacheUtil.get(context);
        if (cacheUtil.getAsString("cookie") != null) {
            LogUtil.log.i("设置：true" + cacheUtil.getAsString("cookie"));
            return true;
        } else {
            LogUtil.log.i("设置：false" + cacheUtil.getAsString("cookie"));
            return false;
        }
    }



    @Click
    void menu_exam() {

        showProgressDialog(getActivity());
        actionMark = 3;
        getCheckCode();

    }

    @Click
    void menu_settings() {
        Settings_.intent(this).start();
    }

    @Click
    void menu_pickCourseInfo() {

        actionMark = 4;
        getCheckCode();

    }

    @Click
    void menu_emptyClassRoom() {

        showProgressDialog(getActivity());
        actionMark = 2;
        getCheckCode();

    }

    @Click
    void menu_searchBook() {
        SearchBook_.intent(this).start();
    }

    @Click
    void menu_nowBorrowedBook() {
        BorrowedBook_.intent(this)
                .target(UIHelper.TARGET_FOR_NOW_BORROW)
                .start();
    }

    @Click
    void menu_pastBorrowedBook() {
        BorrowedBook_.intent(this)
                .target(UIHelper.TARGET_FOR_PAST_BORROW)
                .start();
    }

    @Click
    void menu_lifeinformation() {
        Introduction_.intent(this)
                .target("LifeInformation")
                .title(R.string.menu_lifeinformation)
                .start();
    }

    @Click
    void menu_communityinformation() {
        Introduction_.intent(this)
                .target("CommunityInformation")
                .title(R.string.menu_communityinformation)
                .start();
    }

    @Click
    void menu_guardianserves() {
        Introduction_.intent(this)
                .target("GuardianServes")
                .title(R.string.menu_guardianserves)
                .start();
    }

    @Click
    void menu_studyinformation() {
        Introduction_.intent(this)
                .target("StudyInformation")
                .title(R.string.menu_studyinformation)
                .start();
    }

    @Click
    void menu_busandtelphone() {
        Introduction_.intent(this)
                .target("Bus&Telphone")
                .title(R.string.menu_busandtelphone)
                .start();
    }


//    @Click
//    void menu_calendar() {
//        Calendar_.intent(this).start();
//    }

    @Click
    void menu_notice() {
        Notice_.intent(this).start();
    }

//    @Click
//    void menu_english() {
//        //此频道建设中
//        English_.intent(this).start();
//    }

    @Click
    void menu_contact() {

        FeedbackAgent agent = new FeedbackAgent(getSherlockActivity());
        agent.startFeedbackActivity();
    }

    @Click
    void menu_map() {
        Map_.intent(this).start();
    }

    @Override
    public void onTabSelect() {
        setTitle(R.string.title_menu);
        setSubTitle(null);
    }


}
