package cn.scau.scautreasure.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.api.LibraryApi;
import cn.scau.scautreasure.model.BookModel;
import cn.scau.scautreasure.ui.BorrowedBook_;

/**
 * 借阅的图书适配器;
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
@EBean
public class BorrowedBookAdapter extends QuickAdapter<BookModel> {

    @App
    AppContext app;

    @RestService
    LibraryApi api;

    private cn.scau.scautreasure.ui.BorrowedBook_ ctx;

    public BorrowedBookAdapter(Context context) {
        super(context, R.layout.borrowedbook_listitem);
    }

    public void setParent(Activity act) {
        ctx = (BorrowedBook_) act;
    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, final BookModel model) {
        baseAdapterHelper.setText(R.id.tv_bookname, model.getTitle())
                .setText(R.id.tv_borrow_date, model.getBorrow_date())
                .setText(R.id.tv_barcode_number, model.getBarcode_number())
                .setText(R.id.tv_collection_place, model.getCollection_place());

        if (isHistoryBrrowedBook(model)) {
            baseAdapterHelper.setText(R.id.tv_lable_return_date, ctx.getString(R.string.listitem_lable_return_date));
            baseAdapterHelper.setText(R.id.tv_return_date, model.getReturn_date());
        } else {
            baseAdapterHelper.setText(R.id.tv_lable_return_date, ctx.getString(R.string.listitem_lable_should_return_date));
            baseAdapterHelper.setText(R.id.tv_return_date, model.getShould_return_date());
        }

        if (canRenew(model)) {
            baseAdapterHelper.getView(R.id.btn_renew).setVisibility(View.VISIBLE);
            baseAdapterHelper.getView(R.id.btn_renew).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ctx.reNew(model.getBarcode_number(), model.getCheck_code());
                }
            });
        } else {
            baseAdapterHelper.getView(R.id.btn_renew).setVisibility(View.GONE);
        }
    }

    private boolean isHistoryBrrowedBook(BookModel model) {
        return model.getShould_return_date() == null;
    }

    private boolean canRenew(BookModel model) {
        return model.getRenew_time() == 0 && model.getCheck_code() != null;
    }
}
