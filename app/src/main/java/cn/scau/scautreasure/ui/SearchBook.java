package cn.scau.scautreasure.ui;


import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import android.widget.ListView;


import com.handmark.pulltorefresh.library.PullToRefreshBase;

import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.SearchBookAdapter;
import cn.scau.scautreasure.api.LibraryApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BookModel;
import cn.scau.scautreasure.util.CryptUtil;
import cn.scau.scautreasure.widget.AppProgress;
import cn.scau.scautreasure.widget.AppToast;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.EXPANDABLE_SWING;

@EActivity(R.layout.searchbook)
public class SearchBook extends ListActivity {

    @RestService
    LibraryApi api;


    /**
     * 一共搜索到多少本书
     */
    private int count;

    /**
     * 当前页数;
     */
    private int page;
    /**
     * listView下拉刷新;
     */
    private PullToRefreshBase.OnRefreshListener onRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            if (page < Math.ceil(count / 10.0)) { //修正没有最后一页 把10写成10.0，使到返回的是浮点数，这样ceil才有效
                page++;
                loadData();
            } else {
               /* refreshView.onRefreshComplete();
                AppMsg.makeText(getSherlockActivity(), R.string.tips_default_last, AppMsg.STYLE_CONFIRM).show();*/
                //为什么上面的写法不行，估计是那个下拉控件的官方bug，现在只能写一个假异步来取消那个loading
                onLastPage();

            }
        }
    };
    private String searchKeyword;
    private SearchBookAdapter bookadapter;
    private SlideExpandableListAdapter exadapter;

    /**
     * listView选中图书：
     */
    private AdapterView.OnItemClickListener onListViewItemClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
            BookModel book = (BookModel) parent.getAdapter().getItem(position);
            Log.i(getClass().getName(), book.toString());
            BookDetail_.intent(SearchBook.this).bookName(book.getTitle()).url(book.getUrl()).start();
        }
    };


    @AfterViews
    void init() {
        setTitleText("搜索图书");
        setMoreButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_layout.setVisibility(search_layout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                more.setText(search_layout.getVisibility() == View.VISIBLE ? "隐藏" : "显示");

                if (search_layout.getVisibility() == View.VISIBLE) {
                    edt_search_box.requestFocus();
                }
            }
        });
        tips_empty = "真的没有找到你想要的图书";
        pullListView.setOnRefreshListener(onRefreshListener);
        pullListView.setOnItemClickListener(onListViewItemClicked);

    }


    @UiThread
    void showSuccessResult(BookModel.BookList l) {
        hideProgressBar();
        // new search
        if (bookadapter == null) {
            more.setText("显示");
            more.setVisibility(View.VISIBLE);
            search_layout.setVisibility(View.GONE);
            count = l.getCount();
            setListViewAdapter(l.getBooks());
            String tips = getString(R.string.tips_searchbook_count) + count;
            AppToast.show(this, tips, 0);
        } else {
            // next page;
            bookadapter.addAll(l.getBooks());
            pullListView.onRefreshComplete();

            bookadapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }

    @UiThread
    void onFailed() {
        hideProgressBar();

    }

    private void setListViewAdapter(List<BookModel> books) {
        bookadapter = new SearchBookAdapter(this, R.layout.searchbook_listitem, books);
        ListView lv = pullListView.getRefreshableView();
        adapter = UIHelper.buildEffectAdapter(bookadapter, lv, EXPANDABLE_SWING);
        pullListView.setAdapter(adapter);
    }

    @UiThread
    void showProgressBar() {
        AppProgress.show(this, "正在搜索...", "", "取消", new AppProgress.Callback() {
            @Override
            public void onCancel() {
                BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);
                Log.i(getClass().getName(), "用户手动点击了取消");
            }
        });
    }

    @UiThread
    void hideProgressBar() {
        AppProgress.hide();
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {

        try {

            String serach_text = CryptUtil.base64_url_safe(searchKeyword);
            BookModel.BookList l = api.searchBook(serach_text, page);

            showSuccessResult(l);
            return;
        } catch (HttpStatusCodeException e) {
            showErrorResult(e.getStatusCode().value());

        } catch (Exception e) {
            handleNoNetWorkError();

        }
        onFailed();

    }


    @Background
    void onLastPage() {
        showLastPage();
    }

    @UiThread(delay = 500)
    void showLastPage() {
        pullListView.onRefreshComplete();
        AppToast.show(this, "已经是最后一页", 0);
    }

    @ViewById(R.id.edt_search_box)
    EditText edt_search_box;
    @ViewById(R.id.search_layout)
    View search_layout;

    @Click(R.id.search_go)
    void search_go() {
        searchKeyword = edt_search_box.getText().toString().trim();
        if (searchKeyword.equals("")) {
            AppToast.show(this, "你还没输入关键词", 0);
        } else {
            bookadapter = null;
            page = 1;

            showProgressBar();
            loadData();
        }
    }

}

