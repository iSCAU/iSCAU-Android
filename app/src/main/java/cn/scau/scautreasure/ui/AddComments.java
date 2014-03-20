package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.PostHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.CourseCommentModel;

/**
 * Created by stcdasqy on 14-3-13.
 * If user hasn't post successfully, it may be saved in
 * SharedPref with the way of 'Base64 encode'.
 */
@EActivity(R.layout.addcomments)
public class AddComments extends InjectedSherlockActivity {
    /*@RestService
    CourseApiPost api;*/
    @App
    AppContext app;
    @Pref
    cn.scau.scautreasure.AppConfig_ config;

    private int CourseId;
    private Activity mActivity;
    private CourseCommentModel courseComment;
    private static final String URL="http://76.191.112.146/course_comments/index.php?s=Api/addComment";

    @ViewById
    EditText userName,exam,comments_content;
    @ViewById
    ToggleButton ischeck;
    @ViewById
    RadioButton hashomework;

    private MultiValueMap params=new LinkedMultiValueMap<String,String>();
    @AfterViews
    void init(){
        mActivity=this;
        CourseId = getIntent().getIntExtra("CourseId",1);
        userName.setText(app.userName);
        //userName.setClickable(false);
        readSharedPref();
        if(courseComment != null){
            if(courseComment.getCourseId() == CourseId) {
                config.courseComment().put("");
                applyCourseComment();
            }
        }else{
            courseComment = new CourseCommentModel();
        }
    }

    @Click
    void submit(){
        if(userName.getText().toString().equals("")||
           exam.getText().toString().equals("")||
           comments_content.getText().toString().equals(""))
        {
            AppMsg.makeText(this, R.string.comments_Incomplete_information, AppMsg.STYLE_CONFIRM).show();
            return;
        }
        params.clear();
        params.add("userName", app.userName);
        params.add("courseId", String.valueOf(CourseId));
        params.add("isCheck", String.valueOf(ischeck.isChecked() ? 1 : 0));
        params.add("hasHomework",String.valueOf(hashomework.isChecked() ? 1 : 0));
        params.add("exam", exam.getText().toString());
        params.add("comment", comments_content.getText().toString());
        Log.e("params.", params.toString());
        //UIHelper.getDialog(R.string.comments_posting).show();
        UIHelper.buildLoadingDialog(mActivity, R.string.comments_posting, null).show();
        //AppMsg appMsg = AppMsg.makeText(mActivity, R.string.comments_posting, AppMsg.STYLE_INFO);
        //appMsg.setDuration(10000);
        //appMsg.show();
        PostData(URL, params);
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void PostData(String URL, MultiValueMap params){
        PostHelper.PostCallBack callback = new PostHelper.PostCallBack() {
            @Override
            public void onSuccess() {
                showSuccessResult();
            }

            @Override
            public void onFail(int extraMsgId) {
                showErrorResult();
            }
        };
        PostHelper.PostData(URL, params, callback);
    }
    @UiThread
    void showSuccessResult(){
        //AppMsg.makeText(mActivity,R.string.comments_success,AppMsg.STYLE_INFO).show();
        Toast.makeText(mActivity,R.string.comments_success,Toast.LENGTH_LONG).show();
        Intent result=new Intent();
        result.putExtra("CourseCommentModel", courseComment);
        setResult(Activity.RESULT_OK, result);

        //prevent leaked window
        UIHelper.getDialog().cancel();
        finish();
    }
    @UiThread
    void showErrorResult(){
        AppMsg.makeText(mActivity,R.string.comments_fail,AppMsg.STYLE_INFO).show();
        //ApplySharedPref();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            //alertDialog.setIcon(R.drawable.icon).setTitle("友情提示...").setMessage("你确定要离开吗？");
            alertDialog.setMessage(R.string.comments_askForSave);
            alertDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveCourseComment();
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            });
            alertDialog.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create();
            alertDialog.show();
        }
        return true;
    }
    private void saveCourseComment(){
        if(comments_content.getText().toString().equals("") &&
                exam.getText().toString().equals("")) return;
        courseComment.setCourseId(CourseId);
        courseComment.setComment(comments_content.getText().toString());
        courseComment.setHasHomework(hashomework.isChecked());
        courseComment.setIsCheck(ischeck.isChecked());
        courseComment.setUserName(userName.getText().toString());
        courseComment.setExam(exam.getText().toString());

        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        courseComment.setTime(sf.format(date));

        ApplySharedPref();
    }
    private void applyCourseComment(){
        comments_content.setText(courseComment.getComment());
        hashomework.setChecked(courseComment.getHasHomework());
        ischeck.setChecked(courseComment.getIsCheck());
        userName.setText(courseComment.getUserName());
        exam.setText(courseComment.getExam());
    }
    private void ApplySharedPref() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(courseComment);
            String courseComment_Base64 = new String(Base64.encode(byteArrayOutputStream
                    .toByteArray(), Base64.DEFAULT));
            config.courseComment().put(courseComment_Base64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("ok", "Course Comment Saved");
    }
    private void readSharedPref() {
        String courseComment_Base64 = config.courseComment().get();
        byte[] base64 = Base64.decode(courseComment_Base64.getBytes(),
                Base64.DEFAULT);

        ByteArrayInputStream byteStream = new ByteArrayInputStream(base64);
        try {
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            try {
                courseComment = (CourseCommentModel) objectStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

    /*
    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(MultiValueMap params) {
        try{

            StatusModel m = api.addComment(params);
            if(m==null || m.getResult()!="success"){
                AppMsg.makeText(mActivity, R.string.comments_fail, AppMsg.STYLE_CONFIRM).show();
            }

        }catch (HttpStatusCodeException e){
            AppMsg.makeText(mActivity, e.getStatusCode().value(),AppMsg.STYLE_CONFIRM).show();
        }catch(RestClientException e){

        }
    }*/