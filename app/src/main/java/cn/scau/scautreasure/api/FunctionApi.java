package cn.scau.scautreasure.api;

/**
 * Created by zzb on 2016-2-26.
 */
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import cn.scau.scautreasure.model.FunctionModel;





@Rest(rootUrl="http://wx.huanongbao.com/api/api.php", converters = { GsonHttpMessageConverter.class })
public interface  FunctionApi{
    @Get("?option=appfunction")
    FunctionModel.FunctionList functionList();
}