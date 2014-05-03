package cn.scau.scautreasure.ui;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import cn.scau.scautreasure.AppContext;
import cn.scau.scautreasure.R;
import cn.scau.scautreasure.util.CryptUtil;

/**
 * 登陆窗口(仅仅用于保存帐号密码)
 * User:  Special Leung
 * Date:  13-7-26
 * Time:  下午8:17
 * Mail:  specialcyci@gmail.com
 */
@NoTitle
@EActivity(R.layout.login)
public class Login extends Activity {

    @App
    AppContext app;

    @Bean
    CryptUtil cryptUtil;

    @ViewById
    EditText   edt_userName,edt_eduSysPassword,edt_libPassword,edt_cardPassword;

    @Extra
    boolean    isStartFormMenu = false;

    @AfterViews
    void setUpViews(){
        edt_userName.setText(app.userName);
        edt_eduSysPassword.setText(app.eduSysPassword);
        edt_libPassword.setText(app.libPassword);
        edt_cardPassword.setText(app.cardPassword);
    }

    @Click
    void tv_special_entrance(){
        saveAccount("ilovescau","1","2","3");
    }

    @Click
    void btn_save(){

        String userName       = edt_userName.getText().toString().trim();
        String eduSysPassword = edt_eduSysPassword.getText().toString().trim();
        String libPassword    = edt_libPassword.getText().toString().trim();
        String cardPassword   = edt_cardPassword.getText().toString().trim();

        if (userNameHasInput(userName) && onePasswordHasInput(eduSysPassword,libPassword,cardPassword)){
            saveAccount(userName,eduSysPassword,libPassword,cardPassword);
        }else if( !userNameHasInput(userName) ){
            Toast.makeText(this,R.string.save_input_tips_username,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,R.string.save_input_tips_password,Toast.LENGTH_LONG).show();
        }
    }

    private boolean userNameHasInput(String userName){
        return !userName.equals("");
    }

    private boolean onePasswordHasInput(String eduSysPassword, String libPassword, String cardPassword){
        return (!eduSysPassword.equals("") || !libPassword.equals("") || !cardPassword.equals(""));
    }


    private void saveAccount(String userName, String eduSysPassword, String libPassword, String cardPassword){
        // Base64.NO_WRAP will not padding a %0a;
        AppContext.userName        = userName;
        AppContext.eduSysPassword  = eduSysPassword;
        AppContext.libPassword     = libPassword;
        AppContext.cardPassword    = cardPassword;

        eduSysPassword      = cryptUtil.encrypt(eduSysPassword);
        libPassword         = cryptUtil.encrypt(libPassword);
        cardPassword        = cryptUtil.encrypt(cardPassword);

        app.config.userName().put(userName);
        app.config.eduSysPassword().put(eduSysPassword);
        app.config.libPassword().put(libPassword);
        app.config.cardPassword().put(cardPassword);

        if(!isStartFormMenu)
            Main_.intent(this).start();
        finish();
    }

}
