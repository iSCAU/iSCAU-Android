package cn.scau.scautreasure.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.devspark.appmsg.AppMsg;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.util.DateUtil;

/**
 * 条件选择
 * User:  Special Leung
 * Date:  13-8-15
 * Time:  下午4:13
 * Mail:  specialcyci@gmail.com
 */
@EFragment(R.layout.card_param)
public class CardParam extends CommonFragment {

    @App
    AppContext app;
    @Bean
    DateUtil dateUtil;
    @ViewById
    LinearLayout linear_parent;
    @ViewById
    EditText edt_start_date;
    @ViewById
    EditText edt_end_date;
    private EditText currentEditText;
    private CaldroidFragment dialogCaldroidFragment;
    private CaldroidListener listener = new CaldroidListener() {
        @Override
        public void onSelectDate(Date date, View view) {
            currentEditText.setText(dateUtil.parseCardQueryDate(date));
            dialogCaldroidFragment.dismiss();
        }
    };

    @AfterViews
    void initActionBar() {
        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setTitle(R.string.title_params);
    }

    @Touch
    void edt_start_date(MotionEvent motionEvent) {
        layout_start_date(motionEvent);
    }

    @Touch
    void edt_end_date(MotionEvent motionEvent) {
        layout_end_date(motionEvent);
    }

    @Touch
    void layout_start_date(MotionEvent motionEvent) {
        showCaldroidDialog(edt_start_date, motionEvent);
    }

    @Touch
    void layout_end_date(MotionEvent motionEvent) {
        showCaldroidDialog(edt_end_date, motionEvent);
    }

    private void showCaldroidDialog(EditText targetView, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            initCaldroidDialog();
            dialogCaldroidFragment.show(getSherlockActivity().getSupportFragmentManager(),
                    "CALDROID_DIALOG_FRAGMENT");
            currentEditText = targetView;
        }
    }

    private void initCaldroidDialog() {
        dialogCaldroidFragment = new CaldroidFragment();
        dialogCaldroidFragment.setCaldroidListener(listener);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS,
                true);
        bundle.putString(CaldroidFragment.DIALOG_TITLE,
                "Select a date");
        dialogCaldroidFragment.setArguments(bundle);
    }

    /**
     * 继续查询按钮点击事件;
     */
    @Click
    void btn_continue() {
        try {
            if (isRightStartAndEndDate())
                startDetailFragment();
            else
                AppMsg.makeText(getSherlockActivity(), R.string.tips_card_date_wrong, AppMsg.STYLE_ALERT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isRightStartAndEndDate() {
        int startDate = Integer.valueOf(edt_start_date.getText().toString());
        int endDate = Integer.valueOf(edt_end_date.getText().toString());
        return !(startDate == 0 || endDate == 0 || (startDate > endDate));
    }

    private void startDetailFragment() throws Exception {
        Fragment fragment = new Card_();
        UIHelper.addFragment(getSherlockActivity(), fragment, "startAndEndDate", buildParamsValue());
    }

    private ArrayList<String> buildParamsValue() {
        ArrayList<String> value = new ArrayList<String>();
        value.add(edt_start_date.getText().toString());
        value.add(edt_end_date.getText().toString());
        return value;
    }

}