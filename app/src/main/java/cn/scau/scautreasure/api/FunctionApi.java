package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.FunctionModel;

/**
 * Created by macroyep on 2/4/15.
 * Time:16:42
 */
@Rest(rootUrl = "http://iscaucms.sinaapp.com/apps/webapp", converters = {GsonHttpMessageConverter.class})
public interface FunctionApi {
    @Get("/api.php?student_id={student_id}&device_token={device_token}")
    FunctionModel.FunctionList getFunctionList(String student_id, String device_token);
}
