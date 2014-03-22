package cn.scau.scautreasure.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.SearchCourseAdapter;
import cn.scau.scautreasure.api.CourseApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.CourseModel;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;

/**
 * 2014-3-17
 */

@EFragment(R.layout.course)
public class SearchCourse extends Common {

    @RestService
    CourseApi api;
    private Button btn_search;
    private EditText edt_search;
    private ImageView iv_back;


    @Override
    @AfterInject
    void initActionBar() {
        super.initActionBar();

        LayoutInflater inflater = (LayoutInflater) getSherlockActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.searchcourse_bar, null);

        getSherlockActivity().getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSherlockActivity().getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSherlockActivity().getSupportActionBar().setCustomView(v);
        getSherlockActivity().getSupportActionBar().setLogo(new ColorDrawable(0));
        getSherlockActivity().getSupportActionBar().setDisplayShowHomeEnabled(false);

        // find search button and edittext in actionBar;
        iv_back = (ImageView) v.findViewById(R.id.iv_back);
        btn_search = (Button) v.findViewById(R.id.btn_search);
        edt_search = (EditText) v.findViewById(R.id.edt_search);
        edt_search.setHint(getString(R.string.enter_key_word));
    }

    private View.OnClickListener ivBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Main_ parent = (Main_) getSherlockActivity();
            parent.home();
        }
    };

    void btn_search(View view) {
        if (view != null) {
            if (!edt_search.getText().toString().trim().equals("")) {
                AppMsg.makeText(getSherlockActivity(), R.string.loading_seachcourse, AppMsg.STYLE_CONFIRM).show();
                loadData(edt_search.getText().toString().trim());
            } else {
                AppMsg.makeText(getSherlockActivity(), R.string.no_text, AppMsg.STYLE_CONFIRM).show();
            }
        }
    }

    @AfterViews
    void init() {
        iv_back.setOnClickListener(ivBackOnClickListener);
        edt_search.setOnEditorActionListener(editorActionListener);
        ((ListView) listView).setOnItemClickListener(onListViewItemClicked);
        btn_search.setOnClickListener(btnSeachOnClickListener);
    }

    /*
     *搜索按钮点击
     */
    private View.OnClickListener btnSeachOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            btn_search(view);
        }
    };
    /**
     * listView选中：
     */
    private AdapterView.OnItemClickListener onListViewItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            CourseModel courseModel = (CourseModel) list.get(position);
            Bundle b = new Bundle();
            b.putSerializable("CourseModel", courseModel);
            CourseComments_.intent(getSherlockActivity()).courseBundle(b).start();
        }
    };

    /**
     * 监听搜索输入框的回车事件;
     */
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            app.Log(actionId);
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                    keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                btn_search(textView);
            }
            return true;
        }
    };

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        try {
            list = api.searchCourse((String) params[0]).getCourses();
            showSuccessResult(list);
        } catch (HttpStatusCodeException e) {
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }
    }

    @UiThread
    void showSuccessResult(List<CourseModel> courses) {
        SearchCourseAdapter courseadapter = new SearchCourseAdapter(getSherlockActivity(),
                R.layout.searchcourse_listitem, courses);
        adapter = UIHelper.buildEffectAdapter(courseadapter, (ListView) listView, ALPHA);
        ((ListView) listView).setAdapter(adapter);
    }

}