package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BookDetailModel;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.model.IntroductionModel;
import cn.scau.scautreasure.model.NoticeModel;
import cn.scau.scautreasure.util.TextUtil;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class NoticeAdapter extends QuickAdapter<NoticeModel> {

    public NoticeAdapter(Context context, int layoutResId, List<NoticeModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(final BaseAdapterHelper baseAdapterHelper, NoticeModel model) {
        baseAdapterHelper.setText(R.id.notice_title,model.getTitle())
                         .setText(R.id.notice_time, model.getTime());

        setLeftImageViewColor(baseAdapterHelper);
    }

    private void setLeftImageViewColor(BaseAdapterHelper baseAdapterHelper){
        int color    = AppConstant.IV_COLOR[baseAdapterHelper.getPosition() % AppConstant.IV_COLOR.length ];
        ImageView iv = baseAdapterHelper.getView(R.id.notice_iv);
        iv.setImageDrawable(new ColorDrawable(color));
    }
}
