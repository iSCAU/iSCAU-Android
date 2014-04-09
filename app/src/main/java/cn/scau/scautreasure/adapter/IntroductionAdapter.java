package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.BookDetailModel;
import cn.scau.scautreasure.model.IntroductionModel;
import cn.scau.scautreasure.util.TextUtil;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

/**
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class IntroductionAdapter extends QuickAdapter<IntroductionModel> {

    private TextUtil textUtil;

    public IntroductionAdapter(Context context, int layoutResId, List<IntroductionModel> data) {
        super(context, layoutResId, data);
        textUtil = new TextUtil();
    }

    @Override
    protected void convert(final BaseAdapterHelper baseAdapterHelper, IntroductionModel model) {
        baseAdapterHelper.setText(R.id.intro_title,model.getTitle())
                         .setText(R.id.intro_content,textUtil.toDBC(model.getContent()));

        setLeftImageViewColor(baseAdapterHelper);

        baseAdapterHelper.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() != R.id.expandable_toggle_button ){
                    baseAdapterHelper.getView(R.id.expandable_toggle_button).performClick();
                }
            }
        });
    }

    private void setLeftImageViewColor(BaseAdapterHelper baseAdapterHelper){
        int color    = AppConstant.IV_COLOR[ baseAdapterHelper.getPosition() % AppConstant.IV_COLOR.length ];
        ImageView iv = baseAdapterHelper.getView(R.id.intro_iv);
        iv.setImageDrawable(new ColorDrawable(color));
    }
}
