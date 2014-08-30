package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.List;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.model.CourseCommentModel;

public class CourseCommentsAdapter extends QuickAdapter<CourseCommentModel> {
    private static Bitmap[] userImg = null;
    private static int perPicX = 100;
    private static int perPicY = 100;

    public CourseCommentsAdapter(Context context, int layoutResId, List<CourseCommentModel> data) {
        super(context, layoutResId, data);
        if (userImg == null) initUserImg(context);

    }

    private void initUserImg(Context ctx) {
        Bitmap allCommentsFace = decodeResource(ctx.getResources(),
                R.drawable.comments_face);
        int pWidth = allCommentsFace.getWidth();
        int pHeight = allCommentsFace.getHeight();
        int pNumber = pWidth / 100 * pHeight / 100;
        Log.e("pWidth", String.valueOf(pWidth));
        Log.e("pHeight", String.valueOf(pHeight));
        userImg = new Bitmap[pNumber];
        int k = 0;
        for (int i = 0; i < pWidth; i += perPicX)
            for (int j = 0; j < pHeight; j += perPicY) {
                userImg[k] = Bitmap.createBitmap(allCommentsFace, i, j, perPicX, perPicY);
                k++;
            }
    }

    protected void convert(BaseAdapterHelper baseAdapterHelper, CourseCommentModel model) {
        baseAdapterHelper.setText(R.id.userInfo, model.getUserName() + " (" +
                getDate(model.getTime()) + ")")
                .setText(R.id.content, model.getComment())
                .setText(R.id.hashomework, model.getHasHomework() ? "有作业" : "无作业")
                .setText(R.id.ischeck, model.getIsCheck() ? "不点名" : "要点名")
                .setText(R.id.exam, "考试形式：" + model.getExam());

        setExtraBackground(baseAdapterHelper, R.id.hashomework, model.getHasHomework());
        setExtraBackground(baseAdapterHelper, R.id.ischeck, model.getIsCheck());
        setUserImg(baseAdapterHelper);
    }

    private String getDate(String orl) {
        int p = orl.indexOf(' ');
        if (p > 0)
            return orl.substring(0, p);
        else
            return orl;
    }

    @Deprecated
    private String getExtra(CourseCommentModel m) {
        return (m.getHasHomework() ? "有" : "无") + "作业、" +
                (m.getIsCheck() ? "有" : "不") + "点名、" +
                "考试情况：\"" + m.getExam() + "\"";
    }

    private void setExtraBackground(BaseAdapterHelper baseAdapterHelper, int ViewId,
                                    boolean IsYes) {
        View v = baseAdapterHelper.getView(ViewId);
        if (IsYes)
            v.setBackgroundResource(R.drawable.radius_background_yes);
        else
            v.setBackgroundResource(R.drawable.radius_background_no);
    }

    private void setUserImg(BaseAdapterHelper baseAdapterHelper) {
        ImageView v = baseAdapterHelper.getView(R.id.userImg);
        Bitmap bitmap = userImg[(int) (userImg.length * Math.random())];
        v.setImageBitmap(bitmap);
    }

    /*
        No Scale Decode
     */
    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }
}
