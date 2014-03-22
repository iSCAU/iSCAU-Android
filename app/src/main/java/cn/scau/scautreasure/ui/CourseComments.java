package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.CourseCommentsAdapter;
import cn.scau.scautreasure.api.CourseApi;
import cn.scau.scautreasure.helper.PostHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.CourseCommentModel;
import cn.scau.scautreasure.model.CourseModel;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.SWING;

/**
 * 2014-3-18
 */
@EActivity(R.layout.course_detail)
public class CourseComments extends InjectedSherlockActivity implements DialogInterface.OnCancelListener {
    public class Paths {
        private static final String URL = "http://76.191.112.146/course_comments/index.php?s=Api/addComment";
        private static final String ROOT_URL = "http://76.191.112.146/course_comments/index.php?s=Api/";
        private static final String LIKE_URL = ROOT_URL + "likedCourse";
        private static final String DISLIKE_URL = ROOT_URL + "dislikedCourse";
    }

    @RestService
    CourseApi api;

    @App
    AppContext app;
    @Extra
    Bundle courseBundle;
    @ViewById(R.id.listView)
    PullToRefreshListView pullListView;
    @ViewById
    TextView t1, t2, t3;
    @ViewById
    Button up, down;
    @ViewById
    LinearLayout progress;
    private CourseCommentModel courseCommentModel;
    private static String userNameTemp = null;
    private int CourseId;
    private int SumPage = 1;
    private CourseModel courseModel;
    private boolean endOfPage = false;
    private CourseCommentModel.CourseCommentList courseCommentList;
    private CourseCommentsAdapter commentsAdapter;
    private MultiValueMap params = new LinkedMultiValueMap<String, String>();
    private MultiValueMap paramsUpDown = new LinkedMultiValueMap<String, String>();
    private BaseAdapter adapter;


    private PostHelper.PostCallBack voteBack = new PostHelper.PostCallBack() {
        public void onSuccess() {
            showInfo(getString(R.string.comments_vote_success));
        }

        public void onFail(int extraMsgId) {
            showResult(CourseComments.this, extraMsgId);
        }
    };

    @UiThread
    void showResult(Activity ctx, int extraMsgId) {
        if (ctx == null) return;
        AppMsg.makeText(ctx, extraMsgId, AppMsg.STYLE_INFO).show();
    }


