package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.NoticeModel;

/**
 * 校园通知Api;
 * User:  Special Leung
 * Date:  13-7-16
 * Time:  下午9:35
 * Mail:  specialcyci@gmail.com
 */

@Rest(rootUrl = "http://hnbnodb.huanongbao.com/index.php?s=notice/", converters = {GsonHttpMessageConverter.class})
public interface NoticeApi {

    @Get("getlist/{page}")
    NoticeModel.NoticeList getList(int page);

    @Get("getcontent/{url}")
    NoticeModel getContent(String url);

}
