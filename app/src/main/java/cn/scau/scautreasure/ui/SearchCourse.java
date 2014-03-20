package cn.scau.scautreasure.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.SearchCourseAdapter;
import cn.scau.scautreasure.api.CourseApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.CourseModel;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;


@EFragment( R.layout.searchcourse )
public class SearchCourse extends Common {

    @RestService
    CourseApi api;

    private Button   btn_search;
    private EditText edt_search;
    private ImageView iv_back;
    private FrameLayout fra_search;
    private LinearLayout lin_back;

    @Override
    @AfterInject
    void initActionBar(){
        super.initActionBar();

        LayoutInflater inflater = (LayoutInflater) getSherlockActivity() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.searchcourse_bar, null);

        getSherlockActivity().getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSherlockActivity().getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSherlockActivity().getSupportActionBar().setCustomView(v);
        getSherlockActivity().getSupportActionBar().setLogo(new ColorDrawable(0));
        getSherlockActivity().getSupportActionBar().setDisplayShowHomeEnabled(false);

        // find search button and edittext in actionBar;
        iv_back    = (ImageView)v.findViewById(R.id.iv_back);
        btn_search = (Button)   v.findViewById(R.id.btn_search);
        edt_search = (EditText) v.findViewById(R.id.edt_search);
        fra_search = (FrameLayout) v.findViewById(R.id.fra_search);
        lin_back =(LinearLayout) v.findViewById(R.id.lin_back);
    }


    void iv_back(){
        Main_ parent = (Main_) getSherlockActivity();
        parent.home();
    }

    void btn_search(View view){
        if(view != null){
            //adapter = null;
            AppMsg appMsg = AppMsg.makeText(getSherlockActivity(), R.string.loading_seachcourse, AppMsg.STYLE_CONFIRM);
            appMsg.setDuration(10000);
            appMsg.show();
        }
        loadData(edt_search.getText().toString());
    }

    @AfterViews
    void init(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_back();
            }
        });
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_back();
            }
        });
        edt_search.setOnEditorActionListener(editorActionListener);
        ((AbsListView)listView).setOnItemClickListener(onListViewItemClicked);
        fra_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_search(v);
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_search(v);
            }
        });
        //showInputMethod(getSherlockActivity(),edt_search);
        edt_search.requestFocus();

    }

   /**
     * listView选中：
     */
    private AdapterView.OnItemClickListener onListViewItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppMsg.makeText(getSherlockActivity(), R.string.comments_loading, AppMsg.STYLE_INFO).show();
        //CourseModel l=((CourseModel.CourseList)list).getCourses().get(position);
        CourseModel l=(CourseModel)list.get(position);
        Bundle b=new Bundle();
        b.putSerializable("CourseModel",l);
        Intent v=new Intent();
        v.putExtras(b);
        v.setClass(getActivity(),CourseComments_.class);
        startActivity(v);
        }
    };

    /**
     * 监听搜索输入框的回车事件;
     */
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            app.Log(actionId);
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                btn_search(textView);
            }
            return true;
        }
    };


    private void buildListViewAdapter(List<CourseModel> courses){
        SearchCourseAdapter courseadapter = new SearchCourseAdapter(getSherlockActivity(),
                                        R.layout.searchcourse_listitem,courses);
        adapter = UIHelper.buildEffectAdapter(courseadapter, (AbsListView) listView,ALPHA);
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{
            list = api.searchCourse((String)params[0]).getCourses();
            buildListViewAdapter(list);
            showSuccessResult();

        }catch (HttpStatusCodeException e){
            AppMsg.makeText(getSherlockActivity(),R.string.comments_json_fail,AppMsg.STYLE_CONFIRM).show();
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }catch(RestClientException e){
            AppMsg.makeText(getSherlockActivity(),R.string.comments_fail,AppMsg.STYLE_CONFIRM).show();
        }finally {
            AppMsgCancel();
        }
    }

    @UiThread
    void AppMsgCancel(){
        AppMsg.cancelAll();
    }

    public static void showInputMethod(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }

}