package cn.scau.scautreasure.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.io.InputStream;
import java.net.URL;

import cn.scau.scautreasure.R;
import cn.scau.scautreasure.helper.SchoolActivityHelper;
import cn.scau.scautreasure.model.SchoolActivityModel;
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

        baseAdapterHelper.setText(R.id.place, "  " + model.getPlace())
                .setText(R.id.title, model.getTitle())
                .setText(R.id.association, model.getAssociation())
                .setText(R.id.time, model.getTime());

        if (model.getContent() == null || "".equals(model.getContent()))
            model.setContent("没有详细的说明哦！");
        final View toggle_button = baseAdapterHelper.getView(R.id.expandable_toggle_button);
        final SchoolActivityContentWebView content = baseAdapterHelper.getView(R.id.content);
        content.setContent(model.getContent(),
                helper.getContentCacheName(model.getContent(), model.getT()));

        if (model.getIsNewOne())
            baseAdapterHelper.setVisible(R.id.icon_new, true);

        final ImageView logoView = (ImageView) baseAdapterHelper.getView(R.id.logo);
        Bitmap logo = helper.getBitmap(model.getLogoUrl(), model.getT());
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
        }
    }
}
