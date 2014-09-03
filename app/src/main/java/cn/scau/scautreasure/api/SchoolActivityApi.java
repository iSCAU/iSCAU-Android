package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.SchoolActivityModel;

/**
 * 校园活动 Api;
 */

@Rest(rootUrl = "http://iscaucms.sinaapp.com/index.php/Api/getActivities?", converters = {GsonHttpMessageConverter.class})
public interface SchoolActivityApi {

    @Get("time={time}")
    SchoolActivityModel.ActivityList getSchoolActivity(long time);

}
