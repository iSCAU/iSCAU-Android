package cn.scau.scautreasure.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import cn.scau.scautreasure.R;

/**
 * Created by apple on 14-9-4.
 */
@EActivity(R.layout.english)
public class English extends CommonActivity {

    @AfterViews
    void initView() {
        setTitle("英语角");
    }
}