    @Click
    void tucao() {


        final Dialog dialog = new Dialog(CourseComments.this, R.style.MyDialog);
        //设置ContentView
        dialog.setContentView(R.layout.tu_cao_layout);
        dialog.show();
        dialog.setCancelable(false);
        final Button cancelButton = (Button) dialog.findViewById(R.id.pop_cancel);
        final Button finishButton = (Button) dialog.findViewById(R.id.pop_finish);
        final CheckBox isUnName = (CheckBox) dialog.findViewById(R.id.pop_is_unname);
        final CheckBox hasHomework = (CheckBox) dialog.findViewById(R.id.pop_hashomework);
        final CheckBox isCheck = (CheckBox) dialog.findViewById(R.id.pop_ischeck);
        final EditText editText = (EditText) dialog.findViewById(R.id.pop_username);
        final EditText editText1 = (EditText) dialog.findViewById(R.id.pop_exam);
        final EditText editText2 = (EditText) dialog.findViewById(R.id.pop_comments);
        DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    if (courseCommentModel == null)
                        courseCommentModel = new CourseCommentModel();
                    courseCommentModel.setComment(editText2.getText().toString());
                    courseCommentModel.setExam(editText1.getText().toString());
                    courseCommentModel.setIsCheck(isCheck.isChecked());
                    courseCommentModel.setHasHomework(hasHomework.isChecked());
                    courseCommentModel.setUserName(editText.getText().toString());
                    return true;
                } else {
                    return false;
                }

            }
        };
        dialog.setOnKeyListener(keylistener);
        if (courseCommentModel != null) {
            isUnName.setChecked(courseCommentModel.getUserName().toString().equals(getString(R.string.un_name)) ? true : false);
            editText.setEnabled(courseCommentModel.getUserName().toString().equals(getString(R.string.un_name)) ? false : true);
            editText1.requestFocus();
            editText.setText(courseCommentModel.getUserName().toString());
            editText1.setText(courseCommentModel.getExam().toString());
            editText2.setText(courseCommentModel.getComment());
            isCheck.setChecked(courseCommentModel.getIsCheck());
            hasHomework.setChecked(courseCommentModel.getHasHomework());
        } else {
            editText.setText(app.userName);
        }
        isUnName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userNameTemp = editText.getText().toString();
                    editText1.requestFocus();
                    editText.setText(getString(R.string.un_name));
                    editText.setEnabled(false);
                } else {
                    editText.requestFocus();
                    editText.setText(userNameTemp);
                    editText.setEnabled(true);
                }
            }
        });

        View.OnClickListener popClick = new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (courseCommentModel == null)
                    courseCommentModel = new CourseCommentModel();
                courseCommentModel.setComment(editText2.getText().toString().trim());
                courseCommentModel.setExam(editText1.getText().toString().trim());
                courseCommentModel.setIsCheck(isCheck.isChecked());
                courseCommentModel.setHasHomework(hasHomework.isChecked());
                courseCommentModel.setUserName(editText.getText().toString().trim());
                dialog.dismiss();
                if (arg0 == finishButton) {
                    submit();
                }
            }
        };
        cancelButton.setOnClickListener(popClick);
        finishButton.setOnClickListener(popClick);
    }

    void submit() {
        if (courseCommentModel.getUserName().toString().equals("") ||
                courseCommentModel.getExam().toString().equals("") ||
                courseCommentModel.getComment().toString().equals("")) {
            showInfo(getString(R.string.info_un_enough));
            tucao();
        } else {
            params.clear();
            params.add("userName", courseCommentModel.getUserName().toString());
            params.add("courseId", String.valueOf(CourseId));
            params.add("isCheck", String.valueOf(courseCommentModel.getIsCheck()));
            params.add("hasHomework", String.valueOf(courseCommentModel.getHasHomework()));
            params.add("exam", courseCommentModel.getExam().toString());
            params.add("comment", courseCommentModel.getComment().toString());
            showInfo(getString(R.string.submiting_comments));
            try {
                PostData(Paths.URL, params);
            } catch (Exception er) {
                er.printStackTrace();
            }

        }
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void PostData(String URL, MultiValueMap params) {
        PostHelper.PostCallBack callback = new PostHelper.PostCallBack() {
            @Override
            public void onSuccess() {
                showInfo(getString(R.string.comment_ok_show_later));
            }

            @Override
            public void onFail(int extraMsgId) {
                showInfo(getString(R.string.comment_failed));
            }
        };
        PostHelper.PostData(URL, params, callback);
    }

    @UiThread
    void showInfo(String info) {
        Toast.makeText(CourseComments.this, info, Toast.LENGTH_LONG).show();
    }

    @Click
    void up() {
        upDown(1);

    }

    @Click
    void down() {
        upDown(-1);
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void upDown(int flags) {
        paramsUpDown.clear();
        paramsUpDown.add("courseId", String.valueOf(CourseId));
        paramsUpDown.add("userName", app.userName);
        try {
            if (flags == 1) {

                PostHelper.PostData(Paths.LIKE_URL, paramsUpDown, voteBack);
            } else {

                PostHelper.PostData(Paths.DISLIKE_URL, paramsUpDown, voteBack);
            }

        } catch (HttpStatusCodeException e) {
            showResult(CourseComments.this, R.string.comments_json_fail);
        } catch (RestClientException e) {
            showResult(CourseComments.this, R.string.comments_fail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @AfterViews
    void init() {
        pullListView.setOnRefreshListener(onRefreshListener);
        courseModel = (CourseModel) courseBundle.getSerializable("CourseModel");
        CourseId = courseModel.getCourseId();
        setTitle(courseModel.getCourseName());
        t1.setText(courseModel.getTeacher());
        t2.setText(courseModel.getProperty());
        t3.setText(getString(R.string.listitem_lable_credit) + courseModel.getScore());
        up.setText(getString(R.string.up) + String.valueOf(courseModel.getLiked()));
        down.setText(getString(R.string.down) + String.valueOf(courseModel.getDisliked()));
        loadData(SumPage);
    }

    /**
     * listView下拉刷新;
     */
    private PullToRefreshBase.OnRefreshListener onRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            if (!endOfPage) {
                loadData(SumPage);
            } else {
                showInfo(getString(R.string.comments_nomore));
                pullListView.onRefreshComplete();
                pullListView.setRefreshing(false);
            }

        }
    };

    @UiThread
    void showSuccessResult(CourseCommentModel.CourseCommentList l) {
        pullListView.onRefreshComplete();

        if (commentsAdapter == null) {
            setListViewAdapter(l.getComments());
        } else {

            commentsAdapter.addAll(l.getComments());
            commentsAdapter.notifyDataSetChanged();

        }
    }


    private void setListViewAdapter(List<CourseCommentModel> comments) {
        commentsAdapter = new CourseCommentsAdapter(this, R.layout.coursel_listitem, comments);
        ListView lv = pullListView.getRefreshableView();
        lv.setAdapter(commentsAdapter);
        adapter = UIHelper.buildEffectAdapter(commentsAdapter, lv, SWING);
        pullListView.setAdapter(adapter);
    }

    @UiThread
    void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(int page) {
        try {
            courseCommentList = api.getComments(CourseId, page);
            if (courseCommentList.getStatus().equals("success")) {
                if (courseCommentList.getComments() != null
                        && courseCommentList.getComments().size() > 0) {
                    SumPage++;
                    showSuccessResult(courseCommentList);
                    hideProgress();
                } else {
                    endOfPage = true;
                    showInfo(getString(R.string.comments_nomore));
                }
                return;
            }

        } catch (HttpStatusCodeException e) {
            showErroResult(e.getStatusCode().value());

        }
        pullListView.onRefreshComplete();
    }

    @UiThread
    void showErroResult(int requestCode) {
        UIHelper.getDialog().dismiss();
        if (requestCode == 404) {
            showInfo(getString(R.string.tips_coursecomment_null));
        } else {
            app.showError(requestCode, this);
        }
    }


    @UiThread
    void setPullListView(boolean enabled) {
        pullListView.setEnabled(enabled);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);
        finish();
    }
}