package cn.scau.scautreasure.ui;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
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

import antistatic.spinnerwheel.WheelHorizontalView;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.StringHelper;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.util.DateUtil;
import cn.scau.scautreasure.widget.AppToast;

/**
 * 添加课程/修改课程
 */
@EActivity(R.layout.classtable_editor)
public class ClassEditor extends BaseActivity {
    @Click(R.id.back)
    void onBack() {
        finish();
    }


    @ViewById(R.id.title_text)
    TextView title_text;


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
    @ViewById
    WheelHorizontalView wheel_weekday, wheel_dsz, wheel_note_start;
    @ViewById
    WheelHorizontalView wheel_note_end, wheel_week_start, wheel_week_end;
    @ViewById(R.id.more)
    Button btn_modify;
    @StringArrayRes
    String[] weekdays, notes, week, dsz;

    private ArrayWheelAdapter<String> adapter_weekday;
    private ArrayWheelAdapter<String> adapter_dsz;
    private ArrayWheelAdapter<String> adapter_note_start;
    private ArrayWheelAdapter<String> adapter_note_end;
    private ArrayWheelAdapter<String> adapter_week_start;
    private ArrayWheelAdapter<String> adapter_week_end;

    @AfterViews
    void initData() {

        adapter_dsz = UIHelper.buildWheelAdapter(this, dsz);
        adapter_weekday = UIHelper.buildWheelAdapter(this, weekdays);
        adapter_week_end = UIHelper.buildWheelAdapter(this, week);
        adapter_note_end = UIHelper.buildWheelAdapter(this, notes);
        adapter_note_start = UIHelper.buildWheelAdapter(this, notes);
        adapter_week_start = UIHelper.buildWheelAdapter(this, week);

        wheel_dsz.setViewAdapter(adapter_dsz);
        wheel_weekday.setViewAdapter(adapter_weekday);
        wheel_note_end.setViewAdapter(adapter_note_end);
        wheel_week_end.setViewAdapter(adapter_week_end);
        wheel_note_start.setViewAdapter(adapter_note_start);
        wheel_week_start.setViewAdapter(adapter_week_start);

        initView();
    }

    void initView() {
        btn_modify.setText("完成");
        title_text.setText(isNewClass ? "添加新课程" : "修改课程");
        try {
            edt_place.setText(model.getLocation());
            edt_teacher.setText(model.getTeacher());
            edt_classname.setText(model.getClassname());
            wheel_week_end.setCurrentItem(model.getEndWeek() - 1);
            wheel_week_start.setCurrentItem(model.getStrWeek() - 1);
            setNoteRelativeView();
            setDSZRelativeView();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setNoteRelativeView() throws Exception {
        // 设置节次;
        String[] note = model.getNode().split(",");
        if (note.length > 1) {
            wheel_note_start.setCurrentItem(Integer.parseInt(note[0]) - 1);
            wheel_note_end.setCurrentItem(Integer.parseInt(note[note.length - 1]) - 1);
        } else {
            int single_note = Integer.parseInt(model.getNode()) - 1;
            wheel_note_start.setCurrentItem(single_note);
            wheel_note_end.setCurrentItem(single_note);
        }

    }

    private void setDSZRelativeView() throws Exception {
        if (model.getDsz() != null)
            for (int index = 0; index < dsz.length; index++)
                if (model.getDsz().trim().equals(dsz[index])) {
                    wheel_dsz.setCurrentItem(index);
                }

        int day = dateUtil.chineseToNumDay(model.getDay());
        wheel_weekday.setCurrentItem(day);
    }

    @Click(R.id.more)
    void btn_modify() {
        if (!edt_classname.getText().toString().trim().equals("")) {
            // 判断开始节数是否大于结束节数;
            int startNote = Integer.parseInt(getWheelCurrentText(wheel_note_start, adapter_note_start));
            int endNote = Integer.parseInt(getWheelCurrentText(wheel_note_end, adapter_note_end));
            int startWeek = Integer.parseInt(getWheelCurrentText(wheel_week_start, adapter_week_start));
            int endWeek = Integer.parseInt(getWheelCurrentText(wheel_week_end, adapter_week_end));
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
            model.setDsz(getWheelCurrentText(wheel_dsz, adapter_dsz));
            model.setDay(getWheelCurrentText(wheel_weekday, adapter_weekday));
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

}
