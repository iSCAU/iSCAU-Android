package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

import cn.scau.scautreasure.AppConfig;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.FoodShopDBModel;
import cn.scau.scautreasure.model.FoodShopModel;
import cn.scau.scautreasure.ui.Calendar;

/**
 * Created by apple on 14-8-24.
 */
public class FoodShopAdapter extends QuickAdapter<FoodShopDBModel> {

    private Context context;
    public FoodShopAdapter(Context context, int layoutResId, List<FoodShopDBModel> data) {
        super(context, layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, FoodShopDBModel item) {
        helper.setText(R.id.text1, item.getShop_name());
        checkTime(item.getStart_time(),item.getEnd_time(),helper);
        setLogo(helper,item.getLogo_url());
    }
    private void setLogo(BaseAdapterHelper helper,String url){
        ImageView imageView=(ImageView)helper.getView(R.id.shopLogo);
        AppContext.loadImage(url,imageView,null);

    }
    private void checkTime(String start,String end,BaseAdapterHelper helper){
        String startTime[]=start.split(":");
        String endTime[]=end.split(":");
        int startMin=Integer.parseInt(startTime[0])*60+Integer.parseInt(startTime[1]);
        int endMin=Integer.parseInt(endTime[0])*60+Integer.parseInt(endTime[1]);
        java.util.Calendar c = java.util.Calendar.getInstance();
        int nowMIn=c.get(java.util.Calendar.HOUR_OF_DAY)*60+c.get(java.util.Calendar.MINUTE);
        if (!(nowMIn>startMin&&nowMIn<endMin)){
            helper.getView(R.id.text2).setVisibility(View.VISIBLE);
            ((TextView)helper.getView(R.id.text1)).setTextColor(context.getResources().getColor(R.color.intro_content_textcolor));
        }
    }
}
