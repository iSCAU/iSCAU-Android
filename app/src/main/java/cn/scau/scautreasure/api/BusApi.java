package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.BusLineModel;
import cn.scau.scautreasure.model.BusSiteModel;
import cn.scau.scautreasure.model.BusStateModel;

/**
 * 校巴Api;
 * User:  Special Leung
 * Date:  13-7-16
 * Time:  下午9:35
 * Mail:  specialcyci@gmail.com
 */

@Rest( rootUrl = "http://iscaucms.sinaapp.com/index.php/Bus/",converters = {GsonHttpMessageConverter.class})
public interface BusApi {

    @Get("line")
    BusLineModel.LineList getLine();

    @Get("site/line/{line}/direction/{direction}")
    BusSiteModel.SiteList getSite(String line, String direction);

    @Get("state/line/{line}/direction/{direction}")
    BusStateModel.StateList getBusState(String line, String direction);

}
