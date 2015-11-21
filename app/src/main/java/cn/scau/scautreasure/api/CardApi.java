package cn.scau.scautreasure.api;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.CardRecordModel;

/**
 * User: special
 * Date: 13-9-21
 * Time: 下午3:30
 * Mail: specialcyci@gmail.com
 */
@Rest(rootUrl = "http://hnbnodb.huanongbao.com/index.php?s=card/", converters = {GsonHttpMessageConverter.class})
public interface CardApi {

    @Get("gettoday/{userName}/{passWord}")
    CardRecordModel.RecordList getToday(String userName, String passWord);

    @Get("gethistory/{userName}/{passWord}/{startdate}/{enddate}/{page}")
    CardRecordModel.RecordList getHistory(String userName, String passWord, String startdate, String enddate, int page);

}
