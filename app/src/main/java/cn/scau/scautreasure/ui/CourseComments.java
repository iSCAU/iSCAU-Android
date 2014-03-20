package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
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
//import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.*;

@EActivity( R.layout.coursecomments )
public class CourseComments extends InjectedSherlockActivity{
    @RestService
    CourseApi api;
   // @RestService CourseApiPost apiPost;

    @App
    AppContext app;

    @ViewById( R.id.listView )
    PullToRefreshListView pullListView;
    @ViewById
    TextView course_property,course_score,like_number,
            dislike_number,comment_number,noComments;

    private static final int REQUEST_CODE = 0x0309;
    private static final String ROOT_URL = "http://76.191.112.146/course_comments/index.php?s=Api/";
    private static final String LIKE_URL = ROOT_URL + "likedCourse";
    private static final String DISLIKE_URL = ROOT_URL + "dislikedCourse";
    private PostHelper.PostCallBack voteBack = new PostHelper.PostCallBack() {
        public void onSuccess() {
            showVoteResult();
            waitForVoteResult=false;
        }
        
        public void onFail(int extraMsgId) {
            showResult(mActivity, extraMsgId);
            waitForVoteResult=false;
            hasVotePoint=false;
        }
    };

    @Click
    void ToComment(){
        Intent v=new Intent();
        v.putExtra("courseId",CourseId);
        v.setClass(this,AddComments_.class);
        startActivityForResult(v, REQUEST_CODE);
    }

    @Click({R.id.like_number,R.id.dislike_number})
    void course_like(View v){
        if(waitForVoteResult) {
            AppMsg.makeText(mActivity,R.string.comments_waitForVote,AppMsg.STYLE_INFO).show();
            return;
        }
        waitForVoteResult=true;
        if(hasVotePoint){
            AppMsg.makeText(this, R.string.comments_hasVotePoint, AppMsg.STYLE_CONFIRM).show();
            return;
        }
        hasVotePoint=true;
        if(v.getId()==R.id.like_number) {
            isVotelike=true;
            vote(GOOD_POINT);
        }
        else {
            isVotelike=false;
            vote(BAD_POINT);
        }
    }
    private static final int GOOD_POINT=0x01;
    private static final int BAD_POINT=0x02;

    private int CourseId;
    private int SumPage=1;
    private Activity mActivity;
    private CourseModel courseModel;
    private boolean endOfPage=false;

    private boolean isVotelike=false;
    private boolean hasVotePoint=false;
    private boolean waitForVoteResult=false;

    private CourseCommentModel.CourseCommentList courseCommentList;
    private CourseCommentsAdapter commentsAdapter;
    private MultiValueMap params=new LinkedMultiValueMap<String,String>();
    private BaseAdapter adapter;

   // private SlideExpandableListAdapter exadapter;

    @Override
    @AfterInject
    void initActionBar(){
        super.initActionBar();
        mActivity = this;
    }

    @AfterViews
    void init(){
        pullListView.setOnRefreshListener(onRefreshListener);
        //pullListView.setSelected(false);
        //pullListView.setClickable(false);
        //pullListView.setOnItemClickListener(onListViewItemClicked);
        courseModel = (CourseModel)getIntent().getSerializableExtra("CourseModel");
        CourseId=courseModel.getCourseId();
        setTitle(courseModel.getCourseName()+
                "("+ courseModel.getTeacher() +")");
        course_property.setText(courseModel.getProperty());
        course_score.setText(courseModel.getScore());
        like_number.setText(String.valueOf(courseModel.getLiked()));
        dislike_number.setText(String.valueOf(courseModel.getDisliked()));
        comment_number.setText("");
        AppMsg.makeText(mActivity, R.string.comments_loading, AppMsg.STYLE_INFO);
        //pullListView.setEnabled(false);
        loadData(SumPage);
    }

