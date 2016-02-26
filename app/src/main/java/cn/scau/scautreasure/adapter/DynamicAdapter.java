package cn.scau.scautreasure.adapter;

import android.widget.TextView;
import cn.scau.scautreasure.R;
import android.content.Context;
import cn.scau.scautreasure.widget.CircleImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import java.util.ArrayList;
import cn.scau.scautreasure.model.FunctionModel;
import cn.scau.scautreasure.helper.ColorHelper;
/**
 * Created by zzb on 2016-2-26.
 */
public class DynamicAdapter extends BaseAdapter {

    Context mContext;

    ArrayList<FunctionModel> list;

    public DynamicAdapter(Context mContext, ArrayList<FunctionModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int i) {
        return list.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.dynamic_listview_item, null);
            viewHolder.mainText = (TextView) view.findViewById(R.id.maintext);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.circleImageView=(CircleImageView) view.findViewById(R.id.cimg);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();

        }

        viewHolder.mainText.setText(list.get(i).getFirsttext());
        viewHolder.title.setText(list.get(i).getTitle());
        viewHolder.circleImageView.setImageResource(ColorHelper.randomColor());

        return view;
    }

    public class ViewHolder {
        public TextView mainText;
        public TextView title;
        public CircleImageView circleImageView;

    }

}