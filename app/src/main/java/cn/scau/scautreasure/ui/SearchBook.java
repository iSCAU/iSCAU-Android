package cn.scau.scautreasure.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.SearchBookAdapter;
import cn.scau.scautreasure.api.LibraryApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BookModel;
import cn.scau.scautreasure.util.CryptUtil;
import com.devspark.appmsg.AppMsg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;
import java.util.List;
import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.*;

/**
 * User: special
 * Date: 13-8-18
 * Time: 下午9:27
 * Mail: specialcyci@gmail.com
 */
@EFragment( R.layout.searchbook )
public class SearchBook extends Common {

    @RestService
    LibraryApi api;

    @ViewById( R.id.listView )
    PullToRefreshListView pullListView;

    private Button   btn_search;
    private EditText edt_search;
    private ImageView iv_back;

    /**
     * 一共搜索到多少本书
     */
    private int count;

    /**
     * 当前页数;
     */
    private int page;
    private SearchBookAdapter bookadapter;
    private SlideExpandableListAdapter exadapter;


    @Override
    @AfterInject
    void initActionBar(){
        super.initActionBar();

        LayoutInflater inflator = (LayoutInflater) getSherlockActivity() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.searchbook_bar, null);

        getSherlockActivity().getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSherlockActivity().getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSherlockActivity().getSupportActionBar().setCustomView(v);
        getSherlockActivity().getSupportActionBar().setLogo(new ColorDrawable(0));
        getSherlockActivity().getSupportActionBar().setDisplayShowHomeEnabled(false);

        // find search button and edittext in actionBar;
        iv_back    = (ImageView)v.findViewById(R.id.iv_back);
        btn_search = (Button)   v.findViewById(R.id.btn_search);
        edt_search = (EditText) v.findViewById(R.id.edt_search);
    }

    @AfterViews
    void init(){

        tips_empty = R.string.tips_searchbook_null;
        btn_search.setOnClickListener(btnSeachOnClickListener);
        edt_search.setOnEditorActionListener(editorActionListener);
        iv_back.setOnClickListener(ivBackOnClickListener);
        pullListView.setOnRefreshListener(onRefreshListener);
        pullListView.setOnItemClickListener(onListViewItemClicked);
    }

    private View.OnClickListener btnSeachOnClickListener =  new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            btn_search(view);
        }
    };


    private View.OnClickListener ivBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Main_ parent = (Main_) getSherlockActivity();
            parent.home();
        }
    };

    private void btn_search(View view){
        if(view != null){
            bookadapter = null;
            page = 1;
            AppMsg.makeText(getSherlockActivity(), R.string.loading_seachbook, AppMsg.STYLE_INFO).show();
        }
        loadData(edt_search.getText().toString(),page);

    }

    /**
     * listView下拉刷新;
     */
    private PullToRefreshBase.OnRefreshListener onRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            if(page < Math.ceil(count/10)){
                page++;
                btn_search(null);
            }else{
                AppMsg.makeText(getSherlockActivity(), R.string.tips_default_last, AppMsg.STYLE_CONFIRM).show();
                refreshView.setRefreshing(false);
            }
        }
    };

    /**
     * listView选中图书：
     */
    private AdapterView.OnItemClickListener onListViewItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
            BookModel book = (BookModel) parent.getAdapter().getItem(position);
            BookDetail_.intent(getSherlockActivity()).bookName(book.getTitle()).url(book.getUrl()).start();
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

    @UiThread
    void showSuccessResult(BookModel.BookList l){

        // new search
        if(bookadapter == null){
            count = l.getCount();
            setListViewAdapter(l.getBooks());
            String tips = getString(R.string.tips_searchbook_count) + count;
            AppMsg.makeText(getSherlockActivity(),tips,AppMsg.STYLE_INFO).show();
        }else{
            // next page;
            bookadapter.addAll(l.getBooks());
            pullListView.onRefreshComplete();

            bookadapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }

    private void setListViewAdapter(List<BookModel> books){
        bookadapter = new SearchBookAdapter(getSherlockActivity(), R.layout.searchbook_listitem,books);
        ListView lv = pullListView.getRefreshableView();
        adapter     = UIHelper.buildEffectAdapter(bookadapter, lv, EXPANDABLE_SWING);
        pullListView.setAdapter(adapter);
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{

            String serach_text   = CryptUtil.base64_url_safe((String) params[0]);
            BookModel.BookList l = api.searchBook(serach_text,(Integer)params[1]);

            showSuccessResult(l);

        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }
    }

}