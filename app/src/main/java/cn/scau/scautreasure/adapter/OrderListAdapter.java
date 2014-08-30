package cn.scau.scautreasure.adapter;

import android.content.Context;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import org.androidannotations.annotations.EBean;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.FoodShopDBModel;
import cn.scau.scautreasure.model.ShopMenuDBModel;

/**
 * Created by apple on 14-8-27.
 */

public class OrderListAdapter extends QuickAdapter<ShopMenuDBModel> {
    public OrderListAdapter(Context context, int layoutResId, List<ShopMenuDBModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, ShopMenuDBModel item) {
        helper.setText(R.id.foodName,item.getFood_name());
        helper.setText(R.id.foodCount, String.valueOf(item.getCount())+" 份");
        helper.setText(R.id.foodMoney,"¥ "+String.valueOf(item.getFood_price()*item.getCount()));

    }
}
