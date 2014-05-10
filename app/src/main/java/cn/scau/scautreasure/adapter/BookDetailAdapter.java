package cn.scau.scautreasure.adapter;

import android.content.Context;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.BookDetailModel;

/**
 * 搜到的图书列表适配器
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class BookDetailAdapter extends QuickAdapter<BookDetailModel> {

    public BookDetailAdapter(Context context, int layoutResId, List<BookDetailModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, BookDetailModel model) {
        try {
            baseAdapterHelper.setText(R.id.tv_serial_number,model.getSerial_number())
                 .setText(R.id.tv_book_status, model.getBooks_status())
                 .setText(R.id.tv_barcode_number,model.getBarcode_number())
                 .setText(R.id.tv_collection_place,model.getCollection_place())
                 .setText(R.id.tv_year_title,model.getYear_title());
        }catch (Exception e){

        }

    }
}
