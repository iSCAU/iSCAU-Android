package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.view.View;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.CardRecordModel;
import cn.scau.scautreasure.model.GoalModel;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

/**
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
public class CardRecordAdapter extends QuickAdapter<CardRecordModel> {


    private final Context context;

    public CardRecordAdapter(Context context, int layoutResId, List<CardRecordModel> data) {
        super(context, layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseAdapterHelper baseAdapterHelper, CardRecordModel model) {
        baseAdapterHelper.setText(R.id.tv_amount,model.getAmount())
                         .setText(R.id.tv_time, model.getTime())
                         .setText(R.id.tv_place, model.getPlace())
                         .setText(R.id.tv_type, model.getType())
                         .setText(R.id.tv_balance,model.getBalance())
                         .setText(R.id.tv_frequency,model.getFrequency())
                         .setText(R.id.tv_status,model.getStatus());


        // Transmit the view click to expand button
        baseAdapterHelper.getView().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() != R.id.expandable_toggle_button){
                    baseAdapterHelper.getView(R.id.expandable_toggle_button).performClick();
                }
            }
        });

    }
}
