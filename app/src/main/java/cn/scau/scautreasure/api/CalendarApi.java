package cn.scau.scautreasure.api;

import android.content.Context;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import cn.scau.scautreasure.model.CalendarModel;

/**
 * Created by apple on 14-9-5.
 */
//http://iscaucms.sinaapp.com/apps/calendar/api.php?
@Rest(rootUrl = "http://iscaucms.sinaapp.com/apps/calendar/api.php?", converters = {GsonHttpMessageConverter.class})
public interface CalendarApi {

    @Get("us={student_id}")
    CalendarModel.CalendarList getCalendar(String student_id);

}
