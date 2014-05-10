package cn.scau.scautreasure.ui;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devspark.appmsg.AppMsg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.SearchBookAdapter;
import cn.scau.scautreasure.api.LibraryApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BookModel;
import cn.scau.scautreasure.util.CryptUtil;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_SWING;

/**
 * User: special
 * Date: 13-8-18
 * Time: 下午9:27
 * Mail: specialcyci@gmail.com
 */
@EActivity( R.layout.searchbook )
public class SearchBook extends CommonActivity {

    @RestService
    LibraryApi api;

    @ViewById( R.id.listView )
    PullToRefreshListView pullListView;

    /**
     * 一共搜索到多少本书
     */
    private int count;

    /**
     * 当前页数;
     */
    private int page;
    private String searchKeyword;
    private SearchBookAdapter bookadapter;
    private SlideExpandableListAdapter exadapter;
    private SearchView searchView;
    private MenuItem menuItemSearch;


    @AfterViews
    void init(){

        setTitle(getString(R.string.title_search_book));
        tips_empty = R.string.tips_searchbook_null;
        pullListView.setOnRefreshListener(onRefreshListener);
        pullListView.setOnItemClickListener(onListViewItemClicked);
    }

    /**
     * listView下拉刷新;
     */
    private PullToRefreshBase.OnRefreshListener onRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            if(page < Math.ceil(count/10)){
                page++;
                loadData();
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

            String serach_text   = CryptUtil.base64_url_safe(searchKeyword );
            BookModel.BookList l = api.searchBook(serach_text, page);

            showSuccessResult(l);

        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }catch (Exception e){
            handleNoNetWorkError(getSherlockActivity());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchbook, menu);
        menuItemSearch = menu.findItem(R.id.search_button);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItemSearch);
        searchView.setQueryHint(getString(R.string.hint_searchbook));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(oQueryTextListener);
        MenuItemCompat.expandActionView(menuItemSearch);
        return true;
    }

    SearchView.OnQueryTextListener oQueryTextListener = new SearchView.OnQueryTextListener(){

        @Override
        public boolean onQueryTextSubmit(String s) {
            bookadapter = null;
            page = 1;
            searchKeyword = s.trim();
            searchView.clearFocus();
            MenuItemCompat.collapseActionView(menuItemSearch);

            AppMsg.makeText(getSherlockActivity(), R.string.loading_seachbook, AppMsg.STYLE_INFO).show();
            loadData();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };
}