package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import org.w3c.dom.Text;

import java.util.List;

import cn.scau.scautreasure.AppConfig;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.FoodShopDBModel;
import cn.scau.scautreasure.model.FoodShopModel;

/**
 * Created by apple on 14-8-24.
 */


/**
 * 10-2重写原生adapter进行修bug
 */


public class FoodShopAdapter extends BaseAdapter {

    private Context context;
    private List<FoodShopDBModel> data;

    public FoodShopAdapter(Context context, List<FoodShopDBModel> data) {
        this.context = context;
        this.data = data;
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
//        R.layout.food_shop_list_layout
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.food_shop_list_layout, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.shopLogo);
            viewHolder.textView = (TextView) view.findViewById(R.id.text1);
            viewHolder.goicon = (ImageView) view.findViewById(R.id.goicon);
            viewHolder.restText = (TextView) view.findViewById(R.id.text2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        checkTime(data.get(i).getStart_time(), data.get(i).getEnd_time(), viewHolder);
        viewHolder.textView.setText(data.get(i).getShop_name());
        setLogo(viewHolder.imageView, data.get(i).getLogo_url());

        return view;
    }

    private void setLogo(ImageView imageView, String url) {

        AppContext.loadImage(url, imageView, null);

    }

    private void checkTime(String start, String end, ViewHolder holder) {
        String startTime[] = start.split(":");
        String endTime[] = end.split(":");
        int startMin = Integer.parseInt(startTime[0]) * 60 + Integer.parseInt(startTime[1]);
        int endMin = Integer.parseInt(endTime[0]) * 60 + Integer.parseInt(endTime[1]);
        java.util.Calendar c = java.util.Calendar.getInstance();
        int nowMIn = c.get(java.util.Calendar.HOUR_OF_DAY) * 60 + c.get(java.util.Calendar.MINUTE);
        ImageView imageView = holder.imageView;
        ImageView goIcon = holder.goicon;
        if (!((nowMIn > startMin) && (nowMIn < endMin))) {
            holder.restText.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(context.getResources().getColor(R.color.intro_content_textcolor));
            if (Build.VERSION.SDK_INT >= 11) {
                imageView.setAlpha(0.5f);
                goIcon.setAlpha(0.5f);
            }
        } else {
            holder.restText.setVisibility(View.GONE);
            holder.textView.setTextColor(context.getResources().getColor(R.color.black_text));
            if (Build.VERSION.SDK_INT >= 11) {
                imageView.setAlpha(1f);
                goIcon.setAlpha(1f);
            }
        }
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ImageView goicon;
        public TextView restText;

    }
}
