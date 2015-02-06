package cn.scau.scautreasure.ui;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;

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
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
//import cn.scau.scautreasure.adapter.BookDetailAdapter;
import cn.scau.scautreasure.adapter.BookDetailAdapter;
import cn.scau.scautreasure.api.LibraryApi;
import cn.scau.scautreasure.helper.UIHelper;

import cn.scau.scautreasure.util.CryptUtil;
import cn.scau.scautreasure.widget.AppProgress;
import cn.scau.scautreasure.widget.AppToast;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;

@EActivity(R.layout.bookdetail)
public class BookDetail extends ListActivity {


    @RestService
    LibraryApi api;

    @Extra
    String bookName;
    protected ArrayList list;

    @Extra
    String url;

    @ViewById(R.id.book_name)
    TextView book_name;

    @Override
    void doMoreButtonAction() {
        super.doMoreButtonAction();
        BaseBrowser_.intent(this).allCache("0").browser_title(bookName).
                url("http://iscaucms.sinaapp.com/apps/webapp/_book_by_url.php?url=" + 
                        Base64.encodeToString(("http://202.116.174.108:8080/opac/" + url).getBytes(), Base64.DEFAULT)).
                start();
    }

    @AfterViews
    void init() {
        book_name.setText(bookName);
        setTitleText("借阅情况");
        setMoreButtonText("详细信息");
        AppProgress.show(this, "正在加载...", "", "取消", new AppProgress.Callback() {
            @Override
            public void onCancel() {
                BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);
                finish();
            }
        });
        loadData(url);
    }

    /**
     * 展示查询结果;
     */
    @UiThread
    void showSuccessResult() {
        AppProgress.hide();
        ((ListView) listView).setAdapter(adapter);
    }

    /**
     * 展示http请求异常结果
     *
     * @param requestCode
     */
    @UiThread
    void showErroResult(int requestCode) {
        AppProgress.hide();
        if (requestCode == 404) {
            AppToast.info(this, "没有找到你想要的书的馆藏信息");
        } else {
            app.showError(requestCode, this);
        }
    }

    @Background(id = UIHelper.CANCEL_FLAG)
    void loadData(Object... params) {
        try {
            String url = CryptUtil.base64_url_safe((String) params[0]);
            list = api.getBookDetail(url).getDetails();
            buildListViewAdapter();
            showSuccessResult();
        } catch (HttpStatusCodeException e) {
            showErroResult(e.getStatusCode().value());
        } catch (Exception e) {
            handleNoNetWorkError();
        }
    }

    void handleNoNetWorkError() {
        AppProgress.hide();


    }

    private void buildListViewAdapter() {
        BookDetailAdapter listadapter = new BookDetailAdapter(this, R.layout.bookdetail_listitem, list);
        adapter = UIHelper.buildEffectAdapter(listadapter, (AbsListView) listView, ALPHA);
    }


}