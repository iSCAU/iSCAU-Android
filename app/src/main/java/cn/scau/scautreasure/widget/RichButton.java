package cn.scau.scautreasure.widget;

import android.app.Dialog;
import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;


import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.KeyValueModel;

/**
 * 必须先show再setTitle,setSubTitle
 * Created by macroyep on 15/1/20.
 * Time:09:37
 */
public class RichButton extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView tv_title;
    private TextView tv_subtitle;
    private LinearLayout linearLayout;
    private List<KeyValueModel> list;
    private Context context;
    private Dialog dialog;
    private ListView listView;
    private Callback callback;


    public RichButton(Context context, List<KeyValueModel> list, Callback callback) {
        this(context);
        this.list = list;
        this.context = context;
        this.callback = callback;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.rich_button_layout, this);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tv_title = (TextView) findViewById(R.id.btn_title);
        tv_subtitle = (TextView) findViewById(R.id.btn_subtitle);
        AppViewDrawable.build(tv_subtitle, 2, 0, 0, 20, 20);
        linearLayout.setOnClickListener(this);
        initListDialog();

    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public List<KeyValueModel> getList() {
        return list;
    }

    public void setList(List<KeyValueModel> list) {
        this.list = list;
    }

    public RichButton(Context context) {
        super(context);
    }

    public RichButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public ListView getListView() {
        return listView;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public TextView getTv_subtitle() {
        return tv_subtitle;
    }

    public void setTv_subtitle(TextView tv_subtitle) {
        this.tv_subtitle = tv_subtitle;
    }

    @Override
    public void onClick(View view) {
        dialog.show();
    }

    private void initListDialog() {
        dialog = new Dialog(context, R.style.Dialog_Notitle);
        View view = (View) LayoutInflater.from(context).inflate(
                R.layout.layout_listview, null);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new DialogListAdapter());
        listView.setOnItemClickListener(this);
        dialog.setContentView(view);


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tv_subtitle.setText(list.get(i).getKey());
        callback.afterItemClickListener(list.get(i));
        dialog.dismiss();
    }


    class DialogListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.text_list_item, null);
                holder.textView = (TextView) view.findViewById(R.id.text);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.textView.setText(list.get(i).getKey());
            return view;
        }


        class ViewHolder {
            public TextView textView;

        }
    }

    public interface Callback {
        public void afterItemClickListener(KeyValueModel model);
    }
}
