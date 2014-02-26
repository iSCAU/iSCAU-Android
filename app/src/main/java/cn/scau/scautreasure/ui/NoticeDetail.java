package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.api.NoticeApi;
import cn.scau.scautreasure.helper.UIHelper;
import cn.scau.scautreasure.model.NoticeModel;
import cn.scau.scautreasure.util.CryptUtil;
import com.actionbarsherlock.app.SherlockActivity;
import com.devspark.appmsg.AppMsg;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * 校园通知详细阅读页面：
 * User: special
 * Date: 13-8-27
 * Time: 下午3:12
 * Mail: specialcyci@gmail.com
 */
@EActivity( R.layout.notice_detail )
public class NoticeDetail extends InjectedSherlockActivity {

    @App
    AppContext app;

    @RestService
    NoticeApi api;

    @Extra
    String url;

    @Extra
    String title;

    @Extra
    String time;

    @ViewById
    TextView tv_title,tv_content,tv_time;

    @AfterViews
    void init(){
        getSupportActionBar().hide();

        tv_title.setText(title);
        tv_time.setText(time);
        loadData();
    }

    @UiThread
    void showSuccessResult(String content){
        tv_content.setText(content);
    }


    /**
     * 展示http请求异常结果
     * @param requestCode
     */
    @UiThread
    void showErroResult(int requestCode){
        if(requestCode == 404){
            AppMsg.makeText(this, getString(R.string.tips_default_null), AppMsg.STYLE_CONFIRM).show();
        }else{
            app.showError(requestCode,this);
        }
    }

    @Background( id = UIHelper.CANCEL_FLAG )
    void loadData(Object... params) {
        try{
            url = CryptUtil.base64_url_safe(url);
            NoticeModel l = api.getContent(url);
            showSuccessResult(l.getContent());
        }catch (HttpStatusCodeException e){
            showErroResult(e.getStatusCode().value());
        }
    }
}