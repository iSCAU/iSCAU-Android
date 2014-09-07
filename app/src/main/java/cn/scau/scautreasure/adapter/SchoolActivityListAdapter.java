package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.SchoolActivityHelper;
import cn.scau.scautreasure.model.SchoolActivityModel;
import cn.scau.scautreasure.widget.SchoolActivityContentWebView;
import cn.scau.scautreasure.widget.SchoolActivityToggle;

/**
 * User:  stcdalyc
 */

public class SchoolActivityListAdapter extends QuickAdapter<SchoolActivityModel> {

    private final Context ctx;
    private final SchoolActivityHelper helper;

    public SchoolActivityListAdapter(Context context, SchoolActivityHelper helper) {
        super(context, R.layout.schoolactivity_listitem);
        ctx = context;
        this.helper = helper;
    }

    String getTimeText(String original) {
        String result = original;
        try {
            Timestamp ts = Timestamp.valueOf(original);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ts);
            result = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" +
                    calendar.get(Calendar.DAY_OF_MONTH) + "日  " + calendar.get(Calendar.HOUR) + ":" +
                    calendar.get(Calendar.MINUTE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void convert(final BaseAdapterHelper baseAdapterHelper, final SchoolActivityModel model) {

        baseAdapterHelper.setText(R.id.place, model.getPlace())
                .setText(R.id.title, model.getTitle())
                .setText(R.id.association, model.getAssociation())
                .setText(R.id.time, getTimeText(model.getTime()));

        final SchoolActivityToggle toggle_button = baseAdapterHelper.getView(R.id.expandable_toggle_button);
        final View expandable = baseAdapterHelper.getView(R.id.expandable);
        final View cross_bar = baseAdapterHelper.getView(R.id.cross_bar);
        final ImageView triangle = baseAdapterHelper.getView(R.id.triangle);
        final View toggle_triangle = baseAdapterHelper.getView(R.id.toggle_triangle);
        final View fake_toggle_triangle = baseAdapterHelper.getView(R.id.fake_toggle_triangle);

        toggle_button.controlWebView(expandable, fake_toggle_triangle);
        toggle_triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_button.performClick();
            }
        });
        toggle_button.setExtraOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) cross_bar.getLayoutParams();
                if (flp.gravity == Gravity.TOP) {
                    triangle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flp.gravity = Gravity.BOTTOM;
                            triangle.setImageResource(R.drawable.icon_green_triangle_up);
                            toggle_triangle.setBackgroundColor(Color.rgb(250, 250, 250));
                        }
                    }, 330);
                } else {
                    triangle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flp.gravity = Gravity.TOP;
                            triangle.setImageResource(R.drawable.icon_green_triangle);
                            toggle_triangle.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }, 330);
                }
            }
        });

        if (model.getContent() == null || "".equals(model.getContent()))
            model.setContent("没有详细的说明哦！");
        final SchoolActivityContentWebView content = baseAdapterHelper.getView(R.id.content);
        content.setContent(model.getContent(),
                helper.getContentCacheName(model.getContent(), model.getT()));

        if (model.getIsNewOne())
            baseAdapterHelper.setVisible(R.id.icon_new, true);

        final ImageView logoView = (ImageView) baseAdapterHelper.getView(R.id.logo);
       /* Bitmap logo = helper.getBitmap(model.getLogoUrl(), model.getT());
        if (logo == null)
            helper.download(model.getLogoUrl(), model.getT(), new SchoolActivityHelper.OnDownloadStateChanged() {
                @Override
                public void onDownloadFinished(final Bitmap bitmap) {
                    logoView.post(new Runnable() {
                        @Override
                        public void run() {
                            logoView.setImageBitmap(bitmap);
                        }
                    });
                }

                @Override
                public void onDownloadFailure() {
                    logoView.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ctx, "Download logo Failure", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            });
        else {
            logoView.setImageBitmap(logo);
        }*/
        AppContext.loadImage(model.getLogoUrl(), logoView, null);
    }
}
