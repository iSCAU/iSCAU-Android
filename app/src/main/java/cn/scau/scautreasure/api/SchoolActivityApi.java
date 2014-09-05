package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.ActivityCountModel;
import cn.scau.scautreasure.model.SchoolActivityModel;

/**
 * 校园活动 Api;
 */

@Rest(rootUrl = "http://iscaucms.sinaapp.com/index.php/Api/", converters = {GsonHttpMessageConverter.class})
public interface SchoolActivityApi {

    @Get("getActivities?time={time}")
    SchoolActivityModel.ActivityList getSchoolActivity(long time);

    //http://iscaucms.sinaapp.com/index.php/Api/activityCount?lastUpdate=123456789
    @Get("activityCount?lastUpdate={lastUpdate}")
    ActivityCountModel getActivityCount(String lastUpdate);

}
