package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.ClassModel;
import cn.scau.scautreasure.model.ClassRoomModel;
import cn.scau.scautreasure.model.CookieModel;
import cn.scau.scautreasure.model.ExamModel;
import cn.scau.scautreasure.model.GoalModel;
import cn.scau.scautreasure.model.LoginModel;
import cn.scau.scautreasure.model.ParamModel;
import cn.scau.scautreasure.model.PersonModel;
import cn.scau.scautreasure.model.PickClassModel;

/**
 * 教务系统Api;
 * User:  Special Leung
 * Date:  13-7-16
 * Time:  下午9:35
 * Mail:  specialcyci@gmail.com
 */

@Rest(rootUrl = "http://wx.huanongbao.com/", converters = {GsonHttpMessageConverter.class})
public interface CookieApi {

    @Get("cookie/cookie.php")
    CookieModel getCookie();//错误时候{"msg":"password_error"}

    @Get("cookie/login.php?num={userName}&pwd={passWord}&cookie={cookie}&code={code}")
    LoginModel loginCookie(String userName,String passWord,String cookie,String code);

}
