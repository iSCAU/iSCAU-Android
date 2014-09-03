package cn.scau.scautreasure.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import antistatic.spinnerwheel.WheelHorizontalView;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import cn.scau.scautreasure.R;

/**
 * 参数选择的选择部件;
 * User: special
 * Date: 13-8-25
 * Time: 上午11:37
 * Mail: specialcyci@gmail.com
 */
@EViewGroup(R.layout.param_widget)
public class ParamWidget extends LinearLayout {

    @ViewById
    ImageView param_iv;

    @ViewById
    TextView param_lable;

    @ViewById
    WheelHorizontalView param_wheel;

    @ViewById
    ImageView separator;

    @StringArrayRes
    String[] yes_or_no;

    private Context ctx;
    private ArrayWheelAdapter adapter;

    public ParamWidget(Context context) {
        super(context);
        this.ctx = context;
    }

    public ParamWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
    }

    public ParamWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.ctx = context;
    }

    /**
     * set up the param view with lable text and param list;
     *
     * @param lableText
     * @param paramList
     */
    public void initView(String lableText, String[] paramList, int index) {

        param_lable.setText(lableText);

        adapter = new ArrayWheelAdapter<String>(ctx, paramList);
        adapter.setItemResource(R.layout.param_wheel_text);
        adapter.setItemTextResource(R.id.text);
        param_wheel.setViewAdapter(adapter);

    }

    /**
     * set up the param view with yes or no options.
     *
     * @param lableText
     * @param index
     */
    public void initViewWithYesOrNoOption(String lableText, int index) {
        initView(lableText, yes_or_no, index);
    }

    /**
     * return the wheel control
     *
     * @return
     */
    public WheelHorizontalView getWheel() {
        return param_wheel;
    }

    /**
     * return the wheelAdapter
     *
     * @return
     */
    public ArrayWheelAdapter getAdapter() {
        return adapter;
    }

    /**
     * return the selected content of wheel
     *
     * @return
     */
    public String getSelectedParam() {
        return (String) adapter.getItemText(param_wheel.getCurrentItem());
    }

    /**
     * return the options value "yes" or "no",
     * only work for init views with yes or
     * no options.
     */
    public boolean getYesOrNo() {
        return getWheel().getCurrentItem() == 0;
    }

    /**
     * set the options value "yes" or "no",
     * only work for init views with yes or
     * no options.
     *
     * @param yesOrNo
     */
    public void setYesOrNo(boolean yesOrNo) {
        getWheel().setCurrentItem(yesOrNo ? 0 : 1);
    }

    public void setSeparatorVisable(int visable) {
        separator.setVisibility(visable);
    }
}
