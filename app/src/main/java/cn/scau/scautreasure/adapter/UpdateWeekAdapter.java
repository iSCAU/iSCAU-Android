package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.NoticeModel;

/**
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class UpdateWeekAdapter extends QuickAdapter<Object> {

    public UpdateWeekAdapter(Context context, int layoutResId, List<Object> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(final BaseAdapterHelper baseAdapterHelper, Object model) {
        baseAdapterHelper.setText(R.id.text1,model.toString());
//        baseAdapterHelper.setText(R.id.notice_title, model.getTitle())
//                .setText(R.id.notice_time, model.getTime());

    }

}
