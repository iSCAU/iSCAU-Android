package cn.scau.scautreasure.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.scau.scautreasure.R;

/**
 * Created by macroyep on 15/1/29.
 * Time:00:41
 */
public class ItemButton extends LinearLayout {

    TextView tv_title;
    TextView tv_subtitle;
    TextView iv_logo;
    LinearLayout linearLayout;

    public ItemButton(Context context) {
        super(context);
    }

    public ItemButton(Context context, String title, String subtitle, String logo_key, int color) {
        this(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_item_button, this);
        linearLayout = (LinearLayout) findViewById(R.id.onclick);
        tv_title = (TextView) findViewById(R.id.item_title);
        tv_subtitle = (TextView) findViewById(R.id.item_subtitle);
        iv_logo = (TextView) findViewById(R.id.item_logo);
        tv_title.setText(title);
        iv_logo.setBackgroundColor(color);
        tv_subtitle.setText(subtitle);
        iv_logo.setText(logo_key);
    }

    public ItemButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    public TextView getIv_logo() {
        return iv_logo;
    }

    public void setIv_logo(TextView iv_logo) {
        this.iv_logo = iv_logo;
    }

    public void setOnClick(OnClickListener l) {
        linearLayout.setOnClickListener(l);
    }
}
