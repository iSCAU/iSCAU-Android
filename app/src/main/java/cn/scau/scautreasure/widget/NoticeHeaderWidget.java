package cn.scau.scautreasure.widget;

import android.content.Context;
import android.widget.LinearLayout;

import org.androidannotations.annotations.EViewGroup;

import cn.scau.scautreasure.R;

/**
 * 校园通知列表页面头部图片;
 * User: special
 * Date: 13-8-31
 * Time: 上午11:19
 * Mail: specialcyci@gmail.com
 */
@EViewGroup(R.layout.notice_header)
public class NoticeHeaderWidget extends LinearLayout {

    public NoticeHeaderWidget(Context context) {
        super(context);
    }

}
