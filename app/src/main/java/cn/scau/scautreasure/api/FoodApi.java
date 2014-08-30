package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;

import cn.scau.scautreasure.model.FoodShopModel;

/**
 * Splash Api;
 */

@Rest(rootUrl = "http://iscaucms.sinaapp.com/index.php/Api/syncFood?", converters = {GsonHttpMessageConverter.class})
public interface FoodApi {

    @Get("lastUpdate={time}")
    FoodShopModel.ShopList getFoodShop(long time) ;

}
