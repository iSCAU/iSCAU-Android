package cn.scau.scautreasure.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import org.androidannotations.annotations.EBean;

import cn.scau.scautreasure.AppConstant;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.ui.ClassEditor_;
import cn.scau.scautreasure.ui.ClassTable;

/**
 * User:  Special Leung
 * Date:  13-7-30
 * Time:  下午4:16
 * Mail:  specialcyci@gmail.com
 */
@EBean
public class ClassAdapter extends QuickAdapter<ClassModel> {

    private final Context ctx;
    private ClassTable fragment;

    public ClassAdapter(Context context) {
        super(context, R.layout.classtable_listitem);
        ctx = context;
    }

    public void setFragment(ClassTable fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void convert(final BaseAdapterHelper baseAdapterHelper, final ClassModel classModel) {

        baseAdapterHelper.setText(R.id.tv_classname, classModel.getClassname())
                .setText(R.id.tv_teacher, classModel.getTeacher())
                .setText(R.id.tv_classname_details, classModel.getClassname())
                .setText(R.id.tv_place, classModel.getLocation())
                .setText(R.id.tv_class_place, classModel.getLocation())
                .setText(R.id.tv_nodes, classModel.getNode()+"节@"+getStartTime(classModel.getNode()))
                .setText(R.id.tv_dsz, classModel.getDsz())
                .setText(R.id.tv_week_range, classModel.getStrWeek() + "-" + classModel.getEndWeek());

        setLeftImageViewColor(baseAdapterHelper);

        baseAdapterHelper.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() != R.id.expandable_toggle_button && view.getId() != R.id.btn_modify) {
                    baseAdapterHelper.getView(R.id.expandable_toggle_button).performClick();
                }
            }
        });

        baseAdapterHelper.getView(R.id.btn_modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.startActivityForResult(ClassEditor_.intent(ctx).model(classModel).get(), UIHelper.QUERY_FOR_EDIT_CLASS);
            }
        });

        baseAdapterHelper.getView(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage(R.string.dialog_sure_to_delete_lesson);
                builder.setNegativeButton(R.string.dialog_no, null);
                builder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fragment.deleteClass(classModel);
                    }
                });
                builder.show();
            }
        });
    }

    private String getStartTime(String node){
        String[] nodes = node.split(",");
        int firstNode=Integer.valueOf(nodes[0]);
        switch (firstNode){
            case 1 : return "8:00";
            case 2 : return "8:50";
            case 3 : return "10:05";
            case 4 : return "10:55";
            case 5 : return "12:30";
            case 6 : return "13:20";
            case 7 : return "14:30";
            case 8 : return "15:20";
            case 9 : return "16:35";
            case 10 : return "17:25";
            case 11 : return "19:30";
            case 12 : return "20:20";
            default:return "";


        }

    }
    private void setLeftImageViewColor(BaseAdapterHelper baseAdapterHelper) {
        int color = AppConstant.IV_COLOR[baseAdapterHelper.getPosition() % AppConstant.IV_COLOR.length];
        ImageView iv = baseAdapterHelper.getView(R.id.class_iv);
        iv.setImageDrawable(new ColorDrawable(color));
    }
}
