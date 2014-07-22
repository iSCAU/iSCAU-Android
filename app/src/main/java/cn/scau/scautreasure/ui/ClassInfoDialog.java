package cn.scau.scautreasure.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.ClassModel;

public class ClassInfoDialog extends AlertDialog {
    private Context mContext;
    private ClassModel classModel;
    public ClassInfoDialog(Context context, int theme,Bundle extra) {
        super(context, theme);
        mContext = context;
        classModel = (ClassModel)extra.getParcelable("ClassModel");
    }
    public ClassInfoDialog(Context context, Bundle extra) {
        super(context);
        mContext = context;
        classModel = (ClassModel)extra.getSerializable("ClassModel");
    }
    private Button delete,edit,close;
    private TextView className,teacher,week,day;
    private LinearLayout whole_class_table;

    public interface onDeleteClick{
        void onClick();
    }
    public interface onEditClick{
        void onClick();
    }

    private onDeleteClick mOnDeleteClick;
    private onEditClick mOnEditClick;
    public void setCallBack(onDeleteClick d,onEditClick e){
        mOnDeleteClick = d;
        mOnEditClick = e;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wholeclassinfo);
        whole_class_table = (LinearLayout)findViewById(R.id.whole_class_table);
        whole_class_table.setLayoutParams(new FrameLayout.LayoutParams(400,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        delete = (Button) findViewById(R.id.course_delete);
        edit = (Button) findViewById(R.id.course_edit);
        close = (Button) findViewById(R.id.close_button);
        className = (TextView)findViewById(R.id.course_class);
        teacher = (TextView)findViewById(R.id.course_teacher);
        week = (TextView)findViewById(R.id.course_week);
        day = (TextView)findViewById(R.id.course_day);
        if(classModel == null) dismiss();
        className.setText(classModel.getClassname());
        teacher.setText(classModel.getTeacher());
        String weekString = mContext.getString(R.string.widget_week_start)+
                classModel.getStrWeek()+"-"+classModel.getEndWeek()+
                mContext.getString(R.string.widget_week_end);
        if(classModel.getDsz()!=null && !classModel.getDsz().equals("")) weekString += "("+classModel.getDsz()+")";
        week.setText(weekString);
        day.setText(mContext.getString(R.string.course_day)+classModel.getDay());
        View.OnTouchListener changeAlpha = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Drawable d = v.getBackground();
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    d.setAlpha(50);
                else
                    d.setAlpha(255);
                v.setBackgroundDrawable(d);
                return false;
            }
        };
        close.setOnTouchListener(changeAlpha);
        delete.setOnTouchListener(changeAlpha);
        edit.setOnTouchListener(changeAlpha);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnDeleteClick != null) mOnDeleteClick.onClick();
                dismiss();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnEditClick != null) mOnEditClick.onClick();
                dismiss();
            }
        });
    }

}

