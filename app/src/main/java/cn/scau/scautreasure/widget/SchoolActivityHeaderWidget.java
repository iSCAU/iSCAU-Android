package cn.scau.scautreasure.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.R;

/**
 * 校园活动列表头部;
 */
@EViewGroup(R.layout.schoolactivity_header)
public class SchoolActivityHeaderWidget extends LinearLayout {
    final String today = "今天没有活动，\n喝杯茶休息会。";
    final String tomorrow = "明天没有活动，\n可以睡个懒觉。";
    final String later = "未来都没有活动，\n不过请期待更新。";
    @ViewById
    ImageView imageView;
    @ViewById
    TextView textView;
    @ViewById
    LinearLayout innerLayout;


    public SchoolActivityHeaderWidget(Context context) {
        super(context);
    }

    /*
     * 在build之后，才可以调用setUp
     */
    public void setUp(String when) {
        if ("today".equals(when)) {
            imageView.setImageDrawable(new ColorDrawable(Color.GREEN));
            textView.setText(today);
        } else if ("tomorrow".equals(when)) {
            imageView.setImageDrawable(new ColorDrawable(0xFFFF8C00));
            textView.setText(tomorrow);
        } else {
            imageView.setImageDrawable(new ColorDrawable(0xFFFF00DE));
            textView.setText(later);
        }
    }

    public void gone() {
        LinearLayout.LayoutParams lp = new LayoutParams(0, 0);
        innerLayout.setLayoutParams(lp);
        innerLayout.requestLayout();
    }

    public void visible() {
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        innerLayout.setLayoutParams(lp);
        innerLayout.requestLayout();
    }
}
