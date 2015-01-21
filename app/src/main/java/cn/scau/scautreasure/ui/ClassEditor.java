package cn.scau.scautreasure.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.gc.materialdesign.views.ButtonFlat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.util.ArrayList;
import java.util.List;

import antistatic.spinnerwheel.WheelHorizontalView;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.LogCenter;
import cn.scau.scautreasure.helper.StringHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.model.KeyValueModel;
import cn.scau.scautreasure.model.ParamModel;
import cn.scau.scautreasure.util.DateUtil;
import cn.scau.scautreasure.widget.AppToast;
import cn.scau.scautreasure.widget.RichButton;

/**
 * 添加课程/修改课程
 */
@EActivity(R.layout.classtable_editor)
public class ClassEditor extends BaseActivity implements View.OnClickListener {


    @Extra
    ClassModel model;
    @Extra
    boolean isNewClass = false;
    @Extra
    int position;
    @Bean
    StringHelper stringHelper;
    @Bean
    DateUtil dateUtil;
    @ViewById(R.id.edt_classname)
    EditText edt_classname;
    @ViewById(R.id.edt_teacher)
    EditText edt_teacher;
    @ViewById(R.id.edt_place)
    EditText edt_place;
    @ViewById(R.id.rich_layout)
    LinearLayout rich_layout;
    @ViewById(R.id.more)
    Button btn_modify;
    @StringArrayRes
    String[] weekdays, notes, week, dsz;


    private RichButton weekDayButton;
    private RichButton dszButton;
    private RichButton startNodeButton;
    private RichButton endNodeButton;
    private RichButton startWeekButton;
    private RichButton endWeekButton;

    RichButton.Callback callbackButton = new RichButton.Callback() {
        @Override
        public void afterItemClickListener(KeyValueModel model) {

        }
    };

    void initRichButton() {
        List<KeyValueModel> weekDayList = new ArrayList<KeyValueModel>();
        for (String s : weekdays) {
            weekDayList.add(new KeyValueModel(s, 0));
        }
        weekDayButton = new RichButton(this, weekDayList, callbackButton);
        weekDayButton.getTv_title().setText("星期几");
        weekDayButton.getTv_subtitle().setText(isNewClass ? weekdays[0] : model.getDay());


        List<KeyValueModel> dszList = new ArrayList<KeyValueModel>();
        for (String s : dsz) {
            dszList.add(new KeyValueModel(s, 0));
        }
        dszButton = new RichButton(this, dszList, callbackButton);
        dszButton.getTv_title().setText("单双周");
        dszButton.getTv_subtitle().setText(isNewClass ? dsz[0] : model.getDsz());


        List<KeyValueModel> startNodeList = new ArrayList<KeyValueModel>();
        for (String s : notes) {
            startNodeList.add(new KeyValueModel(s, 0));
        }
        startNodeButton = new RichButton(this, startNodeList, callbackButton);
        startNodeButton.getTv_title().setText("开始节次");

        List<KeyValueModel> endNodeList = new ArrayList<KeyValueModel>();
        for (String s : notes) {
            endNodeList.add(new KeyValueModel(s, 0));
        }
        endNodeButton = new RichButton(this, endNodeList, callbackButton);
        endNodeButton.getTv_title().setText("结束节次");

        List<KeyValueModel> startWeekList = new ArrayList<KeyValueModel>();
        for (String s : week) {
            startWeekList.add(new KeyValueModel(s, 0));
        }
        startWeekButton = new RichButton(this, startWeekList, callbackButton);
        startWeekButton.getTv_title().setText("开始周");
        startWeekButton.getTv_subtitle().setText(isNewClass ? week[0] : String.valueOf(model.getStrWeek()));

        List<KeyValueModel> endWeekList = new ArrayList<KeyValueModel>();
        for (String s : week) {
            endWeekList.add(new KeyValueModel(s, 0));
        }
        endWeekButton = new RichButton(this, endWeekList, callbackButton);
        endWeekButton.getTv_title().setText("结束周");
        endWeekButton.getTv_subtitle().setText(isNewClass ? week[week.length - 1] : String.valueOf(model.getEndWeek()));

        rich_layout.addView(weekDayButton);
        rich_layout.addView(dszButton);
        rich_layout.addView(startNodeButton);
        rich_layout.addView(endNodeButton);
        rich_layout.addView(startWeekButton);
        rich_layout.addView(endWeekButton);


    }

