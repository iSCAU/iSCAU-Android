package cn.scau.scautreasure.ui;

import android.widget.AbsListView;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.adapter.BorrowedBookAdapter_;
import cn.scau.scautreasure.api.LibraryApi;
import cn.scau.scautreasure.helper.UIHelper;
import com.devspark.appmsg.AppMsg;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;
import static cn.scau.scautreasure.helper.UIHelper.LISTVIEW_EFFECT_MODE.*;

/**
 * 当前借阅书籍记录及过去借阅记录
 * User: special
 * Date: 13-8-20
 * Time: 上午11:36
 * Mail: specialcyci@gmail.com
 */
@EFragment ( R.layout.borrowedbook )
public class BorrowedBook extends Common {

    @RestService
    LibraryApi api;

    int target;

    @AfterInject
    void init(){
        setTitle(R.string.title_borrowedbook);
        setDataEmptyTips(R.string.tips_borrowedbook_null);
        getTarget();
        UIHelper.getDialog(R.string.loading_borrowedbook).show();
        loadData();
    }

    private void getTarget(){
        target = getArguments().getInt("target");
    }

    @UiThread
    void showReNewResult(){
        AppMsg.makeText(getSherlockActivity(), R.string.tips_renew_success, AppMsg.STYLE_INFO).show();
    }

    /**
     * 续借网络请求;
     * @param barCode
     * @param checkCode
     */
    @Background
    public void reNew(String barCode,String checkCode){

        try{
            api.reNewBook( app.userName, app.getEncodeLibPassword(), barCode, checkCode );
            showReNewResult();
        }catch (HttpStatusCodeException e){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }

    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {

        try{
            if ( target == UIHelper.TARGET_FOR_NOW_BORROW ) {
                list = api.getNowBorrowedBooks(AppContext.userName, app.getEncodeLibPassword()).getBooks();
            }else{
                list = api.getHistoryBorrowedBooks(AppContext.userName,app.getEncodeLibPassword()).getBooks();
            }
            buildListViewAdapter();
            showSuccessResult();
        }catch ( HttpStatusCodeException e ){
            showErrorResult(getSherlockActivity(), e.getStatusCode().value());
        }
    }

    private void buildListViewAdapter(){
        BorrowedBookAdapter_ bookadapter = BorrowedBookAdapter_.getInstance_(getSherlockActivity());
        bookadapter.setParent(this);
        bookadapter.addAll(list);
        adapter     = UIHelper.buildEffectAdapter(bookadapter, (AbsListView) listView,ALPHA);
    }
}