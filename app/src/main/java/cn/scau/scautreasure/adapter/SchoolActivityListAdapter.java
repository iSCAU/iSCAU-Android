package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import cn.scau.scautreasure.ui.SchoolActivityContent;
//import cn.scau.scautreasure.ui.SchoolActivityContent_;
import cn.scau.scautreasure.ui.SchoolActivityContent_;
import cn.scau.scautreasure.widget.SchoolActivityContentWebView;

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

    @Override
    protected void convert(final BaseAdapterHelper baseAdapterHelper, final SchoolActivityModel model) {

        baseAdapterHelper.setText(R.id.place, model.getPlace())
                .setText(R.id.title, model.getTitle())
                .setText(R.id.association, model.getAssociation())
                .setText(R.id.time, helper.getTimeText(model.getTime()));

        final View expandable = baseAdapterHelper.getView(R.id.expandable_toggle_button);
        expandable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              SchoolActivityContent_.intent(expandable.getContext()).model(model).start();
            }
        });
        if (model.getContent() == null || "".equals(model.getContent()))
            model.setContent("没有详细的说明哦！");

        if (model.getIsNewOne())
            baseAdapterHelper.setVisible(R.id.icon_new, true);

        final ImageView logoView = baseAdapterHelper.getView(R.id.logo);
        AppContext.loadImage(model.getLogoUrl(), logoView, null);
    }
}
