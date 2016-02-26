package cn.scau.scautreasure.api;

/**
 * Created by zzb on 2016-2-26.
 */
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import cn.scau.scautreasure.model.FunctionModel;





@Rest(rootUrl="http://mywpserver.sinaapp.com/index.php?s=/function/", converters = { GsonHttpMessageConverter.class })
public interface  FunctionApi{
    @Get("functionlist")
    FunctionModel.FunctionList functionList();


}