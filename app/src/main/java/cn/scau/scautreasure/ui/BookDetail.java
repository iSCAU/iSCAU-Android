package cn.scau.scautreasure.ui;

import android.content.DialogInterface;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.devspark.appmsg.AppMsg;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.BackgroundExecutor;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.BookDetailAdapter;
import cn.scau.scautreasure.api.LibraryApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BookDetailModel;
import cn.scau.scautreasure.util.CryptUtil;

import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.ALPHA;

/**
 * 搜书图书后，获取详细信息;
 * User: special
 * Date: 13-8-18
 * Time: 下午10:42
 * Mail: specialcyci@gmail.com
 */
@EActivity( R.layout.bookdetail)
public class BookDetail extends CommonActivity implements DialogInterface.OnCancelListener{

    @App         AppContext app;
    @RestService LibraryApi api;
    @Extra       String bookName;
    @Extra       String url;
    private BaseAdapter adapter;
    private List<BookDetailModel> list;

    @AfterInject
    void init(){
        getSupportActionBar().setTitle(bookName);
        UIHelper.getDialog(R.string.loading_bookdetail).show();
        loadData(url);
    }

    /**
     * 展示查询结果;
     */
    @UiThread
    void showSuccessResult(){
        UIHelper.getDialog().dismiss();
        ((ListView)listView).setAdapter(adapter);
    }

    /**
     * 展示http请求异常结果
     * @param requestCode
     */
    @UiThread
    void showErroResult(int requestCode){
        UIHelper.getDialog().dismiss();
        if(requestCode == 404){
            AppMsg.makeText(this, getString(R.string.tips_bookdetail_null), AppMsg.STYLE_CONFIRM).show();
        }else{
            app.showError(requestCode,this);
        }
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{
            String url = CryptUtil.base64_url_safe((String) params[0]);
            list = api.getBookDetail(url).getDetails();
            buildListViewAdapter();
            showSuccessResult();
        }catch (HttpStatusCodeException e){
            showErroResult(e.getStatusCode().value());
        }catch (Exception e){
            handleNoNetWorkError(getSherlockActivity());
        }
    }

    private void buildListViewAdapter(){
        BookDetailAdapter listadapter = new BookDetailAdapter(this, R.layout.bookdetail_listitem, list);
        adapter     = UIHelper.buildEffectAdapter(listadapter,(AbsListView) listView,ALPHA);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        BackgroundExecutor.cancelAll(UIHelper.CANCEL_FLAG, true);
        finish();
    }
}