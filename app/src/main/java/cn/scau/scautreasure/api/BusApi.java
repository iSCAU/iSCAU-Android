package cn.scau.scautreasure.api;

import cn.scau.scautreasure.model.*;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * 校巴Api;
 * User:  Special Leung
 * Date:  13-7-16
 * Time:  下午9:35
 * Mail:  specialcyci@gmail.com
 */

@Rest(rootUrl = "http://115.28.144.49/bus/", converters = { GsonHttpMessageConverter.class } )
public interface BusApi{

    @Get("getline")
    BusLineModel.LineList getLine();

    @Get("getsite/{line}/{direction}")
    BusSiteModel.SiteList getSite(String line,String direction);

    @Get("getbustate/{line}/{direction}")
    BusStateModel.StateList getBusState(String line,String direction);

}
