package cn.scau.scautreasure.ui;

import android.view.View;
import android.widget.EditText;


import org.androidannotations.annotations.AfterViews;


import org.androidannotations.annotations.Bean;


import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;

import cn.scau.scautreasure.service.UpLoadUsersService_;
import cn.scau.scautreasure.util.CryptUtil;
import cn.scau.scautreasure.widget.AppToast;

/**
 * 帐号设置
 */
@EActivity(R.layout.login)
public class Login extends BaseActivity {


    @ViewById
    EditText edt_userName, edt_eduSysPassword, edt_libPassword, edt_cardPassword;

    @Extra
    String startTips;

    @Extra
    boolean runMainActivity = false;

    @Override
    void back() {
        if (runMainActivity)
            Main_.intent(this).start();
        finish();

    }

    @AfterViews
    void setUpViews() {
        edt_userName.setText(app.config.userName().get());
        edt_eduSysPassword.setText(app.config.eduSysPassword().get());
        edt_libPassword.setText(app.config.libPassword().get());
        edt_cardPassword.setText(app.config.cardPassword().get());

        if (startTips != null) {
            AppToast.show(this, startTips, 0);
        }
        setTitleText("帐号设置");
        setMoreButtonText("保存");
        setMoreButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edt_userName.getText().toString().trim();
                String eduSysPassword = edt_eduSysPassword.getText().toString().trim();
                String libPassword = edt_libPassword.getText().toString().trim();
                String cardPassword = edt_cardPassword.getText().toString().trim();

                if (userNameHasInput(userName) && onePasswordHasInput(eduSysPassword, libPassword, cardPassword)) {
                    saveAccount(userName, eduSysPassword, libPassword, cardPassword);

                } else if (!userNameHasInput(userName)) {
                    AppToast.show(Login.this, "你还没输入帐号", 0);
                } else {
                    AppToast.show(Login.this, "你还没输入教务系统密码或图书馆密码", 0);
                }
            }
        });
    }


    private boolean userNameHasInput(String userName) {
        return !userName.equals("");
    }

    private boolean onePasswordHasInput(String eduSysPassword, String libPassword, String cardPassword) {
        return (!eduSysPassword.equals("") || !libPassword.equals("") || !cardPassword.equals(""));
    }


    private void saveAccount(String userName, String eduSysPassword, String libPassword, String cardPassword) {
        // Base64.NO_WRAP will not padding a %0a;
        AppContext.userName = userName;
        AppContext.eduSysPassword = eduSysPassword;
        AppContext.libPassword = libPassword;
        AppContext.cardPassword = cardPassword;

        eduSysPassword = cryptUtil.encrypt(eduSysPassword);
        libPassword = cryptUtil.encrypt(libPassword);
        cardPassword = cryptUtil.encrypt(cardPassword);

        app.config.userName().put(userName);
        app.config.eduSysPassword().put(eduSysPassword);
        app.config.libPassword().put(libPassword);
        app.config.cardPassword().put(cardPassword);

        //LoginService_.intent(getApplicationContext()).start();
        UpLoadUsersService_.intent(getApplicationContext()).start();

        if (runMainActivity)
            Main_.intent(this).start();
        finish();
    }

}
