package cn.scau.scautreasure.ui.sns;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.sns.SNS;
import com.avos.sns.SNSBase;
import com.avos.sns.SNSCallback;
import com.avos.sns.SNSException;
import com.avos.sns.SNSType;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import cn.scau.scautreasure.R;

@EActivity(R.layout.sns_login)
public class Login extends ActionBarActivity {

    private final static String SINA_APP_ID = "1058353952";
    private final static String SINA_APP_KEY = "f9b42c8a19457c25d61d75d45c0e7663";
    private final static String SINA_APP_CALLBACK = "http://www.huanongbao.com";
    private SNSType loginType;

    private SNSCallback loginCallback = new SNSCallback() {
        @Override
        public void done(final SNSBase object, SNSException e) {
            if (e == null) {
                SNS.loginWithAuthData(object.userInfo(), new LogInCallback() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                    }
                });
            }
        }
    };

    @Click
    void sns_login_sina() {
        loginType = SNSType.AVOSCloudSNSSinaWeibo;
        try {
            SNS.setupPlatform(SNSType.AVOSCloudSNSSinaWeibo, SINA_APP_ID, SINA_APP_KEY, SINA_APP_CALLBACK);
            SNS.loginWithCallback(this, SNSType.AVOSCloudSNSSinaWeibo, loginCallback);
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SNS.onActivityResult(requestCode, resultCode, data, loginType);
    }
}