    /**
     * listView下拉刷新;
     */
    private PullToRefreshBase.OnRefreshListener onRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            if(!endOfPage){
                loadData(SumPage);
            }else{
                showFailResult(R.string.comments_nomore);
                //AppMsg.makeText(mActivity, R.string.tips_default_last, AppMsg.STYLE_CONFIRM).show();
                //refreshView.setRefreshing(false);
                //stopRefresh();
            }
        }
    };
    @OnActivityResult(REQUEST_CODE)
    void updateUserComments(int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            CourseCommentModel ccm = (CourseCommentModel)
                    intent.getSerializableExtra("CourseCommentModel");
            //ListView lv = pullListView.getRefreshableView();
            //LinearLayout v = (LinearLayout)lv.getChildAt(lv.getFirstVisiblePosition()+1);
            List<CourseCommentModel> comments = courseCommentList.getComments();
            if(ccm == null){
                try {
                    throw new Exception("ccm is null");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            comments.add(0, ccm);
            setListViewAdapter(comments);
        /*
        v.findViewById(R.id.content).setBackgroundColor(Color.GREEN);
        ((TextView)v.findViewById(R.id.extra_info)).setText(data.getString("extra_info"));
        ((TextView)v.findViewById(R.id.content)).setText(data.getString("content"));
        ((TextView)v.findViewById(R.id.userInfo)).setText(data.getString("user_info"));
        */
        }
    }

    /**
     * listView选中：
    private AdapterView.OnItemClickListener onListViewItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //do nothing;
        }
    };
     */

    @UiThread
    void showSuccessResult(CourseCommentModel.CourseCommentList l){
        pullListView.onRefreshComplete();
        // new search
        if(commentsAdapter == null){
            setListViewAdapter(l.getComments());
        }else{
            // next page;
                commentsAdapter.addAll(l.getComments());
                commentsAdapter.notifyDataSetChanged();
                //myAdapter.updateData();
                //adapter.notifyDataSetChanged();
        }
    }
    @UiThread
    void showFailResult(int resourceId){
        pullListView.onRefreshComplete();
        AppMsg.cancelAll();
        AppMsg.makeText(this,resourceId,AppMsg.STYLE_INFO).show();
        //Toast.makeText(this,resourceId,Toast.LENGTH_SHORT).show();
        //Log.e("showFailResult", "in this function");
    }

    private void setListViewAdapter(List<CourseCommentModel> comments){
        commentsAdapter = new CourseCommentsAdapter(this, R.layout.commentsdetail,comments);
        ListView lv = pullListView.getRefreshableView();
        lv.setAdapter(commentsAdapter);
        lv.setSelector(R.drawable.comment_list_selector);
        adapter     = UIHelper.buildEffectAdapter(commentsAdapter, lv, SWING);
        //myAdapter.set(commentsAdapter, lv);
        //pullListView.setAdapter(myAdapter.updateAdapter());
        pullListView.setAdapter(adapter);
    }
    @UiThread
    void setNoComments(){
        noComments.setVisibility(View.INVISIBLE);
    }

    @Background( id = UIHelper.CANCEL_FLAG ,delay=2000)
    void loadData(int Page) {
        try{
            courseCommentList = api.getComments(CourseId,Page);
            if(courseCommentList.getStatus().equals("success")) {
                if (courseCommentList.getComments() != null
                        && courseCommentList.getComments().size() > 0) {

                    //Log.e("Comments_size", String.valueOf(l.getComments().size()));
                    SumPage++;
                    showSuccessResult(courseCommentList);
                    setNoComments();
                    updateCommentNumber(courseCommentList.getCount());
                } else {
                    endOfPage = true;
                    showFailResult(R.string.comments_nomore);
                }
                return;
            }

        }catch (HttpStatusCodeException e){
            //showResult(this, e.getStatusCode().value());
            e.printStackTrace();
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        showFailResult(R.string.comments_fail);
    }
    @Background( id = UIHelper.CANCEL_FLAG )
    void vote(int Point) {
        params.clear();
        params.add("courseId", String.valueOf(CourseId));
        params.add("userName", app.userName);
        try{
            switch(Point){
                case GOOD_POINT:
                    PostHelper.PostData(LIKE_URL, params, voteBack);break;
                case BAD_POINT:
                    PostHelper.PostData(DISLIKE_URL, params, voteBack);break;
            }
        }catch (HttpStatusCodeException e){
            showResult(mActivity,R.string.comments_json_fail);
        }catch(RestClientException e){
            showResult(mActivity,R.string.comments_fail);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @UiThread
    void showResult(Activity ctx, int extraMsgId) {
        if (ctx == null) return;
        //UIHelper.getDialog().dismiss();
        AppMsg.makeText(ctx, extraMsgId, AppMsg.STYLE_INFO).show();
    }
    @UiThread
    void showVoteResult(){
        TextView v;
        if(isVotelike) v=like_number;
        else v=dislike_number;
        int oldNumber = Integer.valueOf(v.getText().toString());
        v.setText("" + (oldNumber + 1));
        v.setTextColor(Color.RED);
        showResult(mActivity,R.string.comments_vote_success);
    }
    @UiThread
    void updateCommentNumber(int number){
        comment_number.setText(String.valueOf(number));
    }
    @UiThread
    void setPullListView(boolean enabled){
        pullListView.setEnabled(enabled);
    }

}