package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.UpLoadUsersModel;

/**
 * Created by apple on 14-9-5.
 */
@Rest(rootUrl = "http://iscaucms.sinaapp.com/index.php?m=Analy" ,converters = {GsonHttpMessageConverter.class})
public interface UsersApi  {

    /*
    上传用户信息
    返回:
   成功->{"result":"success"}
   失败,参数缺失->{"result":"args_error"}
     */
    //参数:学号,功能,专业
    @Get("&a=add&number={number}&type={type}&message={message}")
    UpLoadUsersModel upLoadUsers(String number,String type,String message);



}