    private RichButton.Callback callback = new RichButton.Callback() {
        @Override
        public void afterItemClickListener(KeyValueModel model) {
            Log.i(getClass().getName(), model.toString());
        }
    };

    @AfterViews
    void initData() {
        initRichButton();

        initView();
    }

    void initView() {
        setMoreButtonText("完成");
        setMoreButtonOnClick(this);
        setTitleText(isNewClass ? "添加新课程" : "修改课程");

        try {
            edt_place.setText(model.getLocation());
            edt_teacher.setText(model.getTeacher());
            edt_classname.setText(model.getClassname());

            setNoteRelativeView();
            setDSZRelativeView();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void setNoteRelativeView() throws Exception {
        if (isNewClass) {
            startNodeButton.getTv_subtitle().setText(notes[0]);
            endNodeButton.getTv_subtitle().setText(notes[0]);
            LogCenter.i(getClass(), Thread.currentThread(), notes[0]);
        } else {
            // 设置节次;
            String[] note = model.getNode().split(",");
            LogCenter.i(getClass(), Thread.currentThread(), model.getNode());
            if (note.length > 1) {
                startNodeButton.getTv_subtitle().setText(note[0]);
                endNodeButton.getTv_subtitle().setText(note[note.length - 1]);
                LogCenter.i(getClass(), Thread.currentThread(), notes[0]);


            } else {
                startNodeButton.getTv_subtitle().setText(model.getNode());
                endNodeButton.getTv_subtitle().setText(model.getNode());
                LogCenter.i(getClass(), Thread.currentThread(), notes[0]);

            }
        }
    }

    private void setDSZRelativeView() throws Exception {
        if (model.getDsz() != null)
            for (int index = 0; index < dsz.length; index++)
                if (model.getDsz().trim().equals(dsz[index])) {
                    dszButton.getTv_subtitle().setText(dsz[index]);
                }

        int day = dateUtil.chineseToNumDay(model.getDay());
        weekDayButton.getTv_subtitle().setText(weekdays[day]);
    }

    /**
     * 返回当前滚轮所选择的文本;
     *
     * @param wheel
     * @param adapter
     *
     * @return
     */
    private String getWheelCurrentText(WheelHorizontalView wheel, ArrayWheelAdapter adapter) {
        return (String) adapter.getItemText(wheel.getCurrentItem());
    }

    @Override
    public void onClick(View view) {
        if (!edt_classname.getText().toString().trim().equals("")) {
            // 判断开始节数是否大于结束节数;
            int startNote = Integer.parseInt(startNodeButton.getTv_subtitle().getText().toString());
            int endNote = Integer.parseInt(endNodeButton.getTv_subtitle().getText().toString());
            int startWeek = Integer.parseInt(startWeekButton.getTv_subtitle().getText().toString());
            int endWeek = Integer.parseInt(endWeekButton.getTv_subtitle().getText().toString());
            if (endNote < startNote) {
                AppToast.show(this, "开始节次不能大于结束节次", 0);

                return;
            }
            if (endWeek < startWeek) {
                AppToast.show(this, "开始周不能大于结束周", 0);
                return;
            }

            // 生成节次信息;
            String[] notes = new String[endNote - startNote + 1];
            for (int note = startNote; note <= endNote; note++)
                notes[note - startNote] = String.valueOf(note);
            String strNote = stringHelper.join(",", notes);

            // 设置信息;
            model.setNode(strNote);
            model.setTeacher(edt_teacher.getText().toString().trim());
            model.setClassname(edt_classname.getText().toString().trim());
            model.setLocation(edt_place.getText().toString().trim());
            model.setDsz(dszButton.getTv_subtitle().getText().toString());
            model.setDay(weekDayButton.getTv_subtitle().getText().toString());
            model.setStrWeek(startWeek);
            model.setEndWeek(endWeek);

            Intent data = new Intent();
            data.putExtra("class", model);
            this.setResult(RESULT_OK, data);
            finish();

        } else {
            finish();
        }
    }

}
