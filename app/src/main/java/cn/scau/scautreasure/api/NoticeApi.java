package cn.scau.scautreasure.api;

import cn.scau.scautreasure.model.*;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * 校园通知Api;
 * User:  Special Leung
 * Date:  13-7-16
 * Time:  下午9:35
 * Mail:  specialcyci@gmail.com
 */

@Rest(rootUrl = "http://3.huanongbao.sinaapp.com/index.php?s=notice/", converters = { GsonHttpMessageConverter.class } )
public interface NoticeApi{

    @Get("getlist/{page}")
    NoticeModel.NoticeList getList(int page);

    @Get("getcontent/{url}")
    NoticeModel getContent(String url);

}
