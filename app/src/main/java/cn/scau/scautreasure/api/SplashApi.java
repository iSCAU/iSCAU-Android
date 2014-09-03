package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.SplashModel;

/**
 * Splash Api;
 */

@Rest(rootUrl = "http://iscaucms.sinaapp.com/index.php?m=Api&a=splash&", converters = {GsonHttpMessageConverter.class})
public interface SplashApi {

    @Get("lastupdate={time}")
    SplashModel.SplashList getSplash(long time);

}
