package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
/*
10-2重写
 */
public class OrderListAdapter extends BaseAdapter{
    private  Context context;
    private List<ShopMenuDBModel> data;
    public OrderListAdapter(Context context,List<ShopMenuDBModel> data) {
        this.context=context;
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        R.layout.order_item_layout
        ViewHolder holder=null;
        if (view ==null){
            holder=new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, null);
            holder.title=(TextView)view.findViewById(R.id.foodName);
            holder.count=(TextView)view.findViewById(R.id.foodCount);
            holder.price=(TextView)view.findViewById(R.id.foodMoney);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        holder.title.setText(data.get(i).getFood_name());
        holder.count.setText(String.valueOf(data.get(i).getCount())+" 份");
        holder.price.setText( "¥ " + String.valueOf(data.get(i).getFood_price() * data.get(i).getCount()));
        setBg(view,i);
        return view;
    }

    private void setBg(View view,int pos) {
        if (pos % 2 == 0) {
            view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.list_item_click));
        } else {
           view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.list_item_click1));
        }
    }

    class ViewHolder{
        public TextView title;
        public TextView count;
        public TextView price;
    }

}



/*
public class OrderListAdapter extends QuickAdapter<ShopMenuDBModel> {
    private Context context;
    public OrderListAdapter(Context context, int layoutResId, List<ShopMenuDBModel> data) {
        super(context, layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, ShopMenuDBModel item) {
        helper.setText(R.id.foodName,item.getFood_name());
        helper.setText(R.id.foodCount, String.valueOf(item.getCount())+" 份");
        helper.setText(R.id.foodMoney, "¥ " + String.valueOf(item.getFood_price() * item.getCount()));
        setBg(helper);

    }
    private void setBg(BaseAdapterHelper helper) {
        if (helper.getPosition() % 2 == 0) {
            helper.getView(R.id.list_item_bg).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.list_item_click));
        } else {
            helper.getView(R.id.list_item_bg).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.list_item_click1));
        }
    }
}
*/