package cn.scau.scautreasure.adapter;

import android.content.Context;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.BookModel;

/**
 * 搜到的图书列表适配器
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class SearchBookAdapter extends QuickAdapter<BookModel> {

    public SearchBookAdapter(Context context, int layoutResId, List<BookModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, BookModel model) {
        baseAdapterHelper.setText(R.id.tv_bookname, model.getTitle())
                .setText(R.id.tv_author, model.getAuthor())
                .setText(R.id.tv_press, model.getPress())
                .setText(R.id.tv_serial_number, model.getSerial_number())
                .setText(R.id.tv_document_type, model.getDocument_type())
                .setText(R.id.tv_all_bookname, model.getTitle());
    }
}
